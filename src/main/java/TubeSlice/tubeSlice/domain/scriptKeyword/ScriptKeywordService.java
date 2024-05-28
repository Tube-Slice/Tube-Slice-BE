package TubeSlice.tubeSlice.domain.scriptKeyword;

import TubeSlice.tubeSlice.domain.keyword.Keyword;
import TubeSlice.tubeSlice.domain.keyword.KeywordRepository;
import TubeSlice.tubeSlice.domain.userScript.UserScript;
import TubeSlice.tubeSlice.domain.userScript.dto.request.UserScriptRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScriptKeywordService {

    private final KeywordRepository keywordRepository;
    private final ScriptKeywordRepository scriptKeywordRepository;


    public void saveScriptKeyword(UserScriptRequest.SaveRequestDto requestDto, UserScript userScript) {
        for (String pk: requestDto.getScriptKeywords()){
            Optional<Keyword> findKeyword = keywordRepository.findByName(pk);
            if (findKeyword.isEmpty()) {
                Keyword keyword = Keyword.builder()
                        .name(pk)
                        .build();
                keywordRepository.save(keyword);
                ScriptKeyword scriptKeyword = ScriptKeyword.builder()
                        .keyword(keyword)
                        .userScript(userScript)
                        .build();
                scriptKeywordRepository.save(scriptKeyword);
            }else { //keyword 테이블에 이미 있는 키워드 저장.
                ScriptKeyword scriptKeyword = ScriptKeyword.builder()
                        .keyword(findKeyword.get())
                        .userScript(userScript)
                        .build();
                scriptKeywordRepository.save(scriptKeyword);
            }
        }
    }

}
