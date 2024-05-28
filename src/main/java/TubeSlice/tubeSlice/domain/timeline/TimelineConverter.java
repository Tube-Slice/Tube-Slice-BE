package TubeSlice.tubeSlice.domain.timeline;

import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.timeline.dto.TimelineRequestDto;

import java.util.List;

public class TimelineConverter {

    public static Timeline toTimeline(TimelineRequestDto.TimelineDto timelineDto, Post post){
        int startTime = timelineDto.getStartTime();
        int endTime = timelineDto.getEndTime();

        return Timeline.builder()
                .startTime(timelineDto.getStartTime())
                .endTime(timelineDto.getEndTime())
                .post(post)
                .build();
    }
}
