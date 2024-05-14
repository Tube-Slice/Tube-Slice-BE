package TubeSlice.tubeSlice.domain.subtitle.dto.response;

import TubeSlice.tubeSlice.domain.subtitle.Subtitle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubtitleResponseDto {

    private Double timeline;
    private String subtitles;

    public SubtitleResponseDto(Subtitle subtitle){
        this.timeline = subtitle.getTimeline();
        this.subtitles = subtitle.getSubtitle();
    }

}
