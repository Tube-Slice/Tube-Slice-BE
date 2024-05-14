package TubeSlice.tubeSlice.domain.script;

import TubeSlice.tubeSlice.domain.script.dto.response.ScriptResponseDto;
import TubeSlice.tubeSlice.domain.subtitle.Subtitle;
import TubeSlice.tubeSlice.domain.subtitle.dto.response.SubtitleResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScriptService {

    private final ScriptRepository scriptRepository;

    public List<SubtitleResponseDto> getSubtitles(Long script_id){

        Script script = scriptRepository.findById(script_id).orElseThrow();
        ScriptResponseDto scriptResponseDto = new ScriptResponseDto(script);
        return scriptResponseDto.getSubtitles();
    }
}
