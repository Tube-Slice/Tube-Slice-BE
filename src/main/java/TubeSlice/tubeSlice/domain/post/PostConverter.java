package TubeSlice.tubeSlice.domain.post;

import TubeSlice.tubeSlice.domain.follow.FollowRepository;
import TubeSlice.tubeSlice.domain.keyword.KeywordConverter;
import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import TubeSlice.tubeSlice.domain.post.dto.response.PostResponseDto;
import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.domain.user.UserConverter;
import TubeSlice.tubeSlice.domain.user.dto.response.UserResponseDto;
import org.springframework.data.domain.Page;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PostConverter {


    public static String toCreatedFormat(LocalDateTime createdAt){
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(createdAt, now);

        long seconds = duration.getSeconds();
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if(minutes < 1) {
            return "방금 전";
        }else if(minutes < 60){
            return minutes + "분 전";
        }else if (hours < 24) {
            return "약 " + hours + "시간 전";
        }else if(days < 2){
            return "어제";
        }else if (days < 7){
            return days + "일 전";
        }else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
            return createdAt.format(formatter);
        }

    }

    public static PostResponseDto.PostInfoDto toPostInfoDto(Post post){
        String createdAt = toCreatedFormat(post.getCreatedAt());
        List<KeywordResponseDto.KeywordResultDto> keywordList = KeywordConverter.toPostKeywordDtoList(post);
        return PostResponseDto.PostInfoDto.builder()
                .title(post.getTitle())
                .postId(post.getId())
                .content(post.getContent())
                .keywords(keywordList)
                .videoUrl(post.getVideoUrl())
                .likeNum(post.getPostLikeList().size())
                .commentNum(post.getCommentList().size())
                .createdAt(createdAt)
                .build();
    }

    public static PostResponseDto.PostInfoListDto toPostInfoDtoList(Page<Post> postList){
        List<PostResponseDto.PostInfoDto> postInfoDtoList = new java.util.ArrayList<>(postList.stream()
                .map(PostConverter::toPostInfoDto)
                .toList());

        Collections.reverse(postInfoDtoList);

        return PostResponseDto.PostInfoListDto.builder()
                .isLast(postList.isLast())
                .isFirst(postList.isFirst())
                .totalElement(postList.getTotalElements())
                .totalPage(postList.getTotalPages())
                .listSize(postInfoDtoList.size())
                .posts(postInfoDtoList)
                .build();

    }

    public static PostResponseDto.SinglePostInfoDto toSinglePostDto(User user, Post post, Boolean isFollowing, Boolean isLike){
        boolean isMine = post.getUser() == user;

        User writer = post.getUser();

        UserResponseDto.PostUserInfo writerInfo = UserResponseDto.PostUserInfo.builder()
                .userId(writer.getId())
                .nickname(writer.getNickname())
                .profileUrl(writer.getProfileUrl())
                .isFollowing(isFollowing)
                .build();
        PostResponseDto.PostInfoDto postInfo = toPostInfoDto(post);

        String createdAt = toCreatedFormat(post.getCreatedAt());
        List<KeywordResponseDto.KeywordResultDto> keywordList = KeywordConverter.toPostKeywordDtoList(post);
        PostResponseDto.SinglePostUserInfoDto postInfoDto = PostResponseDto.SinglePostUserInfoDto.builder()
                .writer(writerInfo)
                .title(post.getTitle())
                .postId(post.getId())
                .content(post.getContent())
                .keywords(keywordList)
                .videoUrl(post.getVideoUrl())
                .likeNum(post.getPostLikeList().size())
                .commentNum(post.getCommentList().size())
                .createdAt(createdAt)
                .build();

        return PostResponseDto.SinglePostInfoDto.builder()
                .isLike(isLike)
                .isMine(isMine)
                .post(postInfoDto)
                .build();
    }

    public static PostResponseDto.BoardDto toBoardDto(Post post){
        String createdAt = toCreatedFormat(post.getCreatedAt());
        UserResponseDto.BoardUserDto user = UserConverter.toBoardUserDto(post.getUser());

        return PostResponseDto.BoardDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .commentNum(post.getCommentList().size())
                .likeNum(post.getPostLikeList().size())
                .createdAt(createdAt)
                .build();
    }

    public static List<PostResponseDto.BoardDto> toRecentBoardDtoList(List<Post> postList){

        return postList.stream()
                .map(PostConverter::toBoardDto)
                .toList();
    }
}
