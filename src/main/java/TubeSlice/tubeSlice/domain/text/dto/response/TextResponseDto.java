package TubeSlice.tubeSlice.domain.text.dto.response;

import TubeSlice.tubeSlice.domain.scriptKeyword.ScriptKeyword;
import TubeSlice.tubeSlice.domain.subtitle.Subtitle;
import TubeSlice.tubeSlice.domain.text.Text;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextResponseDto {

    private Double timeline;
    private String scripts;

    public TextResponseDto(Text text){
        this.timeline = text.getTimeline();
        this.scripts = text.getScripts();
    }
}
