package TubeSlice.tubeSlice.domain.follow;

import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.domain.user.UserService;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import TubeSlice.tubeSlice.global.response.code.resultCode.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/follows")
public class FollowController {
    private final UserService userService;
    private final FollowService followService;

    @PostMapping("/users/{userId}")
    @Operation(summary = "유저 팔로우 하기 API",description = "유저의 id를 받아 해당 유저 팔로우 하기")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "팔로우 할 유저가 존재하지 않습니다",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "팔로우할 유저의 아이디"),
    })
    public ApiResponse<SuccessStatus> createFollow(@AuthenticationPrincipal UserDetails details, @PathVariable(name = "userId") Long userId){
        Long myId = userService.getUserId(details);

        return followService.createFollow(myId, userId);
    }

    @DeleteMapping("/users/{userId}")
    @Operation(summary = "유저 팔로우 취소하기 API",description = "유저의 id를 받아 해당 유저 팔로우  취소하기")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "팔로우 할 유저가 존재하지 않습니다",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "팔로우 최소할 유저의 아이디"),
    })
    public ApiResponse<SuccessStatus> deleteFollow(@AuthenticationPrincipal UserDetails details, @PathVariable(name = "userId") Long userId){
        Long myId = userService.getUserId(details);

        return followService.deleteFollow(myId, userId);
    }

}
