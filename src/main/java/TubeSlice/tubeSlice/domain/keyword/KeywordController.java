package TubeSlice.tubeSlice.domain.keyword;

import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/keywords")
public class KeywordController {

    private final KeywordService keywordService;

    @PostMapping("/post")
    @Operation(summary = "게시글 키워드 추가 API",description = "게시글에 필요한 키워드 객체 생성, KeywordResultDto 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "keyword", description = "키워드"),
    })
    public ApiResponse<KeywordResponseDto.KeywordInfoDto> createPostKeyword(@RequestParam String keyword){
        return ApiResponse.onSuccess(keywordService.createPostKeyword(keyword));
    }
}
