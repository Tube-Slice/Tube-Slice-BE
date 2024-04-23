package TubeSlice.tubeSlice.domain.oauth;

import TubeSlice.tubeSlice.domain.oauth.dto.response.LoginResponseDto;
import TubeSlice.tubeSlice.domain.user.UserService;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/oauth")
public class OauthController {

    private final OauthService userService;

    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> socialLogin(@RequestHeader(name = "access_token") String access_token,
                                                     @RequestHeader(name = "social_type") String social_type){

        LoginResponseDto loginResponseDto = userService.getJwtTokenAndUserId(access_token, social_type);

        return ApiResponse.onSuccess(loginResponseDto);

    }
}
