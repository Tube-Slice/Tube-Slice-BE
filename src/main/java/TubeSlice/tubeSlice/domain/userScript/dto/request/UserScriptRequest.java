package TubeSlice.tubeSlice.domain.userScript.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


public class UserScriptRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveRequestDto{
        String youtubeUrl;
        private List<String> scriptKeywords;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequestDto{

        String scripts;
        private List<String> scriptKeywords;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class highlightRequestDto{
        private Double timeline;
    }
}
