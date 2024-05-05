package TubeSlice.tubeSlice.domain.user;

import TubeSlice.tubeSlice.domain.follow.Follow;
import TubeSlice.tubeSlice.domain.user.dto.response.UserResponseDto;

import java.util.List;

public class UserConverter {
    public static UserResponseDto.FollowInfoDto toFollowInfoDto(List<Long> followingIdList, Follow follow){
        boolean isFollowing = followingIdList.contains(follow.getReceiver().getId());
        User user = follow.getReceiver();
        return UserResponseDto.FollowInfoDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileUrl(user.getProfileUrl())
                .introduction(user.getIntroduction())
                .isFollowing(isFollowing)
                .build();
    }

    public static UserResponseDto.FollowListDto toFollowListDto(List<Long> followingIdList, List<Follow> followingList
            , User user){
        List<UserResponseDto.FollowInfoDto> followInfoDtoList = followingList.stream()
                .map(follow -> toFollowInfoDto(followingIdList, follow))
                .toList();

        return UserResponseDto.FollowListDto.builder()
                .users(followInfoDtoList)
                .followerNum(user.getFollowerList().size())
                .followingNum(user.getFollowingList().size())
                .build();
    }

    public static UserResponseDto.MypageUserInfoDto toMypageUserInfoDto(User user){
        return UserResponseDto.MypageUserInfoDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileUrl(user.getProfileUrl())
                .introduction(user.getIntroduction())
                .followerNum(user.getFollowerList().size())
                .followingNum(user.getFollowingList().size())
                .build();
    }
}
