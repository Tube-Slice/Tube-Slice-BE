package TubeSlice.tubeSlice.domain.text;

import TubeSlice.tubeSlice.domain.script.Script;
import TubeSlice.tubeSlice.domain.script.ScriptRepository;
import TubeSlice.tubeSlice.domain.text.dto.request.TextRequestDto;
import TubeSlice.tubeSlice.domain.text.dto.response.TextResponseDto;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/text")
public class TextController {

    private final TextService textService;
    private final ScriptRepository scriptRepository;

    @PostMapping("/translation")
    @Operation(summary = "영상에서 스크립트 추출", description = "유튜브 url을 입력 받아 영상의 스크립트를 반환한다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "변환에 실패하였습니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<Object> videoToScript(@RequestParam("youtubeUrl") String youtubeUrl){
        //String audioFile = new File(youtubeUrl).getName();
        Script findScript = scriptRepository.findByVideoUrl(youtubeUrl);

        if (findScript != null){    //ObjectStorage에 변환된 기록 있으면 변환 없이 파일 가져오기.

            log.info("findScript: {}", findScript);
            return ApiResponse.onSuccess(textService.getScriptFromBucket(findScript));
        }

        return ApiResponse.onSuccess(textService.videoToScript(youtubeUrl));
    }

    @PostMapping("/summary")
    @Operation(summary = "동영상 요약하기",description = "동영상 요약하기")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    @Parameters(value = {
            @Parameter(name = "scriptBody", description = "요약할 영상의 스크립트"),
    })
    public ApiResponse<Object> gptSummarize(@RequestParam("row") String row,
                                            @RequestParam("youtubeUrl") String youtubeUrl){
        Script findScript = scriptRepository.findByVideoUrl(youtubeUrl);

        if (findScript != null){    //ObjectStorage에 변환된 기록 있으면 변환 없이 파일 가져오기.

            log.info("findScript: {}", findScript);
            String totalScript = textService.getTotalScript(textService.getScriptFromBucket(findScript));
            return ApiResponse.onSuccess(textService.summarize(new TextRequestDto.SummaryRequestDto(row,totalScript)));
        }

        String totalScript = textService.getTotalScript(textService.videoToScript(youtubeUrl));
        return ApiResponse.onSuccess(textService.summarize(new TextRequestDto.SummaryRequestDto(row, totalScript)));
    }
//        {
//            "isSuccess": true,
//                "code": "COMMON200",
//                "message": "성공입니다.",
//                "result": "{\n    \"요약\": [\n        \"성인 ADHD의 특징: 인강이나 반복 작업 지루함, 물건을 잃어버리고 정리 잘 못함, 시작은 창대하나 속상하고 완료 어려움\",\n  " +
//                               "      \"대인관계 오해 경험: 대화 중 주의 분산, 상대방 표정 및 언어 무시, 양측 오해로 충돌\",\n    " +
//                            "    \"자책 대신 질병 인지: 치료로 자신감 회복 가능, 성인 ADHD 검사 및 치료 권고, 정신 건강 회복으로 가치 발견\"\n    ]\n}"
//        }

    @PostMapping("/upload")
    public ApiResponse<TextResponseDto.ClovaSpeechResponseDto> uploadFile(@RequestParam("filePath") String filePath){

        textService.uploadFile(filePath);

        return ApiResponse.onSuccess(new TextResponseDto.ClovaSpeechResponseDto());
    }

    @PostMapping("/youtube")
    public ApiResponse<Object> youtubeUrl(@RequestParam("youtubeUrl") String youtubeUrl) {

        return ApiResponse.onSuccess(textService.getAudioFileFromYoutubeUrl(youtubeUrl));
    }

}
