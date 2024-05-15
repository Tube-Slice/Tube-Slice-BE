package TubeSlice.tubeSlice.domain.script;

import TubeSlice.tubeSlice.domain.script.dto.response.ScriptResponseDto;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/scripts")
public class ScriptController {

    private final ScriptService scriptService;

    @GetMapping("/{scriptId}/subtitles")
    @Operation(summary = "스크립트 소제목 가져오기 API",description = "스크립트 소제목 가져오기")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "scriptId", description = "스크립트 id"),
    })
    public ApiResponse<List<ScriptResponseDto.SubtitleResponseDto>> getSubtitles(@PathVariable(name = "scriptId") Long scriptId){

        return ApiResponse.onSuccess(scriptService.getSubtitles(scriptId));
    }
}
