package TubeSlice.tubeSlice.domain.user.dto.response;

import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.domain.user.UserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class UserResponseDto {

    private Boolean isSuccess;
    private String code;
    private String message;

    @JsonProperty("result")
    private LoginResponseDto loginResponseDto;

}
