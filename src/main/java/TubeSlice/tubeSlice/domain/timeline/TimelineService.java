package TubeSlice.tubeSlice.domain.timeline;

import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.post.dto.request.PostRequestDto;
import TubeSlice.tubeSlice.domain.timeline.dto.TimelineRequestDto.TimelineRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    @Transactional
    public void updatePostTimeline(PostRequestDto.PostUpdateDto postUpdateDto, Post post) {
        List<TimelineRequestDto.TimelineDto> timelineDtoList = postUpdateDto.getTimelineDtoList();  //변경
        List<Timeline> findTimelines = timelineRepository.findAllByPost(post);  //기존


        if (timelineDtoList.size() > findTimelines.size()){ //추가
            setTimeline(findTimelines, timelineDtoList, findTimelines.size());
            for (int i = findTimelines.size(); i < timelineDtoList.size(); i++) {
                TimelineRequestDto.TimelineDto t = timelineDtoList.get(i);
                timelineRepository.save(TimelineConverter.toTimeline(t, post));
            }
        }
        if (timelineDtoList.size() < findTimelines.size()) {    //삭제
            setTimeline(findTimelines, timelineDtoList, timelineDtoList.size());
            for (int i = timelineDtoList.size(); i < findTimelines.size(); i++) {
                timelineRepository.delete(findTimelines.get(i));
            }
        }
        if (timelineDtoList.size() == findTimelines.size()){
            setTimeline(findTimelines, timelineDtoList, timelineDtoList.size());
        }
    }

    private void setTimeline(List<Timeline> findTimelines, List<TimelineRequestDto.TimelineDto> timelineDtoList, int size) {
        for (int i = 0; i< size; i++) {
            Timeline t = findTimelines.get(i);
            TimelineRequestDto.TimelineDto t2 = timelineDtoList.get(i);

            if (!t.getStartTime().equals(t2.getStartTime())
                    || !t.getEndTime().equals(t2.getEndTime())
                    || !t.getDescription().equals(t2.getDescription())) {
                t.setDescription(t2.getDescription());
                t.setStartTime(t2.getStartTime());
                t.setEndTime(t2.getEndTime());
                timelineRepository.save(t);
            }
        }
    }
}
