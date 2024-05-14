package TubeSlice.tubeSlice.domain.text.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GptRequest {

    private String model;
    private List<Messages> messages;


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Messages {
        private String role;
        private String content;
    }
}
