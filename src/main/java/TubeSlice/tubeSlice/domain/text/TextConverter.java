package TubeSlice.tubeSlice.domain.text;

import TubeSlice.tubeSlice.domain.script.Script;
import TubeSlice.tubeSlice.domain.userScript.UserScript;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextConverter {

    public static Text toText(Map.Entry<Double, String> e, UserScript userScript){

        return Text.builder()
                .timeline(e.getKey())
                .text(e.getValue())
                .highlight(false)
                .userScript(userScript)
                .build();
    }
}
