package TubeSlice.tubeSlice.domain.userScript.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.query.sqm.internal.SqmCreationProcessingStateImpl;

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

        private List<Script> script;
        private List<String> scriptKeywords;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Script{
            private Double timeline;
            private String text;
        }
    }

}
