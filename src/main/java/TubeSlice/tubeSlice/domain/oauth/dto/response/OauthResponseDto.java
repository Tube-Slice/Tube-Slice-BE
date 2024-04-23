package TubeSlice.tubeSlice.domain.oauth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OauthResponseDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenDto{
        private String grantType;
        private String accessToken;
    }

}
