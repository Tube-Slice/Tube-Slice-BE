package TubeSlice.tubeSlice.domain.post;

import TubeSlice.tubeSlice.domain.keyword.Keyword;
import TubeSlice.tubeSlice.domain.post.dto.PostResponseDto;
import TubeSlice.tubeSlice.domain.postKeyword.PostKeyword;

import java.util.List;
import java.util.stream.Collectors;

public class PostConverter {

    public static PostResponseDto.PostInfoDto toPostInfoDto(Post post){
        return PostResponseDto.PostInfoDto.builder()
                .title(post.getTitle())
                .postId(post.getId())
                .content(post.getContent())
                .keywords(post.getPostKeywordList().stream().map(PostKeyword::getKeyword).map(Keyword::getName).toList())
                .videoUrl(post.getVideoUrl())
                .likeNum(post.getPostLikeList().size())
                .commentNum(post.getCommentList().size())
                .build();
    }

    public static List<PostResponseDto.PostInfoDto> toPostInfoDtoList(List<Post> postList){
        return postList.stream()
                .map(PostConverter::toPostInfoDto)
                .collect(Collectors.toList());

    }
}
