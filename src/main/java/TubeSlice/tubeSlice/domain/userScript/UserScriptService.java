package TubeSlice.tubeSlice.domain.userScript;

import TubeSlice.tubeSlice.domain.script.Script;
import TubeSlice.tubeSlice.domain.scriptKeyword.ScriptKeyword;
import TubeSlice.tubeSlice.domain.scriptKeyword.ScriptKeywordRepository;
import TubeSlice.tubeSlice.domain.scriptKeyword.ScriptKeywordService;
import TubeSlice.tubeSlice.domain.text.Text;
import TubeSlice.tubeSlice.domain.text.TextConverter;
import TubeSlice.tubeSlice.domain.text.TextRepository;
import TubeSlice.tubeSlice.domain.text.TextService;
import TubeSlice.tubeSlice.domain.text.dto.response.TextResponseDto;
import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.domain.userScript.dto.request.UserScriptRequest;
import TubeSlice.tubeSlice.domain.userScript.dto.response.UserScriptResponse;
import TubeSlice.tubeSlice.global.response.code.resultCode.ErrorStatus;
import TubeSlice.tubeSlice.global.response.code.resultCode.SuccessStatus;
import TubeSlice.tubeSlice.global.response.exception.handler.UserScriptHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserScriptService {

    private final UserScriptRepository userScriptRepository;
    private final ScriptKeywordRepository scriptKeywordRepository;
    private final TextRepository textRepository;

    private final TextService textService;
    private final ScriptKeywordService scriptKeywordService;

    public UserScriptResponse.UserScriptResponseDto getScript(User user , Long userScriptId){
        UserScript userScript = userScriptRepository.findById(userScriptId).orElseThrow(() -> new UserScriptHandler(ErrorStatus.USER_SCRIPT_NOT_FOUND));

        if (userScript.getUser() != user){
            throw new UserScriptHandler(ErrorStatus.USER_SCRIPT_NOT_FOUND);
        }

        return UserScriptConverter.toUserScript(userScript);
    }

    public UserScriptResponse.UserScriptResponseListDto getScriptList(User user){

        List<UserScript> userScripts = userScriptRepository.findAllByUser(user);

        if (userScripts == null){
            throw new UserScriptHandler(ErrorStatus.USER_SCRIPT_NOT_FOUND2);
        }

        return UserScriptConverter.toUserScriptList(userScripts);
    }

    @Transactional
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

    @Transactional
    public SuccessStatus updateScript(User user, Long userScriptId, UserScriptRequest.UpdateRequestDto requestDto){

        UserScript userScript = userScriptRepository.findById(userScriptId).orElseThrow(() -> new UserScriptHandler(ErrorStatus.USER_SCRIPT_NOT_FOUND));

        if (userScript.getUser() != user){
            throw new UserScriptHandler(ErrorStatus.USER_SCRIPT_NOT_FOUND);
        }

        if (requestDto.getScript() != null) {
            for (UserScriptRequest.UpdateRequestDto.Script r : requestDto.getScript()) {
                Text text = textRepository.findAllByUserScriptAndTimeline(userScript, r.getTimeline());
                text.setText(r.getText());
                textRepository.save(text);
            }
        }

        if (requestDto.getScriptKeywords()!=null){
            scriptKeywordService.updateScriptKeyword(userScriptId, requestDto.getScriptKeywords(),userScript);
        }

        return SuccessStatus._OK;
    }

    @Transactional
    public SuccessStatus highlightScript(User user, Long userScriptId, List<Long> textId){

        UserScript userScript = userScriptRepository.findById(userScriptId).orElseThrow(() -> new UserScriptHandler(ErrorStatus.USER_SCRIPT_NOT_FOUND));

        if (userScript.getUser() != user){
            throw new UserScriptHandler(ErrorStatus.USER_SCRIPT_NOT_FOUND);
        }

        for (Long r : textId){
            Text text  = textRepository.findById(r).orElseThrow(()-> new UserScriptHandler(ErrorStatus.USER_SCRIPT_NOT_FOUND));
            text.setHighlight(true);
            textRepository.save(text);
        }

        return SuccessStatus._OK;
    }

    @Transactional
    public SuccessStatus deleteScript(User user, Long userScriptId){

        UserScript findScript = userScriptRepository.findById(userScriptId).orElseThrow(() -> new UserScriptHandler(ErrorStatus.USER_SCRIPT_NOT_FOUND));

        if (findScript.getUser() != user){
            throw new UserScriptHandler(ErrorStatus.USER_SCRIPT_NOT_FOUND);
        }

        userScriptRepository.delete(findScript);

        return SuccessStatus._OK;
    }

    public UserScriptResponse.UserScriptKeywordtListDto getScriptKeywordList(){

        //List<UserScript> userScriptList = userScriptRepository.findAllByUser(user);
        List<UserScript> userScriptList = userScriptRepository.findAllByUserId(1L);

        return UserScriptConverter.toUserScriptKeywordList(userScriptList);
    }

    public UserScriptResponse.UserScriptResponseListDto getScriptListByKeyword(User user, String keyword){

        List<ScriptKeyword> scriptKeywords = scriptKeywordRepository.findAllByKeywordName(keyword);

        if (scriptKeywords == null){
            throw new UserScriptHandler(ErrorStatus.USER_SCRIPT_NOT_FOUND2);
        }

        List<UserScript> userScriptList = scriptKeywords.stream()
                .map(ScriptKeyword::getUserScript)
                .collect(Collectors.toList());

        return UserScriptConverter.toUserScriptList(userScriptList);
    }
}
