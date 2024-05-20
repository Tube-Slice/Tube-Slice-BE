package TubeSlice.tubeSlice.domain.text;

import TubeSlice.tubeSlice.domain.script.Script;
import TubeSlice.tubeSlice.domain.script.ScriptRepository;
import TubeSlice.tubeSlice.domain.subtitle.Subtitle;
import TubeSlice.tubeSlice.domain.subtitle.SubtitleRepository;
import TubeSlice.tubeSlice.domain.text.dto.request.TextRequestDto;
import TubeSlice.tubeSlice.domain.text.dto.response.TextResponseDto;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TextService {

    private final TextRepository textRepository;
    private final ScriptRepository scriptRepository;
    private final SubtitleRepository subtitleRepository;

    @Value("${gpt.api.url}")
    private String gpt_api_url;

    @Value("${gpt.model}")
    private String gpt_model;

    @Value("${clova.speech.api_key}")
    private String clova_speech_api_key;

    @Value("${clova.speech.invoke_url}")
    private String clova_speech_invoke_url;

    @Autowired
    private RestTemplate template;

    @Autowired
    private final AmazonS3Client amazonS3Client;

    private String wavBucket = "test-wav"; //kms 키 없는 버킷.
    private String scriptBucket = "script-file-bucket";


    @Transactional
    public List<Map.Entry<Double, String>> videoToScript(String youtubeUrl) {
        String filePath = getAudioFileFromYoutubeUrl(youtubeUrl);
        String objectStorageDataKey = uploadFile(filePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-CLOVASPEECH-API-KEY", clova_speech_api_key);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("dataKey", objectStorageDataKey);   // 인식을 원하는 파일명
        requestBody.put("language", "enko");    //영어, 한글 둘다 인식.
        requestBody.put("completion", "sync");  // 결과를 Object Storage에 저장할지 여부 : async -> 토큰 반환 -> 진행상태 확인가능. sync -> 변환 다 끝나면 응답옴.
        requestBody.put("resultToObs", true);
        requestBody.put("noiseFiltering", true);    // 노이즈 필터링 적용 여부
        requestBody.put("wordAlignment", true); // 단어 정렬 정보 포함 여부
        requestBody.put("fullText", true);  // 전체 인식 결과 텍스트를 출력할지 여부

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<TextResponseDto.ClovaSpeechResponseDto> response =
                restTemplate.exchange(clova_speech_invoke_url, HttpMethod.POST ,request, TextResponseDto.ClovaSpeechResponseDto.class);

        String token = response.getBody().getToken();

        //objectStorage에 저장되는 스크립트 파일명.
        String fileName = wavBucket + ":" + objectStorageDataKey + "_" + token + ".json";

        Script findScript = scriptRepository.findByVideoUrl(objectStorageDataKey);
        Script script = null;

        if (findScript == null) {

            script = Script.builder()
                    .videoTitle(objectStorageDataKey)
                    .videoUrl(youtubeUrl)
                    .scriptTitle(fileName)
                    .build();

            scriptRepository.save(script);
        } else {
            script = findScript;
        }

        return getScriptFromBucket(script);
    }

    public Object summarize(TextRequestDto.SummaryRequestDto summaryRequestDto){
        String requestMessage1 = "내 질문에 대한 응답을 json 형식으로 출력해줘.";
        String requestMessage2 = "\"" + summaryRequestDto.getScript() + "\"\n이 스크립트에서 핵심 내용을 요약해서 이해하기 쉽게"+ summaryRequestDto.getRow() + "문장으로 설명해줘.\n" +
                "출력 형식은 \"'1':'설명내용'\" 에 맞게 답해줘.";

        List<TextRequestDto.GptRequest.Messages> messages = new ArrayList<>();
        messages.add(new TextRequestDto.GptRequest.Messages("system",requestMessage1));
        messages.add(new TextRequestDto.GptRequest.Messages("user",requestMessage2));

        HashMap<String, String> response_format = new HashMap<>();
        response_format.put("type","json_object");

        TextRequestDto.GptRequest gptRequest = new TextRequestDto.GptRequest(gpt_model,messages,response_format);
        TextResponseDto.GptResponse gptResponse = template.postForObject(gpt_api_url, gptRequest, TextResponseDto.GptResponse.class);

        String content = gptResponse.getChoices().get(0).getMessage().getContent();

        return trimSummary(content);
    }

    public String uploadFile(String filePath) {
        String key = new File(filePath).getName();

        PutObjectRequest request = new PutObjectRequest(wavBucket, key, new File(filePath)).withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3Client.putObject(request);

        return key;
    }
    
    @Transactional
    public List<Map.Entry<Double, String>> getScriptFromBucket(Script script){ //key가 파일명
        String fileName = script.getScriptTitle();

        S3Object result = amazonS3Client.getObject(new GetObjectRequest(scriptBucket, fileName));

        if (result == null){
            throw new RuntimeException("Script does not exist.");
        }

        List<Map.Entry<Double, String>> scripts = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(result.getObjectContent()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            // JSON 파싱 및 segments 추출
            JsonNode rootNode = new ObjectMapper().readTree(stringBuilder.toString());
            for (JsonNode segmentNode : rootNode.get("segments")) {
                String start = segmentNode.get("start").asText();   //6111은 6.111초
                String eachScript = segmentNode.get("textEdited").asText();

                //타임라인 double 형으로 변환.
                if(start.length() > 3) {
                    start = start.substring(0, start.length() - 3) + "." + start.substring(start.length() - 3);
                }
                else if(start.equals("0")){
                    start = start + ".0";
                }
                Double startDouble = Double.parseDouble(start);

                scripts.add(new AbstractMap.SimpleEntry<>(startDouble, eachScript));

            }
            //스크립트 타임라인 순으로 정렬.
            Collections.sort(scripts, new Comparator<Map.Entry<Double, String>>() {
                @Override
                public int compare(Map.Entry<Double, String> o1, Map.Entry<Double, String> o2) {
                    return o1.getKey().compareTo(o2.getKey());
                }
            });
        } catch (IOException e) {
            System.err.println("Error in reading file from storage: " + e.getMessage());
            throw new RuntimeException("Error in reading file from storage", e);
        }

        Text findText = textRepository.findByScripts(scripts.get(0).getValue());

        if (findText == null) {
            //text 저장
            for (Map.Entry<Double, String> e : scripts) {

                Text text = Text.builder()
                        .timeline(e.getKey())
                        .scripts(e.getValue())
                        .isSaved(true)
                        .script(script)
                        .build();
                textRepository.save(text);
            }
            String totalScriptsWithTimeline = getTotalScriptsWithTimeline(scripts);
            String subtitles = trimText(totalScriptsWithTimeline);

            HashMap<Double, String> sub = trimResult(subtitles);

            //subtitle 저장.
            for (Map.Entry<Double, String> e : sub.entrySet()) {
                Subtitle subtitle = Subtitle.builder()
                        .subtitle(e.getValue())
                        .timeline(e.getKey())
                        .script(script)
                        .build();
                subtitleRepository.save(subtitle);
            }
        }

        return scripts;
    }

    public String getTotalScriptsWithTimeline(List<Map.Entry<Double, String>> scripts){
        String totalScriptsWithTimeline = "";

        for (Map.Entry<Double, String> e : scripts){
            totalScriptsWithTimeline += e.getKey() + ":" + e.getValue() + "\n";
        }
        log.info("totalScriptWithTimeLine: {}", totalScriptsWithTimeline);

        return totalScriptsWithTimeline;
    }

    public String getTotalScript(List<Map.Entry<Double, String>> scripts){
        String totalScript = "";

        for (Map.Entry<Double, String> e : scripts){
            totalScript += e.getValue() + "\n";
        }
        log.info("totalScript: {}", totalScript);

        return totalScript;
    }

    @Transactional
    public String trimText(String script){
        String requestMessage1 = "내 질문에 대한 응답을 json 형식으로 출력해줘.";
        String requestMessage2 = "\"" + script + "\" \n위 전체 스크립트를 읽어보고 중요한 핵심적인 내용에 해당하는 부분만 소제목 지어서 해당하는 시간과 소제목을 출력해줘. \n" +
                "출력 형식: \"'시간':'소제목'\"";

        List<TextRequestDto.GptRequest.Messages> messages = new ArrayList<>();
        messages.add(new TextRequestDto.GptRequest.Messages("system",requestMessage1));
        messages.add(new TextRequestDto.GptRequest.Messages("user",requestMessage2));

        HashMap<String, String> response_format = new HashMap<>();
        response_format.put("type","json_object");

        TextRequestDto.GptRequest gptRequest = new TextRequestDto.GptRequest(gpt_model, messages, response_format);
        TextResponseDto.GptResponse gptResponse = template.postForObject(gpt_api_url, gptRequest, TextResponseDto.GptResponse.class);

        return gptResponse.getChoices().get(0).getMessage().getContent();
    }

    private HashMap<Double,String> trimResult(String jsonResult){
        log.info("요약 내용: {}", jsonResult);
        HashMap<Double, String> result = new HashMap<>();

        //jsonResult = jsonResult.replaceAll("\n", "").trim();
        jsonResult = jsonResult.replace("{", "").trim();
        jsonResult = jsonResult.replaceAll("}", "").trim();

        List<String> lines = List.of(jsonResult.split("\n"));

        for (String line : lines) {

            List<String> parts = List.of(line.split(":"));

            String value = parts.get(1).replaceAll("\"", "");

            Double tl = Double.valueOf(parts.get(0).replaceAll("\"",""));

            result.put(tl,value);

            log.info("{} : {}", tl, value);
        }

        return result;
    }

    private HashMap<String,String> trimSummary(String jsonResult){
        log.info("요약 내용: {}", jsonResult);
        HashMap<String, String> result = new HashMap<>();

        //jsonResult = jsonResult.replaceAll("\n", "").trim();
        jsonResult = jsonResult.replace("{", "").trim();
        jsonResult = jsonResult.replaceAll("}", "").trim();

        List<String> lines = List.of(jsonResult.split("\n"));

        for (String line : lines) {

            List<String> parts = List.of(line.split(":"));

            String value = parts.get(1).replaceAll("\"", "");
            value = value.trim();

            String idx = parts.get(0).replaceAll("\"","");
            idx = idx.trim();

            result.put(idx,value);

            log.info("{} : {}", idx, value);
        }

        return result;
    }

    public String getAudioFileFromYoutubeUrl(String youtubeUrl){
        String filename = "mp3 파일 가져오기 실패.";

        //yt-dlp 파일 실행 위치.
        String ytDlpPath = "yt-dlp";
        //String ytDlpPath = "D:\\youtube-dl\\ffmpeg-7.0-essentials_build\\bin\\yt-dlp.exe";

        //mp3 파일 저장 경로. 서버 상에 경로 지정.
        String downloadDir = "yt-dlp/mp3/%(title)s.%(ext)s";
        //String downloadDir = "D:\\youtube-dl\\%(title)s.%(ext)s";

        try {
            // Command to download video
            String downloadCommand = String.format("%s -x --audio-format mp3 -o \"%s\" %s", ytDlpPath, downloadDir, youtubeUrl);

            // Command to print filename
            String printCommand = String.format("%s --print filename -o \"%s\" %s", ytDlpPath, downloadDir, youtubeUrl);

            executeCommand(downloadCommand);

            filename = executeCommand(printCommand);

            if (filename != null) {
                String ext = filename.substring(filename.lastIndexOf("."));
                filename = filename.replace(ext, ".mp3");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        log.info("filename: {}" ,filename);
        return filename;
    }

    private String executeCommand(String command) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        int i = 0;
        String filename = null;
        while ((line = reader.readLine()) != null) {
            filename = line;
        }
        process.waitFor();

        return filename;
    }
}
