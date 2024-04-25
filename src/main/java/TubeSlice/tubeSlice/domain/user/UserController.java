package TubeSlice.tubeSlice.domain.user;

import TubeSlice.tubeSlice.domain.oauth.CustomOauth2User;
import TubeSlice.tubeSlice.domain.user.dto.response.UserResponseDto;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me/mypage")
    public ApiResponse<UserResponseDto.PageResponseDto> getMyPage(){
        return ApiResponse.onSuccess(userService.getMyPage(1L, 1L));
    }

}
