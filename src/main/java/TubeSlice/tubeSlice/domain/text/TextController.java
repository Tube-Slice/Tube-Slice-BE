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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SCRIPT401", description = "변환에 실패하였습니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<TextResponseDto.transResponseListDto> videoToScript(@RequestParam("youtubeUrl") String youtubeUrl){
        Script findScript = scriptRepository.findByVideoUrl(youtubeUrl);

        if (findScript != null){    //ObjectStorage에 변환된 기록 있으면 변환 없이 파일 가져오기.

            log.info("findScript: {}", findScript);
            return ApiResponse.onSuccess(new TextResponseDto.transResponseListDto(textService.getScriptFromBucket(findScript)));
        }

        return ApiResponse.onSuccess(new TextResponseDto.transResponseListDto(textService.videoToScript(youtubeUrl)));
    }

    @PostMapping("/summary")
    @Operation(summary = "동영상 요약하기",description = "동영상 요약하기")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SCRIPT401", description = "스크립트를 찾을 수 없습니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<Object> gptSummarize(@RequestParam("row") Integer row,
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


}
