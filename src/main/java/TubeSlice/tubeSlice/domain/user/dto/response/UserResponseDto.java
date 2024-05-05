package TubeSlice.tubeSlice.domain.user.dto.response;

import TubeSlice.tubeSlice.domain.oauth.dto.response.LoginResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowInfoDto{
        private Long userId;
        private String nickname;
        private String profileUrl;
        private String introduction;
        private boolean isFollowing;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowListDto{
        private List<FollowInfoDto> users;
        private Integer followingNum;
        private Integer followerNum;
    }


}
