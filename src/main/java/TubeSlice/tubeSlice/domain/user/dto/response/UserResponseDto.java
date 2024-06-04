package TubeSlice.tubeSlice.domain.user.dto.response;

import TubeSlice.tubeSlice.domain.image.dto.response.ImageResponseDto;
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
        private Boolean isFollowing;
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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MypageUserInfoDto{
        private Long userId;
        private String nickname;
        private String profileUrl;
        private String introduction;
        private Integer followingNum;
        private Integer followerNum;
        private Boolean isFollowing;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostUserInfo{
        private Long userId;
        private String nickname;
        private String profileUrl;
        private Boolean isFollowing;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentUserDto{
        private Long userId;
        private String nickname;
        private String profileUrl;
        private Boolean isMine;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardUserDto{
        private Long userId;
        private String nickname;
        private String profileUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSettingInfoDto{
        private Long userId;
        private String nickname;
        private String profileUrl;
        private String introduction;
    }



}
