package TubeSlice.tubeSlice.domain.video.dto.response;

import TubeSlice.tubeSlice.domain.video.dto.request.GptRequest;
import lombok.Getter;

import java.util.List;

@Getter
public class GptResponse {

    private List<Choice> choices;

    @Getter
    public static class Choice {
        private int index;
        private GptRequest.Message message;
    }
}
