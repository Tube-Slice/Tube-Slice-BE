package TubeSlice.tubeSlice.domain.user;

import TubeSlice.tubeSlice.domain.oauth.CustomOauth2User;
import TubeSlice.tubeSlice.domain.user.dto.response.UserResponseDto;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "나의 마이페이지 가져오기 API",description = "나의 마이페이지 불러오기, PageResponseDto 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<UserResponseDto.PageResponseDto> getMyPage(){
        return ApiResponse.onSuccess(userService.getMyPage(1L, 1L));
    }

}
