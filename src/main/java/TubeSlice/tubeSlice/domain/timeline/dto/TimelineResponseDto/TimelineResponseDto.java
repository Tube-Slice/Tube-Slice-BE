package TubeSlice.tubeSlice.domain.timeline.dto.TimelineResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class TimelineResponseDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostTimelineDto{
        private Long timelineId;
        private Integer startTime;
        private Integer endTime;
        private String description;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostTimelineDtoList{
        private List<PostTimelineDto> postTimelineDtoList;
    }
}
