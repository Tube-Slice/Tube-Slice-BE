package TubeSlice.tubeSlice.domain.timeline;

import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.post.dto.request.PostRequestDto;
import TubeSlice.tubeSlice.domain.timeline.dto.TimelineRequestDto.TimelineRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimelineService {
    private final TimelineRepository timelineRepository;

    @Transactional
    public void savePostTimeline(PostRequestDto.PostCreateDto postCreateDto, Post post) {
        List<TimelineRequestDto.TimelineDto> timelineDtoList = postCreateDto.getTimelineDtoList();

        if (!timelineDtoList.isEmpty()) {
            List<Timeline> timelineList = timelineDtoList.stream()
                    .map(timelineDto -> {return timelineRepository.save(TimelineConverter.toTimeline(timelineDto, post));})
                    .toList();
        }


    }
}
