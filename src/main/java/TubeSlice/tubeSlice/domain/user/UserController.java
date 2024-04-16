package TubeSlice.tubeSlice.domain.user;

import TubeSlice.tubeSlice.domain.user.dto.response.LoginResponseDto;
import TubeSlice.tubeSlice.domain.user.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public UserResponseDto socialLogin(@RequestHeader(name = "accessToken") String access_token,
                                       @RequestHeader(name = "socialType") String social_type){

        LoginResponseDto loginResponseDto = userService.getJwtTokenAndUserId(access_token, social_type);

        return new UserResponseDto(true,"ok", "성공", loginResponseDto);

    }
}
