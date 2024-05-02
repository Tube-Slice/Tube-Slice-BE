package TubeSlice.tubeSlice.domain.comment.dto.response;

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
}
