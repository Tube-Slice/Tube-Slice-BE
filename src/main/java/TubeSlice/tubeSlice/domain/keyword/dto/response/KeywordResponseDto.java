package TubeSlice.tubeSlice.domain.keyword.dto.response;

import lombok.*;

import java.util.List;

public class KeywordResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeywordResultDto{
        private Long keywordId;
        private String name;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeywordDtoList{
        private List<KeywordResultDto> keywords;
    }
}
