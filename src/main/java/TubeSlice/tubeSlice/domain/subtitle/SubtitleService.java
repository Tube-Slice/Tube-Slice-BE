package TubeSlice.tubeSlice.domain.subtitle;

import TubeSlice.tubeSlice.domain.script.Script;
import TubeSlice.tubeSlice.domain.text.TextService;
import TubeSlice.tubeSlice.domain.text.dto.request.TextRequestDto;
import TubeSlice.tubeSlice.domain.text.dto.response.TextResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubtitleService {

    private final SubtitleRepository subtitleRepository;

    @Value("${gpt.model}")
    private String gpt_model;

    @Value("${gpt.api.url}")
    private String gpt_api_url;

    @Autowired
    private RestTemplate template;

    @Transactional
    public void saveSubtitle(List<TextResponseDto> scripts, Script script) {

        //subtitle은 무조건 저장. 아니면 gpt api 계속 호출해야함.
        String totalScriptsWithTimeline = getTotalScriptsWithTimeline(scripts);
        String totalScripts = getTotalScript(scripts);
        String subtitles = getSubtitles(totalScriptsWithTimeline, totalScripts.length());

        HashMap<Double, String> sub = trimSubtitle(subtitles);

        Subtitle findSubtitle = subtitleRepository.findByScript(script);

        if (findSubtitle == null) {

            for (Map.Entry<Double, String> e : sub.entrySet()) {
                Subtitle subtitle = Subtitle.builder()
                        .subtitle(e.getValue())
                        .timeline(e.getKey())
                        .script(script)
                        .build();
                subtitleRepository.save(subtitle);
            }
        }
    }

    public String getTotalScriptsWithTimeline(List<TextResponseDto> scripts){
        String totalScriptsWithTimeline = "";

        for (TextResponseDto e : scripts){
            totalScriptsWithTimeline += e.getTimeline() + ":" + e.getText() + "\n";
        }
        log.info("totalScriptWithTimeLine: {}", totalScriptsWithTimeline);

        return totalScriptsWithTimeline;
    }

    public String getTotalScript(List<TextResponseDto> scripts){
        String totalScript = "";

        for (TextResponseDto e : scripts){
            totalScript += e.getText() + "\n";
        }
        log.info("totalScript: {}", totalScript);

        return totalScript;
    }

    public String getSubtitles(String script, Integer size){
        log.info("스크립트 크기: {}", size);
        int count = 0;
        if (size < 1200){
            count = 4;
        } else if (size < 2000){
            count = 6;
        } else if (size < 5000){
            count = 8;
        } else if (size < 10000){
            count = 10;
        } else if (size < 15000){
            count = 12;
        } else if (size < 20000){
            count = 14;
        } else {
            count = 16;
        }

        String requestMessage1 = "내 질문에 대한 응답을 json 형식으로 출력해줘.";
        String requestMessage2 = "\"" + script + "\" \n위 전체 스크립트를 읽어보고 중요한 핵심적인 내용에 해당하는 부분만 소제목 지어서 해당하는 시간과 소제목을 " + count + "개 이내로 출력해줘. \n" +
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

    public HashMap<Double,String> trimSubtitle(String jsonResult){
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
}
