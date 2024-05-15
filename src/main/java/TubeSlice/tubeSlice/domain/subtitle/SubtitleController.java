package TubeSlice.tubeSlice.domain.subtitle;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/subtitles")
@RequiredArgsConstructor
public class SubtitleController {

    private final SubtitleService subtitleService;


}
