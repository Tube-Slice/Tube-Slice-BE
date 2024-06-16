package TubeSlice.tubeSlice.domain.userScript;

import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import TubeSlice.tubeSlice.domain.script.Script;
import TubeSlice.tubeSlice.domain.script.dto.response.ScriptResponseDto;
import TubeSlice.tubeSlice.domain.scriptKeyword.ScriptKeyword;
import TubeSlice.tubeSlice.domain.subtitle.Subtitle;
import TubeSlice.tubeSlice.domain.text.Text;
import TubeSlice.tubeSlice.domain.userScript.dto.response.UserScriptResponse;
import TubeSlice.tubeSlice.global.entity.BaseEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserScriptConverter {

    public static UserScriptResponse.UserScriptKeywordtListDto toUserScriptKeywordList(List<UserScript> userScriptList){
        List<UserScriptResponse.UserScriptKeywordtDto> scriptKeywordtDtos = new ArrayList<>();

        for (UserScript us : userScriptList){
            for (ScriptKeyword keyword : us.getScriptKeywords()){
                scriptKeywordtDtos.add(new UserScriptResponse.UserScriptKeywordtDto(keyword.getKeyword().getName()));
            }
        }

        scriptKeywordtDtos = scriptKeywordtDtos.stream().sorted(Comparator.comparing(UserScriptResponse.UserScriptKeywordtDto::getKeyword)).collect(Collectors.toList());

        return  UserScriptResponse.UserScriptKeywordtListDto.builder()
                .script_keywords(scriptKeywordtDtos)
                .build();
    }

    public static UserScriptResponse.UserScriptResponseListDto toUserScriptList(List<UserScript> userScriptList){

        List<UserScriptResponse.UserScriptResponseDto> userScriptResponseDtos = userScriptList.stream()
                .map(UserScriptConverter::toUserScript)
                .sorted(Comparator.comparing(UserScriptResponse.UserScriptResponseDto::getUpdateAt).reversed())
                .toList();

        return UserScriptResponse.UserScriptResponseListDto.builder()
                .scriptList(userScriptResponseDtos)
                .build();
    }

    public static UserScriptResponse.UserScriptResponseDto toUserScript(UserScript userScript){
        Script script = userScript.getScript();
        List<UserScriptResponse.Script> texts = userScript.getScriptTexts().stream()
                .map(t -> new UserScriptResponse.Script(t.getId(), t.getTimeline(), t.getText(), t.getHighlight()))
                .collect(Collectors.toList());
        List<KeywordResponseDto.KeywordResultDto> scriptKeywords = userScript.getScriptKeywords().stream()
                .map(scriptKeyword -> new KeywordResponseDto.KeywordResultDto(scriptKeyword.getId(), scriptKeyword.getKeyword().getName()))
                .toList();
        List<ScriptResponseDto.SubtitleResponseDto> subtitiles = script.getSubtitles().stream()
                .map(s -> new ScriptResponseDto.SubtitleResponseDto(s.getTimeline(), s.getSubtitle()))
                .sorted(Comparator.comparing(ScriptResponseDto.SubtitleResponseDto::getTimeline))
                .toList();
        String updateAt = userScript.getScriptTexts().stream()
                .max(Comparator.comparing(BaseEntity::getUpdatedAt))
                .stream().findFirst().orElseThrow(null)
                .getUpdatedAt().toString();

        return UserScriptResponse.UserScriptResponseDto.builder()
                .userScriptId(userScript.getId())
                .youtubeTitle(script.getVideoTitle().replace(".mp3",""))
                .youtubeUrl(script.getVideoUrl())
                .scriptId(script.getId())
                .scripts(texts)
                .scriptKeywords(scriptKeywords)
                .subtitles(subtitiles)
                .updateAt(updateAt)
                .build();
    }
}
