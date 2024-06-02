package TubeSlice.tubeSlice.domain.userScript;

import TubeSlice.tubeSlice.domain.script.Script;
import TubeSlice.tubeSlice.domain.script.ScriptRepository;
import TubeSlice.tubeSlice.domain.scriptKeyword.ScriptKeyword;
import TubeSlice.tubeSlice.domain.scriptKeyword.ScriptKeywordService;
import TubeSlice.tubeSlice.domain.subtitle.Subtitle;
import TubeSlice.tubeSlice.domain.subtitle.SubtitleRepository;
import TubeSlice.tubeSlice.domain.text.Text;
import TubeSlice.tubeSlice.domain.text.TextConverter;
import TubeSlice.tubeSlice.domain.text.TextRepository;
import TubeSlice.tubeSlice.domain.text.TextService;
import TubeSlice.tubeSlice.domain.text.dto.request.TextRequestDto;
import TubeSlice.tubeSlice.domain.text.dto.response.TextResponseDto;
import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.domain.userScript.dto.request.UserScriptRequest;
import TubeSlice.tubeSlice.global.response.code.resultCode.ErrorStatus;
import TubeSlice.tubeSlice.global.response.code.resultCode.SuccessStatus;
import TubeSlice.tubeSlice.global.response.exception.handler.UserScriptHandler;
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
    private final ScriptRepository scriptRepository;

    private final TextService textService;
    private final ScriptKeywordService scriptKeywordService;

    public Long saveScript(User user, Script script, UserScriptRequest.SaveRequestDto requestDto){

        List<TextResponseDto.transResponseDto> scripts = textService.getScriptFromBucket(script);

        UserScript userScript = UserScript.builder()
                .script(script)
                .user(user)
                .build();
        userScriptRepository.save(userScript);

        //text 저장
        for (TextResponseDto.transResponseDto e : scripts) {

            Text text = TextConverter.toText(e, userScript);
            textRepository.save(text);
        }

        scriptKeywordService.saveScriptKeyword(requestDto, userScript);

        return userScript.getId();
    }

    public SuccessStatus updateScript(User user, Long userScriptId, List<UserScriptRequest.UpdateRequestDto> requestDto){

        UserScript userScript = userScriptRepository.findById(userScriptId).orElseThrow(() -> new UserScriptHandler(ErrorStatus.USER_SCRIPT_NOT_FOUND));

        if (userScript.getUser() != user){
            throw new UserScriptHandler(ErrorStatus.USER_SCRIPT_NOT_FOUND);
        }

        for (UserScriptRequest.UpdateRequestDto r : requestDto){
            Text text  = textRepository.findAllByUserScriptAndTimeline(userScript, r.getTimeline());
            text.setText(r.getText());
            textRepository.save(text);
        }

        return SuccessStatus._OK;
    }

    public SuccessStatus highlightScript(User user, Long userScriptId, List<UserScriptRequest.highlightRequestDto> requestListDto){

        UserScript userScript = userScriptRepository.findById(userScriptId).orElseThrow(() -> new UserScriptHandler(ErrorStatus.USER_SCRIPT_NOT_FOUND));

        if (userScript.getUser() != user){
            throw new UserScriptHandler(ErrorStatus.USER_SCRIPT_NOT_FOUND);
        }

        for (UserScriptRequest.highlightRequestDto r : requestListDto){
            Text text  = textRepository.findAllByUserScriptAndTimeline(userScript, r.getTimeline());
            text.setHighlight(true);
            textRepository.save(text);
        }

        return SuccessStatus._OK;
    }
}
