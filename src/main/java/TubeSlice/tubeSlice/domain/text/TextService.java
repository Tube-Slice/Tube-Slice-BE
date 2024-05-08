package TubeSlice.tubeSlice.domain.text;

import TubeSlice.tubeSlice.domain.text.dto.response.ClovaSpeechResponseDto;
import TubeSlice.tubeSlice.domain.video.dto.request.GptRequest;
import TubeSlice.tubeSlice.domain.video.dto.response.GptResponse;
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


    public void uploadFile(String filePath) {
        String key = new File(filePath).getName();

        PutObjectRequest request = new PutObjectRequest(wavBucket, key, new File(filePath)).withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3Client.putObject(request);
    }

    public List<Map.Entry<Integer, String>> getScriptFromBucket(String fileName){ //key가 파일명

        S3Object result = amazonS3Client.getObject(new GetObjectRequest(scriptBucket, fileName));

        if (result == null){
            throw new RuntimeException("Script does not exist.");
        }

        HashMap<Integer, String> segments = new HashMap<>();

        List<Map.Entry<Integer, String>> script = new ArrayList<>();
        Text text = null;

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
                int start = segmentNode.get("start").asInt();   //6111은 6.111초
                segments.put(start, segmentNode.get("textEdited").asText());

                script.add(new AbstractMap.SimpleEntry<>(start, segmentNode.get("textEdited").asText()));
            }

            //스크립트 타임라인 순으로 출력.
            Collections.sort(script, new Comparator<Map.Entry<Integer, String>>() {
                @Override
                public int compare(Map.Entry<Integer, String> o1, Map.Entry<Integer, String> o2) {
                    return o1.getKey().compareTo(o2.getKey());
                }
            });

            for (Map.Entry<Integer, String> e: script){
                text = Text.builder()
                        .timeline(e.getKey())
                        .scripts(e.getValue())
                        .isSaved(true)
                        .script(null)
                        .build();
                textRepository.save(text);
            }


        } catch (IOException e) {
            System.err.println("Error in reading file from storage: " + e.getMessage());
            throw new RuntimeException("Error in reading file from storage", e);
        }





        return script;
    }

    public Object videoToScript(String objectStorageDataKey) {
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
//        try {
//          ResponseEntity<ClovaSpeechResponseDto> response =
//                  restTemplate.exchange(clova_speech_invoke_url, HttpMethod.POST ,request, ClovaSpeechResponseDto.class);
//          //return restTemplate.postForObject(clova_speech_invoke_url + "/recognizer/object-storage", request, Object.class);
//
//
//        } catch (Exception e) {
//            System.err.println("Error in recognizing from object storage: " + e.getMessage());
//            throw e;
//        }

        String token = response.getBody().getToken();

        //return response;
        String fileName = wavBucket + ":" + objectStorageDataKey + "_" + token + ".json";

        return getScriptFromBucket(fileName);
    }


    public String trimText(String script){
        String requestMessage = "\"" + script + "\" 이 스크립트에서 부자연스러운 문장들 고쳐줘.";

        GptRequest gptRequest = new GptRequest(gpt_model,new GptRequest.Message("user",script));
        GptResponse gptResponse = template.postForObject(gpt_api_url, gptRequest, GptResponse.class);

        return gptResponse.getChoices().get(0).getMessage().getContent();
    }

    public String summarize(String script){
        String requestMessage = "\"" + script + "\" 이 스크립트 내용 3줄로 요약해줘.";

        GptRequest gptRequest = new GptRequest(gpt_model,new GptRequest.Message("user",requestMessage));
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
