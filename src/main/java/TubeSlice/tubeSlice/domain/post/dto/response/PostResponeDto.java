package TubeSlice.tubeSlice.domain.post.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponeDto {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostInfoDto{
        private Long postId;
        private String title;
        private String content;
        private List<String> keyword;
        private String videoUrl;
        private Integer commentNum;
        private Integer likeNum;
        private LocalDateTime createdAt;
    }
}
