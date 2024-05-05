package TubeSlice.tubeSlice.domain.comment;

import TubeSlice.tubeSlice.domain.comment.dto.request.CommentRequestDto;
import TubeSlice.tubeSlice.domain.comment.dto.response.CommentResponseDto;
import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.post.PostService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/comments")
public class CommentController {
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    @PostMapping("/posts/{postId}")
    @Operation(summary = "댓글 작성하기 API",description = "post의 id를 받아 댓글을 생성한 후 comment의 id(CommentResultDto)를 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "댓글을 작성할 유저가 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST401", description = "댓글을 작성할 게시글이 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "postId", description = "댓글을 작성할 post의 id"),
    })
    public ApiResponse<CommentResponseDto.CommentResultDto> createComment(@PathVariable(name = "postId")Long postId, @RequestBody CommentRequestDto.CommentCreateDto request){
        User user = userService.findUser(1L);
        Post post = postService.findPost(postId);

        return ApiResponse.onSuccess(commentService.createComment(request, user, post));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제하기 API",description = "삭제할 comment의 id를 받아 삭제")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT401", description = "삭제할 댓글이 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "commentId", description = "삭제할 댓글의 id"),
    })
    public ApiResponse<SuccessStatus> deleteComment(@PathVariable(name = "commentId")Long commentId){
        return commentService.deleteComment(commentId);
    }

    @PatchMapping("/{commentId}")
    @Operation(summary = "댓글 수정하기 API",description = "수정할 comment의 id를 받아 삭제")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT401", description = "수정할 댓글이 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT402", description = "수정할 권한이 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<SuccessStatus> updateComment(@RequestBody CommentRequestDto.CommentPatchDto request, @PathVariable(name = "commentId")Long commentId) {
        User user = userService.findUser(1L); // 작성자 확인용 수정예정
        return commentService.updateComment(request, commentId, user);
    }

}
