package TubeSlice.tubeSlice.domain.post;

import TubeSlice.tubeSlice.domain.comment.dto.response.CommentResponseDto;
import TubeSlice.tubeSlice.domain.post.dto.response.PostResponseDto;
import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.domain.user.UserService;
import TubeSlice.tubeSlice.global.response.ApiResponse;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/posts")
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 정보 가져오기 API",description = "SinglePostInfoDto 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<PostResponseDto.SinglePostInfoDto> getSinglePostInfo(@AuthenticationPrincipal UserDetails details, @PathVariable(name = "postId")Long postId){
        Post post = postService.findPost(postId);

        Long myId = userService.getUserId(details);
        User user = userService.findUser(myId);

        return ApiResponse.onSuccess(postService.getSinglePostInfo(user, post));
    }

    @GetMapping("/{postId}/comments")
    @Operation(summary = "게시글의 댓글 목록 가져오기 API",description = "PostCommentDto 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<List<CommentResponseDto.PostCommentDto>> getPostComment(@AuthenticationPrincipal UserDetails details, @PathVariable(name = "postId")Long postId){
        Post post = postService.findPost(postId);

        Long myId = userService.getUserId(details);
        User user = userService.findUser(myId);

        return ApiResponse.onSuccess(postService.getPostComment(user, post));
    }

    @GetMapping("/recent")
    @Operation(summary = "게시판 페이지 최신순 게시글 반환 API",description = "BoardDto의 배열을 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<List<PostResponseDto.BoardDto>> getRecentBoard(){
        return ApiResponse.onSuccess(postService.getRecentBoard());
    }

    @GetMapping("/popular")
    @Operation(summary = "게시판 페이지 좋아요순 게시글 반환 API",description = "BoardDto의 배열을 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<List<PostResponseDto.BoardDto>> getPopularBoard(){
        return ApiResponse.onSuccess(postService.getPopularBoard());
    }

    @GetMapping("/search")
    @Operation(summary = "키워드기반 게시판페이지 게시글 목록 가져오기 API",description = "BoardDto의 배열을 반환한다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST402", description = "type을 확인해주세요.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "type", description = "TITLE, CONTENT,BOTH"),
            @Parameter(name = "search", description = "검색어"),

    })
    public ApiResponse<List<PostResponseDto.BoardDto>> getSearchBoard(@RequestParam(name = "type") String type, @RequestParam(name = "search") String search){

        return ApiResponse.onSuccess(postService.getSearchBoard(type, search));
    }
}
