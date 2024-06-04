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

    public static UserResponseDto.MypageUserInfoDto toMypageUserInfoDto(User user, boolean isFollowing){
        return UserResponseDto.MypageUserInfoDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileUrl(user.getProfileUrl())
                .introduction(user.getIntroduction())
                .followerNum(user.getFollowerList().size())
                .followingNum(user.getFollowingList().size())
                .isFollowing(isFollowing)
                .build();
    }

    public static UserResponseDto.CommentUserDto toCommentUserDto(User user, boolean isMine){
        return UserResponseDto.CommentUserDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileUrl(user.getProfileUrl())
                .isMine(isMine)
                .build();
    }

    public static UserResponseDto.BoardUserDto toBoardUserDto(User user){
        return UserResponseDto.BoardUserDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileUrl(user.getProfileUrl())
                .build();
    }

    public static UserResponseDto.UserSettingInfoDto toUserSettingInfoDto(User user){
        return UserResponseDto.UserSettingInfoDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileUrl(user.getProfileUrl())
                .introduction(user.getIntroduction())
                .build();
    }
}
