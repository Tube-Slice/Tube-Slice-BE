package TubeSlice.tubeSlice.domain.user;

import TubeSlice.tubeSlice.domain.post.dto.PostResponseDto;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me/posts")
    @Operation(summary = "나의 게시글 목록 가져오기 API",description = "내가 작성한 게시글 목록 가져오기, PostInfoDto의 list를 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<List<PostResponseDto.PostInfoDto>> getMyPostList(){
        User user = userService.findUser(1L);

        return ApiResponse.onSuccess(userService.getPostList(user));
    }


    @GetMapping("/{userId}/posts")
    @Operation(summary = "특정유저의 게시글 목록 가져오기 API",description = "특정유저가 작성한 게시글 목록 가져오기, PostInfoDto의 list를 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "userId", description = "특정 유저의 id"),
    })
    public ApiResponse<List<PostResponseDto.PostInfoDto>> getUserPostList(@PathVariable(name = "userId")Long userId){
        User user = userService.findUser(userId);

        return ApiResponse.onSuccess(userService.getPostList(user));
    }

    @GetMapping("/me/keywords")
    @Operation(summary = "나의 키워드 목록 가져오기 API",description = "내가 작성한 키워드 목록 가져오기, KeywordResponseDto list를 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<List<KeywordResponseDto.KeywordResultDto>> getMyKeywordList(){
        User user = userService.findUser(1L);

        return ApiResponse.onSuccess(userService.getUserKeywordList(user));
    }

    @GetMapping("/{userId}/keywords")
    @Operation(summary = "특정유저의 키워드 목록 가져오기 API",description = "특정유저가 작성한 키워드 목록 가져오기, KeywordResponseDto list를 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "userId", description = "특정 유저의 id"),
    })
    public ApiResponse<List<KeywordResponseDto.KeywordResultDto>> getUserKeywordList(@PathVariable(name = "userId")Long userId){
        User user = userService.findUser(userId);

        return ApiResponse.onSuccess(userService.getUserKeywordList(user));
    }
}
