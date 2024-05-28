package TubeSlice.tubeSlice.domain.userScript;

import TubeSlice.tubeSlice.domain.script.Script;
import TubeSlice.tubeSlice.domain.scriptKeyword.ScriptKeyword;
import TubeSlice.tubeSlice.domain.scriptKeyword.ScriptKeywordService;
import TubeSlice.tubeSlice.domain.subtitle.Subtitle;
import TubeSlice.tubeSlice.domain.subtitle.SubtitleRepository;
import TubeSlice.tubeSlice.domain.text.Text;
import TubeSlice.tubeSlice.domain.text.TextConverter;
import TubeSlice.tubeSlice.domain.text.TextRepository;
import TubeSlice.tubeSlice.domain.text.TextService;
import TubeSlice.tubeSlice.domain.text.dto.request.TextRequestDto;
import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.domain.userScript.dto.request.UserScriptRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserScriptService {

    private final UserScriptRepository userScriptRepository;
    private final TextRepository textRepository;
    private final ScriptKeywordService scriptKeywordService;

    private final TextService textService;

    public Long saveScript(User user, Script script, UserScriptRequest.SaveRequestDto requestDto){

        List<Map.Entry<Double, String>> scripts = textService.getScriptFromBucket(script);

        UserScript userScript = UserScript.builder()
                .script(script)
                .user(user)
                .build();
        userScriptRepository.save(userScript);

        //text 저장
        for (Map.Entry<Double, String> e : scripts) {

            Text text = TextConverter.toText(e, userScript);
            textRepository.save(text);
        }

        scriptKeywordService.saveScriptKeyword(requestDto, userScript);

        return userScript.getId();
    }
}
