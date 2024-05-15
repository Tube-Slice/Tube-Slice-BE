package TubeSlice.tubeSlice.domain.comment.dto.response;

import TubeSlice.tubeSlice.domain.user.dto.response.UserResponseDto;
import lombok.*;

public class CommentResponseDto {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentResultDto{
        private Long commentId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostCommentDto{
        private UserResponseDto.CommentUserDto user;
        private Long commentId;
        private String content;
        private String createdAt;
    }
}
