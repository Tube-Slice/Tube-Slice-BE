package TubeSlice.tubeSlice.domain.post.dto.response;

import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import TubeSlice.tubeSlice.domain.user.dto.response.UserResponseDto;
import lombok.*;

import java.util.List;

public class PostResponseDto {
    @Builder
    @Getter
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostInfoListDto{
        private List<PostInfoDto> posts;
        private Integer listSize;
        private Integer totalPage;
        private Long totalElement;
        private Boolean isFirst;
        private Boolean isLast;
    }

    @Builder
    @Getter
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SinglePostInfoDto{
        private Boolean isMine;
        private Boolean isLike;
        private SinglePostUserInfoDto post;
    }

}
