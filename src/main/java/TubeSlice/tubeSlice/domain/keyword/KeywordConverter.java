package TubeSlice.tubeSlice.domain.keyword;

import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.postKeyword.PostKeyword;

import java.util.List;
import java.util.stream.Collectors;

public class KeywordConverter {

    public static KeywordResponseDto.KeywordResultDto toKeywordResultDto(Keyword keyword){
        return KeywordResponseDto.KeywordResultDto.builder()
                .keywordId(keyword.getId())
                .name(keyword.getName())
                .build();
    }
    public static List<KeywordResponseDto.KeywordResultDto> toKeywordDtoList(List<Post> postList){
        List<List<PostKeyword>> postKeyword = postList.stream()
                .map(Post::getPostKeywordList)
                .toList();

        return postKeyword.stream()
                .flatMap(List::stream)
                .map(PostKeyword::getKeyword)
                .distinct()
                .map(KeywordConverter::toKeywordResultDto)
                .toList();
    }

    public static List<KeywordResponseDto.KeywordResultDto> toPostKeywordDtoList(Post post){
        return post.getPostKeywordList().stream()
                .map(PostKeyword::getKeyword)
                .map(KeywordConverter::toKeywordResultDto)
                .toList();
    }
}
