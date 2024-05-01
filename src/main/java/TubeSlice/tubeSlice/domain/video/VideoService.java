package TubeSlice.tubeSlice.domain.video;

import TubeSlice.tubeSlice.domain.oauth.dto.response.NaverLoginDto;
import TubeSlice.tubeSlice.domain.video.dto.ClovaSpeechResponseDto;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.google.api.services.youtube.model.Video;
import lombok.RequiredArgsConstructor;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoService {

    @Value("${CLOVA_SPEECH_API_KEY}")
    private String clova_speech_api_key;

    @Value("${CLOVA_SPEECH_INVOKE_URL}")
    private String clova_speech_invoke_url;

    @Value("${youtube.api.key}")
    private String youtube_api_key;

    private String clove_speech_request_url = clova_speech_invoke_url + "/recognizer/object-storage";
    private String callback;

    @Autowired
    private final AmazonS3Client amazonS3Client;

    private String wavBucket = "test-wav"; //kms 키 없는 버킷.
    private String scriptBucket = "script-file-bucket";


    public void uploadFile(String filePath) {
        String key = new File(filePath).getName();

        PutObjectRequest request = new PutObjectRequest(wavBucket, key, new File(filePath)).withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3Client.putObject(request);
    }

    public Object getScriptFromBucket(String fileName){ //key가 파일명

        S3Object result = amazonS3Client.getObject(new GetObjectRequest(scriptBucket, fileName));

        HashMap<Integer, String> segments = new HashMap<>();
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
            }

        } catch (IOException e) {
            System.err.println("Error in reading file from storage: " + e.getMessage());
            throw new RuntimeException("Error in reading file from storage", e);
        }
        return segments;
    }

    public Object videoToScript(String objectStorageDataKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-CLOVASPEECH-API-KEY", clova_speech_api_key);

//        String requestBody = "{" +
//                "\"dataKey\": \"" + objectStorageDataKey + "\"," +  // 인식을 원하는 파일의 ObjectStorage 경로에 접근하기 위한 Key
//                "\"language\": \"ko-KO\"," +
//                "\"completion\": \"async\"," +  // 결과를 Object Storage에 저장할지 여부 : async -> 토큰 반환 -> 진행상태 확인가능. sync -> 변환 다 끝나면 응답옴.
//                "\"resultToObs\": true," +
//                //"\"noiseFiltering\": true," +   // 노이즈 필터링 적용 여부
//                //"\"wordAlignment\": true," +    // 단어 정렬 정보 포함 여부
//                "\"fullText\": true" +  // 전체 인식 결과 텍스트를 출력할지 여부
//                "}";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("dataKey", objectStorageDataKey);
        requestBody.put("language", "ko-KR");
        requestBody.put("completion", "sync");
        requestBody.put("resultToObs", true);
        requestBody.put("noiseFiltering", true);
        requestBody.put("wordAlignment", true);
        requestBody.put("fullText", true);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();

//        try {
            ResponseEntity<ClovaSpeechResponseDto> response =
                    restTemplate.exchange("https://clovaspeech-gw.ncloud.com/external/v1/7718/30fc9e3356c21227177859ff28b144526d9bcaf9802b1bc19ed5709ca482f8ae/recognizer/object-storage", HttpMethod.POST ,request, ClovaSpeechResponseDto.class);
            //return restTemplate.postForObject(clova_speech_invoke_url + "/recognizer/object-storage", request, Object.class);



//        } catch (Exception e) {
//            System.err.println("Error in recognizing from object storage: " + e.getMessage());
//            throw e;
//        }
        String token = response.getBody().getToken();
        String result = response.getBody().getResult();
        String message = response.getBody().getMessage();

        return response;
    }




    public String getWavFileFromYoutubeUrl(String youtubeUrl){



        return " ";
    }


}
