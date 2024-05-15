package TubeSlice.tubeSlice.domain.script;

import TubeSlice.tubeSlice.domain.script.dto.response.ScriptResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScriptService {

    private final ScriptRepository scriptRepository;

    public List<ScriptResponseDto.SubtitleResponseDto> getSubtitles(Long script_id){

        Script script = scriptRepository.findById(script_id).orElseThrow();

        return ScriptConverter.toSubtitleListDto(script);
    }
}
