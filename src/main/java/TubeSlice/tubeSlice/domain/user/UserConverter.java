package TubeSlice.tubeSlice.domain.user;

import TubeSlice.tubeSlice.domain.keyword.Keyword;
import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.post.dto.response.PostResponeDto;
import TubeSlice.tubeSlice.domain.postKeyword.PostKeyword;
import TubeSlice.tubeSlice.domain.user.dto.response.UserResponseDto;

import java.util.List;
import java.util.stream.Collectors;


public class UserConverter {

    public static UserResponseDto.UserInfoDto toUserInfoDto(User me, User user, boolean following){


        return UserResponseDto.UserInfoDto.builder()
                .userId(me.getId())
                .nickname(me.getNickname())
                .profileUrl(me.getProfileUrl())
                .introduction(me.getIntroduction())
                .isFollowing(following)
                .follower(me.getFollowingList().size())
                .following(me.getFollowingList().size())
                .build();
    }

    public static List<PostResponeDto.PostInfoDto> toPostInfoDtoList (List<Post> postList){
        return postList.stream()
                .map(UserConverter::toPostInfoDto).collect(Collectors.toList());
    }

    public static List<String> toKeywordList(List<PostKeyword> keywordList){
        List<Keyword> postKeywrodList =  keywordList.stream()
                .map(PostKeyword::getKeyword)
                .toList();

        return postKeywrodList.stream()
                .map(Keyword::getName)
                .collect(Collectors.toList());
    }

    public static PostResponeDto.PostInfoDto toPostInfoDto(Post post){
        return PostResponeDto.PostInfoDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .videoUrl(post.getVideoUrl())
                .keyword(toKeywordList(post.getPostKeywordList()))
                .build();

    }

    public static List<String> toUserKeyword(User user){
        List<Post> postList = user.getPostList();
        List<List<PostKeyword>> list = postList.stream()
                .map(Post::getPostKeywordList)
                .toList();

        List<String> keyword = list.stream()
                .flatMap(List::stream)
                .map(PostKeyword::getKeyword)
                .map(Keyword::getName)
                .distinct()
                .toList();

        return keyword;
    }
}
