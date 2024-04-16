package TubeSlice.tubeSlice.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NaverLoginDto {

    private String code;
    private String message;
    private Response response;

    @Getter
    public static class Response{
        private String id;
        private String email;
        private String name;
    }


}
