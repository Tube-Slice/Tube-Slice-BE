package TubeSlice.tubeSlice.domain.post.dto;

import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import lombok.*;

import java.util.List;

public class PostResponseDto {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostInfoDto{
        private Long postId;
        private String title;
        private String content;
        private String videoUrl;
        private List<KeywordResponseDto.KeywordResultDto> keywords;
        private Integer likeNum;
        private Integer commentNum;
        private String createdAt;
    }
}
