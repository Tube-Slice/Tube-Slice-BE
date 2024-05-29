package TubeSlice.tubeSlice.domain.timeline;

import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.timeline.dto.TimelineRequestDto.TimelineRequestDto;
import TubeSlice.tubeSlice.domain.timeline.dto.TimelineResponseDto.TimelineResponseDto;

import java.util.List;

public class TimelineConverter {

    public static Timeline toTimeline(TimelineRequestDto.TimelineDto timelineDto, Post post) {

        return Timeline.builder()
                .startTime(timelineDto.getStartTime())
                .endTime(timelineDto.getEndTime())
                .post(post)
                .build();
    }

    public static TimelineResponseDto.PostTimelineDto toPostTimelineDto(Timeline timeline) {
        return TimelineResponseDto.PostTimelineDto.builder()
                .timelineId(timeline.getId())
                .startTime(timeline.getStartTime())
                .endTime(timeline.getEndTime())
                .description(timeline.getDescription())
                .build();
    }

    public static TimelineResponseDto.PostTimelineDtoList toPostTImelineDtoList(List<Timeline> timelineList) {
        List<TimelineResponseDto.PostTimelineDto> postTimelineDtoList = timelineList.stream()
                .map(TimelineConverter::toPostTimelineDto)
                .toList();

        return TimelineResponseDto.PostTimelineDtoList.builder()
                .postTimelineDtoList(postTimelineDtoList)
                .build();
    }
}