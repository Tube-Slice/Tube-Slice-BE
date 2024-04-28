package TubeSlice.tubeSlice.domain.keyword.dto.response;

import lombok.*;

public class KeywordResponseDto {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeywordResultDto{
        private Long keywordId;
        private String name;
    }
}
