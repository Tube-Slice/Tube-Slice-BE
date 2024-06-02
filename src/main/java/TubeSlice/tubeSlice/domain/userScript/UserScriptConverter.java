package TubeSlice.tubeSlice.domain.userScript;

import TubeSlice.tubeSlice.domain.script.Script;
import TubeSlice.tubeSlice.domain.scriptKeyword.ScriptKeyword;
import TubeSlice.tubeSlice.domain.subtitle.Subtitle;
import TubeSlice.tubeSlice.domain.text.Text;
import TubeSlice.tubeSlice.domain.userScript.dto.response.UserScriptResponse;

import java.util.List;
import java.util.stream.Collectors;

public class UserScriptConverter {

    public static UserScriptResponse.UserScriptResponseDto toUserScript(UserScript userScript){
        Script script = userScript.getScript();
        List<UserScriptResponse.Script> texts = userScript.getScriptTexts().stream()
                .map(t -> new UserScriptResponse.Script(t.getTimeline(), t.getText(), t.getHighlight()))
                .collect(Collectors.toList());
        List<String> scriptKeywords = userScript.getScriptKeywords().stream()
                .map(scriptKeyword -> scriptKeyword.getKeyword().getName())
                .toList();
        List<String> subtitiles = script.getSubtitles().stream()
                .map(Subtitle::getSubtitle)
                .toList();

        return UserScriptResponse.UserScriptResponseDto.builder()
                .userScriptId(userScript.getId())
                .youtubeTitle(script.getVideoTitle().replace(".mp3",""))
                .youtubeUrl(script.getVideoUrl())
                .scriptId(script.getId())
                .scripts(texts)
                .scriptKeywords(scriptKeywords)
                .subtitles(subtitiles)
                .build();
    }
}
