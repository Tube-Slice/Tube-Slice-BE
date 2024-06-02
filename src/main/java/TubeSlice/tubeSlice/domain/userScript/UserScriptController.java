package TubeSlice.tubeSlice.domain.userScript;

import TubeSlice.tubeSlice.domain.script.Script;
import TubeSlice.tubeSlice.domain.script.ScriptRepository;
import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.domain.user.UserService;
import TubeSlice.tubeSlice.domain.userScript.dto.request.UserScriptRequest;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import TubeSlice.tubeSlice.global.response.code.resultCode.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user-scripts/")
public class UserScriptController {

    private final UserScriptService userScriptService;
    private final UserService userService;

    private final ScriptRepository scriptRepository;

    @PostMapping("/save")
    @Operation(summary = "스크립트 저장하기",description = "youtubeUrl과 키워드 입력받아 스크립드와 함께 저장")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<SuccessStatus> saveScript(@AuthenticationPrincipal UserDetails details, @RequestBody UserScriptRequest.SaveRequestDto requestDto){
        Long userId = userService.getUserId(details);
        User user = userService.findUser(userId);

        Script script = scriptRepository.findByVideoUrl(requestDto.getYoutubeUrl());
        userScriptService.saveScript(user,script, requestDto);

        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @PatchMapping("/{userScriptId}/highlights")
    @Operation(summary = "스크립트 강조하기",description = "강조할 문장의 타임라인을 입력받아 해당 문장 강조")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<SuccessStatus> highlightScript(@AuthenticationPrincipal UserDetails details,
                                                      @PathVariable("userScriptId") Long userScriptId,
                                                      @RequestBody List<UserScriptRequest.highlightRequestDto> requestDto){
        Long userId = userService.getUserId(details);
        User user = userService.findUser(userId);

        return ApiResponse.onSuccess(userScriptService.highlightScript(user, userScriptId, requestDto));
    }
}
