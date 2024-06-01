package TubeSlice.tubeSlice.domain.script;

import TubeSlice.tubeSlice.domain.script.dto.response.ScriptResponseDto;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/scripts")
public class ScriptController {

    private final ScriptService scriptService;
    private final ScriptRepository scriptRepository;

    @GetMapping("/subtitles")
    @Operation(summary = "스크립트 소제목 가져오기 API",description = "스크립트 소제목 가져오기")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<List<ScriptResponseDto.SubtitleResponseDto>> getSubtitles(@RequestParam(name = "youtubeUrl") String youtubeUrl){
        //youtubeUrl을 입력으로 받아 소제목 가져오기.
        Script findScript = scriptRepository.findByVideoUrl(youtubeUrl);


        return ApiResponse.onSuccess(scriptService.getSubtitles(findScript.getId()));
    }
}
