package TubeSlice.tubeSlice.domain.text;

import TubeSlice.tubeSlice.domain.script.Script;
import TubeSlice.tubeSlice.domain.script.ScriptRepository;
import TubeSlice.tubeSlice.domain.subtitle.Subtitle;
import TubeSlice.tubeSlice.domain.subtitle.SubtitleRepository;
import TubeSlice.tubeSlice.domain.text.dto.request.SummaryRequestDto;
import TubeSlice.tubeSlice.domain.text.dto.response.ClovaSpeechResponseDto;
import TubeSlice.tubeSlice.domain.text.dto.request.GptRequest;
import TubeSlice.tubeSlice.domain.text.dto.response.GptResponse;
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
import java.lang.reflect.Array;
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

    private String totalScriptsWithTimeline = "";

    @Transactional
    public Object videoToScript(String filePath) {
        String objectStorageDataKey = uploadFile(filePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-CLOVASPEECH-API-KEY", clova_speech_api_key);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("dataKey", objectStorageDataKey);   // 인식을 원하는 파일명
        requestBody.put("language", "ko-KR");
        requestBody.put("completion", "sync");  // 결과를 Object Storage에 저장할지 여부 : async -> 토큰 반환 -> 진행상태 확인가능. sync -> 변환 다 끝나면 응답옴.
        requestBody.put("resultToObs", true);
        requestBody.put("noiseFiltering", true);    // 노이즈 필터링 적용 여부
        requestBody.put("wordAlignment", true); // 단어 정렬 정보 포함 여부
        requestBody.put("fullText", true);  // 전체 인식 결과 텍스트를 출력할지 여부

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<ClovaSpeechResponseDto> response =
                restTemplate.exchange(clova_speech_invoke_url, HttpMethod.POST ,request, ClovaSpeechResponseDto.class);

        String token = response.getBody().getToken();

        String fileName = wavBucket + ":" + objectStorageDataKey + "_" + token + ".json";

        return getScriptFromBucket(fileName);
    }

    public String uploadFile(String filePath) {
        String key = new File(filePath).getName();

        PutObjectRequest request = new PutObjectRequest(wavBucket, key, new File(filePath)).withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3Client.putObject(request);

        return key;
    }
    
    @Transactional
    public List<Map.Entry<Double, String>> getScriptFromBucket(String fileName){ //key가 파일명

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

                totalScriptsWithTimeline += start + ":" + eachScript + "\n";
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

        Optional<Script> findScript = scriptRepository.findByVideoUrl(fileName);
        Script script = null;

        if (findScript.isEmpty()) {

            script = Script.builder()
                    .videoTitle(null)
                    .videoUrl(fileName)
                    .build();

            scriptRepository.save(script);

            //Text text = null;
            for (Map.Entry<Double, String> e : scripts) {

                Text text = Text.builder()
                        .timeline(e.getKey())
                        .scripts(e.getValue())
                        .isSaved(true)
                        .script(script)
                        .build();
                textRepository.save(text);
            }

            log.info("totalScriptsWithTimeline : {}", totalScriptsWithTimeline);

            String subtitles = trimText(totalScriptsWithTimeline);

            HashMap<Double, String> sub = trimResult(subtitles);
            //Subtitle subtitle = null;
            for (Map.Entry<Double, String> e : sub.entrySet()){
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




    @Transactional
    public String trimText(String script){
        String requestMessage1 = "내 질문에 대한 응답 json 형식으로 출력해줘.";
        String requestMessage2 = "\"" + script + "\" \n위 전체 스크립트를 읽어보고 중요한 핵심적인 내용에 해당하는 부분만 소제목 지어서 해당하는 시간과 소제목을 출력해줘. \n" +
                "출력 형식: \"'시간':'소제목'\"";

        List<GptRequest.Messages> messages = new ArrayList<>();
        messages.add(new GptRequest.Messages("system",requestMessage1));
        messages.add(new GptRequest.Messages("user",requestMessage2));

        HashMap<String, String> response_format = new HashMap<>();
        response_format.put("type","json_object");

        GptRequest gptRequest = new GptRequest(gpt_model, messages, response_format);
        GptResponse gptResponse = template.postForObject(gpt_api_url, gptRequest, GptResponse.class);

        String subtitles = gptResponse.getChoices().get(0).getMessage().getContent();

        return subtitles;
    }

    private HashMap<Double,String> trimResult(String jsonResult){

        HashMap<Double, String> result = new HashMap<>();

        jsonResult = jsonResult.replaceAll("\n", "").trim();
        jsonResult = jsonResult.replace("{", "").trim();
        jsonResult = jsonResult.replaceAll("}", "").trim();
        jsonResult = jsonResult.replaceAll(" ", "").trim();

        List<String> lines = List.of(jsonResult.split(","));

        for (String line : lines) {

            List<String> parts = List.of(line.split(":"));

            String value = parts.get(1).replaceAll("\"", "");

            Double tl = Double.valueOf(parts.get(0).replaceAll("\"",""));

            result.put(tl,value);

            log.info("{} : {}", tl, value);
        }

        return result;
    }

    public Object summarize(SummaryRequestDto summaryRequestDto){
        String requestMessage1 = "내 질문에 대한 응답 json 형식으로 출력해줘.";
        String requestMessage2 = "\"" + summaryRequestDto.getScript() + "\" 이 스크립트에서 핵심 내용" + summaryRequestDto.getRow() + "문장으로 요약해서 구분해서 보여줘.";

        List<GptRequest.Messages> messages = new ArrayList<>();
        messages.add(new GptRequest.Messages("system",requestMessage1));
        messages.add(new GptRequest.Messages("user",requestMessage2));

        HashMap<String, String> response_format = new HashMap<>();
        response_format.put("type","json_object");

        GptRequest gptRequest = new GptRequest(gpt_model,messages,response_format);
        GptResponse gptResponse = template.postForObject(gpt_api_url, gptRequest, GptResponse.class);

        return gptResponse.getChoices().get(0).getMessage().getContent();
    }

    public String getAudioFileFromYoutubeUrl(String youtubeUrl){

        try {
            // Command to download video
            String downloadCommand = "youtube-dl -f bestaudio -x --audio-format mp3 -o 'temp.%(ext)s' " + youtubeUrl;
            // Command to extract audio
            String extractCommand = "ffmpeg -i temp.mp3 -vn -acodec copy output.mp3";

            // Download video
            executeCommand(downloadCommand);
            // Extract audio
            executeCommand(extractCommand);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return " ";
    }

    private void executeCommand(String command) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        process.waitFor();
    }
}
