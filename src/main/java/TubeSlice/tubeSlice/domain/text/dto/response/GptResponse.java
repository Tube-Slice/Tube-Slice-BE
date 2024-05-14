package TubeSlice.tubeSlice.domain.text.dto.response;

import TubeSlice.tubeSlice.domain.text.dto.request.GptRequest;
import lombok.Getter;

import java.util.List;

@Getter
public class GptResponse {

    private List<Choice> choices;

    @Getter
    public static class Choice {
        private int index;
        private GptRequest.Messages message;
    }
}
