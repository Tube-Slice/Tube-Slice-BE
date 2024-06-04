package TubeSlice.tubeSlice.domain.script.dto.response;

import TubeSlice.tubeSlice.domain.scriptKeyword.ScriptKeyword;
import TubeSlice.tubeSlice.domain.text.dto.response.TextResponseDto;
import TubeSlice.tubeSlice.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScriptResponseDto {

    private String videoUrl;
    private String videoTitle;
    private User user;
    private List<TextResponseDto> userScriptList;
    private List<ScriptKeyword> keywords;
    private List<SubtitleResponseDto> subtitles;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubtitleResponseListDto {

        private List<SubtitleResponseDto> subtitles;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubtitleResponseDto {

        private Double timeline;
        private String sub;
    }
}
