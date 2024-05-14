package TubeSlice.tubeSlice.domain.post.dto.response;

import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import TubeSlice.tubeSlice.domain.user.dto.response.UserResponseDto;
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

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SinglePostUserInfoDto{
        private UserResponseDto.PostUserInfo writer;
        private Long postId;
        private String title;
        private String content;
        private String videoUrl;
        private List<KeywordResponseDto.KeywordResultDto> keywords;
        private Integer likeNum;
        private Integer commentNum;
        private String createdAt;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SinglePostInfoDto{
        private Boolean isMine;
        private Boolean isLike;
        private SinglePostUserInfoDto post;
    }
}
