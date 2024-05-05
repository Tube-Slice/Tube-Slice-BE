package TubeSlice.tubeSlice.domain.user;

import TubeSlice.tubeSlice.domain.follow.Follow;
import TubeSlice.tubeSlice.domain.user.dto.response.UserResponseDto;

import java.util.List;

public class UserConverter {
    public static UserResponseDto.FollowInfoDto toFollowingInfoDto(List<Long> followingIdList, Follow follow){
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

    public static UserResponseDto.FollowListDto toFollowingListDto(List<Long> followingIdList, List<Follow> followingList
            , User user){
        List<UserResponseDto.FollowInfoDto> followInfoDtoList = followingList.stream()
                .map(follow -> toFollowingInfoDto(followingIdList, follow))
                .toList();

        return UserResponseDto.FollowListDto.builder()
                .users(followInfoDtoList)
                .followerNum(user.getFollowerList().size())
                .followingNum(user.getFollowingList().size())
                .build();
    }

    public static UserResponseDto.FollowInfoDto toFollowerInfoDto(List<Long> followingIdList, Follow follow){
        boolean isFollowing = followingIdList.contains(follow.getSender().getId());
        User user = follow.getSender();
        return UserResponseDto.FollowInfoDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileUrl(user.getProfileUrl())
                .introduction(user.getIntroduction())
                .isFollowing(isFollowing)
                .build();
    }

    public static UserResponseDto.FollowListDto toFollowerListDto(List<Long> followingIdList, List<Follow> followingList
            , User user){
        List<UserResponseDto.FollowInfoDto> followInfoDtoList = followingList.stream()
                .map(follow -> toFollowerInfoDto(followingIdList, follow))
                .toList();

        return UserResponseDto.FollowListDto.builder()
                .users(followInfoDtoList)
                .followerNum(user.getFollowerList().size())
                .followingNum(user.getFollowingList().size())
                .build();
    }

}
