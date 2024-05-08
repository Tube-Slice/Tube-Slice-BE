package TubeSlice.tubeSlice.domain.video.dto.response;

import TubeSlice.tubeSlice.domain.subtitle.Subtitle;
import TubeSlice.tubeSlice.domain.videoKeyword.VideoKeyword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VideoResponseDto {

    private String title;
    private String content;
    private String url;

    private List<VideoKeyword> videoKeywords;
    private List<Subtitle> subtitles;

}
