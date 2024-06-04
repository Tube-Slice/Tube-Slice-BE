package TubeSlice.tubeSlice.domain.scriptKeyword;

import TubeSlice.tubeSlice.domain.keyword.Keyword;
import TubeSlice.tubeSlice.domain.keyword.KeywordRepository;
import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.postKeyword.PostKeyword;
import TubeSlice.tubeSlice.domain.userScript.UserScript;
import TubeSlice.tubeSlice.domain.userScript.dto.request.UserScriptRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void updateScriptKeyword(Long userScriptId, List<String> updateScriptKeywords, UserScript findUserScript) {
        for (String pk : updateScriptKeywords) {
            Optional<Keyword> findKeyword = keywordRepository.findByName(pk);
            Keyword keyword;
            ScriptKeyword findScriptKeyword = scriptKeywordRepository.findByKeywordAndUserScriptId(findKeyword.orElse(null), userScriptId);

            if (findKeyword.isEmpty()){ //keyword 테이블에 없으면 keyword랑 post_keyword에 둘다 저장 필요.
                keyword = Keyword.builder()
                        .name(pk)
                        .build();
                keywordRepository.save(keyword);
                ScriptKeyword scriptKeyword = ScriptKeyword.builder()
                        .keyword(keyword)
                        .userScript(findUserScript)
                        .build();

                scriptKeywordRepository.save(scriptKeyword);
            } else if (findScriptKeyword == null){    //keyword에는 있는데 post_keyword에 없는 경우.

                ScriptKeyword scriptKeyword = ScriptKeyword.builder()
                        .keyword(findKeyword.get())
                        .userScript(findUserScript)
                        .build();

                scriptKeywordRepository.save(scriptKeyword);
            }
        }

        //기존 postKeyword 와 비교해서 삭제된 keyword를 db에서 삭제.
        List<ScriptKeyword> findScriptKeywords = scriptKeywordRepository.findByUserScriptId(userScriptId);
        for (ScriptKeyword scriptKeyword: findScriptKeywords){
            if (!updateScriptKeywords.contains(scriptKeyword.getKeyword().getName())){
                scriptKeywordRepository.delete(scriptKeyword);
            }
        }
    }

}
