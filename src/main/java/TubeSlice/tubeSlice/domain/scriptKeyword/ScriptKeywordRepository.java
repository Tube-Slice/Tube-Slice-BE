package TubeSlice.tubeSlice.domain.scriptKeyword;

import TubeSlice.tubeSlice.domain.keyword.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScriptKeywordRepository extends JpaRepository<ScriptKeyword, Long> {


    ScriptKeyword findByKeywordAndUserScriptId(Keyword keyword, Long userScriptId);
    List<ScriptKeyword> findByUserScriptId(Long userScriptId);
}
