package TubeSlice.tubeSlice.domain.userScript.dto.response;

import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import TubeSlice.tubeSlice.domain.script.dto.response.ScriptResponseDto;
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
        private String updateAt;
        private List<ScriptResponseDto.SubtitleResponseDto> subtitles;
        private Long scriptId;
        private List<Script> scripts;
        private List<KeywordResponseDto.KeywordResultDto> scriptKeywords;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Script{

        private Long textId;
        private Double timeline;
        private String text;
        private Boolean isHighlighted;

    }
}
