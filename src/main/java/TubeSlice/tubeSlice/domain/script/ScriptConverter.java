package TubeSlice.tubeSlice.domain.script;

import TubeSlice.tubeSlice.domain.script.dto.response.ScriptResponseDto;
import TubeSlice.tubeSlice.domain.text.dto.response.TextResponseDto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ScriptConverter {

    public static ScriptResponseDto.SubtitleResponseListDto toSubtitleListDto(Script script){
        List<ScriptResponseDto.SubtitleResponseDto> subs = script.getSubtitles().stream()
                .map(subtitle -> new ScriptResponseDto.SubtitleResponseDto(subtitle.getTimeline(), subtitle.getSubtitle()))
                .sorted(Comparator.comparing(ScriptResponseDto.SubtitleResponseDto::getTimeline))
                .collect(Collectors.toList());

        return new ScriptResponseDto.SubtitleResponseListDto(subs);
    }

}
