package TubeSlice.tubeSlice.domain.text.dto.response;

import TubeSlice.tubeSlice.domain.text.dto.request.TextRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


public class TextResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class transResponseListDto{
        List<transResponseDto> scripts;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class transResponseDto{
        private Double timeline;
        private String text;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClovaSpeechResponseDto {

        private String token;
        private String result;
        private String message;
    }

    @Getter
    public static class GptResponse {

        private List<Choice> choices;

        @Getter
        public static class Choice {
            private int index;
            private TextRequestDto.GptRequest.Messages message;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SummaryResponseListDto{
        private List<SummaryResponseDto> summaries;
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SummaryResponseDto{
        private Integer id;
        private String message;
    }
}
