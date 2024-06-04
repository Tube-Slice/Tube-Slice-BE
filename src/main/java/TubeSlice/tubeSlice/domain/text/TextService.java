package TubeSlice.tubeSlice.domain.text;

import TubeSlice.tubeSlice.domain.script.Script;
import TubeSlice.tubeSlice.domain.script.ScriptRepository;
import TubeSlice.tubeSlice.domain.subtitle.Subtitle;
import TubeSlice.tubeSlice.domain.subtitle.SubtitleRepository;
import TubeSlice.tubeSlice.domain.subtitle.SubtitleService;
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
import org.springframework.beans.factory.annotation.Qualifier;
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

    private final SubtitleService subtitleService;

    private final ScriptRepository scriptRepository;


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

    @Autowired @Qualifier("amazonS3Client")
    private final AmazonS3Client amazonS3Client;

    private String wavBucket = "test-wav"; //kms 키 없는 버킷.
    private String scriptBucket = "script-file-bucket";

    @Transactional
    public List<TextResponseDto.transResponseDto> videoToScript(String youtubeUrl) {
        String filePath = getAudioFileFromYoutubeUrl(youtubeUrl);   // "mp3/파일이름.mp3"
        String objectStorageDataKey = uploadFile("mp3/" + filePath); //파일이름.mp3

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
        List<TextResponseDto.transResponseDto> result = getScriptFromBucket(script);

        subtitleService.saveSubtitle(result, script);

        File file = new File("mp3/" + objectStorageDataKey);
        boolean isDeleted = file.delete();

        log.info("파일 삭제: {}", isDeleted);

        return result;
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

        return key; //mp3 파일 이름
    }

    @Transactional
    public List<TextResponseDto.transResponseDto> getScriptFromBucket(Script script){ //key가 파일명
        String fileName = script.getScriptTitle();

        S3Object result = amazonS3Client.getObject(new GetObjectRequest(scriptBucket, fileName));

        if (result == null){
            throw new RuntimeException("Script does not exist.");
        }

        List<TextResponseDto.transResponseDto> scripts = new ArrayList<>();

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
                if(start.equals("0")){
                    start = start + ".0";
                } else if (start.length() == 1){
                    start = "0.00" + start;
                } else if (start.length() == 2) {
                    start = "0.0" + start;

                } else if (start.length() == 3) {
                    start = "0." + start;

                } else if(start.length() > 3) {
                    start = start.substring(0, start.length() - 3) + "." + start.substring(start.length() - 3);
                }

                Double startDouble = Double.parseDouble(start);

                scripts.add(new TextResponseDto.transResponseDto(startDouble, eachScript));

            }
            //스크립트 타임라인 순으로 정렬.
            Collections.sort(scripts, new Comparator<TextResponseDto.transResponseDto>() {
                @Override
                public int compare(TextResponseDto.transResponseDto o1, TextResponseDto.transResponseDto o2) {
                    return o1.getTimeline().compareTo(o2.getTimeline());
                }
            });
        } catch (IOException e) {
            System.err.println("Error in reading file from storage: " + e.getMessage());
            throw new RuntimeException("Error in reading file from storage", e);
        }

        return scripts;
    }



    public String getTotalScript(List<TextResponseDto.transResponseDto> scripts){
        String totalScript = "";

        for (TextResponseDto.transResponseDto e : scripts){
            totalScript += e.getText() + "\n";
        }
        log.info("totalScript: {}", totalScript);

        return totalScript;
    }



    private TextResponseDto.SummaryResponseListDto trimSummary(String jsonResult){
        log.info("요약 내용: {}", jsonResult);
        List<TextResponseDto.SummaryResponseDto> result = new ArrayList<>();

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
            Integer id = Integer.parseInt(idx);

            result.add(new TextResponseDto.SummaryResponseDto(id,value));

            log.info("{} : {}", idx, value);
        }

        return new TextResponseDto.SummaryResponseListDto(result);
    }



    public String getAudioFileFromYoutubeUrl(String youtubeUrl){
        String filename = "mp3 파일 가져오기 실패.";

        try {

            executeCommand(youtubeUrl);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        String DATA_DIRECTORY = "mp3/";
        File dir = new File(DATA_DIRECTORY);

        if (dir.exists() && dir.isDirectory()) {
            // 디렉토리 내의 모든 파일과 디렉토리 이름을 배열로 가져옵니다.
            String[] fileList = dir.list();

            // 파일 목록을 출력합니다.
            if (fileList != null) {
                for (String fn : fileList) {
                    log.info("mp3/ : {}", fn);
                    filename=fn;
                }
            } else {
                System.out.println("디렉토리 내에 파일이 없습니다.");
            }
        }


        log.info("filename: {}" ,filename);

        return filename;
    }

    private String executeCommand(String youtubeUrl) throws IOException, InterruptedException {

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("/usr/local/bin/yt-dlp", "-x", "--audio-format", "mp3", "-o", "'%(title)s.%(ext)s'", youtubeUrl);
        String filename = null;
        try {
            processBuilder.directory(new java.io.File("./mp3"));  // 원하는 작업 디렉토리로 변경

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));


            String line;
            while ((line = reader.readLine()) != null) {
                filename = line;
            }
            log.info("command: {}", filename);

            // 에러 출력
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "EUC-KR"));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println(errorLine);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Download completed successfully!");
            } else {
                System.err.println("다운로드 실패 Download failed with exit code " + exitCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        return filename;
    }
}
