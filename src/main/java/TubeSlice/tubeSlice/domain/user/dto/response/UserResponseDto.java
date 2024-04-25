package TubeSlice.tubeSlice.domain.user.dto.response;

import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import TubeSlice.tubeSlice.domain.oauth.dto.response.LoginResponseDto;
import TubeSlice.tubeSlice.domain.post.dto.response.PostResponeDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserResponseDto {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfoDto{
        private Long userId;
        private String nickname;
        private String introduction;
        private boolean isFollowing;
        private String profileUrl;
        private Integer followingNum;
        private Integer followerNum;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageResponseDto{
        private UserInfoDto user;
        private List<String> keyword;
        private List<PostResponeDto.PostInfoDto> post;
    }

}
