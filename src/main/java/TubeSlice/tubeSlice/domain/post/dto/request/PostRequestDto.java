package TubeSlice.tubeSlice.domain.post.dto.request;

import TubeSlice.tubeSlice.domain.timeline.dto.TimelineRequestDto.TimelineRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


public class PostRequestDto {


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostCreateDto{
        private String title;
        private String content;
        private String youtubeUrl;
        private List<String> postKeywords;
        private List<TimelineRequestDto.TimelineDto> timelineDtoList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostUpdateDto{
        private String title;
        private String content;
        private String youtubeUrl;
        private List<String> postKeywords;
        private List<TimelineRequestDto.TimelineDto> timelineDtoList;
    }
}
