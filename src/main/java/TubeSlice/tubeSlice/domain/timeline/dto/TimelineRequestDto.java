package TubeSlice.tubeSlice.domain.timeline.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TimelineRequestDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimelineDto{
        private Integer startTime;
        private Integer endTime;
        private String description;
    }
}
