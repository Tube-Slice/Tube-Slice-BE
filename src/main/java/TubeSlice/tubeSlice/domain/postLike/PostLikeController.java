package TubeSlice.tubeSlice.domain.postLike;

import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.domain.user.UserService;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import TubeSlice.tubeSlice.global.response.code.resultCode.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/post-likes")
public class PostLikeController {

    private final PostLikeService postLikeService;
    private final UserService userService;

    @PostMapping("/posts/{postId}")
    @Operation(summary = "게시글 좋아요 API",description = "게시글에 좋아요 누르기")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "유저가 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST401", description = "게시글이 존재하지 않습니다",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POSTLIKE401", description = "좋아요한 기록이 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "postId", description = "좋아요 할 게시글의 id"),
    })
    public ApiResponse<SuccessStatus> createPostLike(@AuthenticationPrincipal UserDetails details,@PathVariable(name = "postId")Long postId){
        Long userId = userService.getUserId(details);

        return postLikeService.createPostLike(userId, postId);
    }

    @DeleteMapping("/posts/{postId}")
    @Operation(summary = "게시글 좋아요 취소하기 API",description = "게시글의 좋아요 취소하기")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "유저가 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST401", description = "게시글이 존재하지 않습니다",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "postId", description = "좋아요 할 게시글의 id"),
    })
    public ApiResponse<SuccessStatus> deletePostLike(@AuthenticationPrincipal UserDetails details,@PathVariable(name = "postId") Long postId){
        Long userId = userService.getUserId(details);

        return postLikeService.deletePostLike(userId, postId);
    }

}
