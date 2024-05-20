package TubeSlice.tubeSlice.domain.text.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

public class TextRequestDto {

    private String youtubeUrl;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GptRequest {

        private String model;
        private List<Messages> messages;
        private HashMap<String, String> response_format;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Messages {
            private String role;
            private String content;
        }
    }

    @Builder
    @Getter
    public static class SummaryRequestDto {

        private String row;
        private String script;
    }
}
