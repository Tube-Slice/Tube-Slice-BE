package TubeSlice.tubeSlice.domain.timeline;

import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.timeline.dto.TimelineRequestDto;

import java.util.List;

public class TimelineConverter {

    public static Timeline toTimeline(TimelineRequestDto.TimelineDto timelineDto, Post post){
        int startTime = timelineDto.getStartTime();
        int endTime = timelineDto.getEndTime();

        int startHours = startTime / 3600;
        int startMinutes = (startTime % 3600) / 60;
        int startSeconds = startTime % 60;

        int endHours = endTime / 3600;
        int endMinutes = (endTime % 3600) / 60;
        int endSeconds  = endTime % 60;

        return Timeline.builder()
                .startHours(startHours)
                .startMinutes(startMinutes)
                .startSeconds(startSeconds)
                .endHours(endHours)
                .endMinutes(endMinutes)
                .endSeconds(endSeconds)
                .post(post)
                .build();
    }
}
