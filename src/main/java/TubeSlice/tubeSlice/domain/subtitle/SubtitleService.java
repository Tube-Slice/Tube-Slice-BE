package TubeSlice.tubeSlice.domain.subtitle;

import TubeSlice.tubeSlice.domain.subtitle.dto.response.SubtitleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubtitleService {

    private final SubtitleRepository subtitleRepository;


}
