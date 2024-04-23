package TubeSlice.tubeSlice.domain.oauth;

import TubeSlice.tubeSlice.domain.oauth.dto.response.LoginResponseDto;
import TubeSlice.tubeSlice.domain.user.UserService;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "로그인 API", description = "로그인 후 accessToken 전송, LoginResponseDto 이용")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "유저가 존재하지 않습니다",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<LoginResponseDto> socialLogin(@RequestHeader(name = "access_token") String access_token,
                                                     @RequestHeader(name = "social_type") String social_type){

        LoginResponseDto loginResponseDto = userService.getJwtTokenAndUserId(access_token, social_type);

        return ApiResponse.onSuccess(loginResponseDto);

    }
}
