package TubeSlice.tubeSlice.domain.oauth.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoLoginDto {

    private String id;
    private LocalDateTime localDateTime;

    private Properties properties;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Kakao_account {    //필요 없는 값들 안받음.
        private Boolean agreement;
        private String nickname;
        private Boolean isDefaultNickname;
    }

    @Getter
    public static class Properties {
        private String nickname;
    }


}
