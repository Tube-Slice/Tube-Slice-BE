package TubeSlice.tubeSlice.domain.post;

import TubeSlice.tubeSlice.domain.keyword.Keyword;
import TubeSlice.tubeSlice.domain.keyword.KeywordConverter;
import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import TubeSlice.tubeSlice.domain.post.dto.PostResponseDto;
import TubeSlice.tubeSlice.domain.postKeyword.PostKeyword;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public static List<PostResponseDto.PostInfoDto> toPostInfoDtoList(List<Post> postList){
        return postList.stream()
                .map(PostConverter::toPostInfoDto)
                .collect(Collectors.toList());

    }
}
