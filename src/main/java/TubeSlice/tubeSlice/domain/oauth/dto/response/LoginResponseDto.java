package TubeSlice.tubeSlice.domain.oauth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponseDto {

        private Long userId;
        private String name;
        private String access_token;
        private Boolean isUser;
}
