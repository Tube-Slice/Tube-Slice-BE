package TubeSlice.tubeSlice.domain.text;

import TubeSlice.tubeSlice.domain.text.dto.response.ClovaSpeechResponseDto;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/text")
public class TextController {

    private final TextService textService;

    @PostMapping("/translation")
    @Operation(summary = "영상에서 스크립트 추출", description = "유튜브 url을 입력 받아 영상의 스크립트를 반환한다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "변환에 실패하였습니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<Object> videoToScript(@RequestParam("fileName") String wavFile){



        return ApiResponse.onSuccess(textService.videoToScript(wavFile));
    }

    @PostMapping("/upload")
    public ApiResponse<ClovaSpeechResponseDto> uploadFile(@RequestParam("filePath") String filePath){

        textService.uploadFile(filePath);

        return ApiResponse.onSuccess(new ClovaSpeechResponseDto());
    }

    @GetMapping("/script")
    public ApiResponse<List<Map.Entry<Integer,String>>> getScript(@RequestParam("fileName") String fileName){

        List<Map.Entry<Integer,String>> result = textService.getScriptFromBucket(fileName);

        return ApiResponse.onSuccess(result);

    }

    @PostMapping("/summary")
    public ApiResponse<Object> gptSummarize(@RequestBody String script){


        return ApiResponse.onSuccess(textService.summarize(script));
    }

    @PostMapping("/trim")
    public ApiResponse<Object> trimText(@RequestBody String script){


        return ApiResponse.onSuccess(textService.trimText(script));
    }

}
