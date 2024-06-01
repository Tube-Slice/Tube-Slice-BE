package TubeSlice.tubeSlice.domain.text;

import TubeSlice.tubeSlice.domain.script.Script;
import TubeSlice.tubeSlice.domain.text.dto.response.TextResponseDto;
import TubeSlice.tubeSlice.domain.userScript.UserScript;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextConverter {

    public static Text toText(TextResponseDto.transResponseDto e, UserScript userScript){

        return Text.builder()
                .timeline(e.getTimeline())
                .text(e.getText())
                .highlight(false)
                .userScript(userScript)
                .build();
    }
}
