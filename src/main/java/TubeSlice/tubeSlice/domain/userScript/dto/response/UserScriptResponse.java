package TubeSlice.tubeSlice.domain.userScript.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class UserScriptResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserScriptResponseListDto{
        private List<UserScriptResponseDto> scriptList;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserScriptResponseDto{
        private Long userScriptId;
        private String youtubeUrl;
        private String youtubeTitle;
        private List<String> subtitles;
        private Long scriptId;
        private List<Script> scripts;
        private List<String> scriptKeywords;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Script{

        private Double timeline;
        private String text;
        Boolean isHighlighted;

    }
}
