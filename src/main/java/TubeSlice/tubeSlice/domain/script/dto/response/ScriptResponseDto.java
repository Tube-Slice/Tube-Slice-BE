package TubeSlice.tubeSlice.domain.script.dto.response;

import TubeSlice.tubeSlice.domain.script.Script;
import TubeSlice.tubeSlice.domain.scriptKeyword.ScriptKeyword;
import TubeSlice.tubeSlice.domain.subtitle.dto.response.SubtitleResponseDto;
import TubeSlice.tubeSlice.domain.text.dto.response.TextResponseDto;
import TubeSlice.tubeSlice.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public ScriptResponseDto(Script script){
        this.videoUrl = script.getVideoUrl();
        this.videoTitle = script.getVideoTitle();
        this.user = script.getUser();
        this.userScriptList = script.getUserScriptList().stream()
                .map(text -> new TextResponseDto(text))
                .collect(Collectors.toList());
        this.keywords = script.getKeywords();
        this.subtitles = script.getSubtitles().stream()
                .map(subtitle -> new SubtitleResponseDto(subtitle))
                .sorted(Comparator.comparing(SubtitleResponseDto::getTimeline))
                .collect(Collectors.toList());
    }

}
