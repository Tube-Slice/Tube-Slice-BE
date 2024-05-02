package TubeSlice.tubeSlice.domain.comment;

import TubeSlice.tubeSlice.domain.comment.dto.request.CommentRequestDto;
import TubeSlice.tubeSlice.domain.comment.dto.response.CommentResponseDto;
import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import TubeSlice.tubeSlice.global.response.code.resultCode.ErrorStatus;
import TubeSlice.tubeSlice.global.response.code.resultCode.SuccessStatus;
import TubeSlice.tubeSlice.global.response.exception.handler.CommentHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDto.CommentResultDto createComment(CommentRequestDto.CommentCreateDto request, User user, Post post){
        String content = request.getContent();

        Comment comment = Comment.builder()
                .content(content)
                .user(user)
                .post(post)
                .build();

        commentRepository.save(comment);

        return CommentResponseDto.CommentResultDto.builder()
                .commentId(comment.getId())
                .build();
    }

    @Transactional
    public ApiResponse<SuccessStatus> deleteComment(Long commentId){
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));
        commentRepository.delete(comment);

        return ApiResponse.onSuccess(SuccessStatus._OK);
    }
}
