package TubeSlice.tubeSlice.domain.user.dto.request;

import lombok.*;

public class UserRequestDto {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfoUpdateDto{
        private String nickname;
        private Long imageId;
        private String introduction;
    }

}
