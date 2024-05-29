package TubeSlice.tubeSlice.domain.timeline.dto.TimelineRequestDto;

import lombok.Builder;
import lombok.Getter;

public class TimelineRequestDto {

    @Getter
    public static class TimelineDto{
        private Integer startTime;
        private Integer endTime;
        private String description;
    }
}
