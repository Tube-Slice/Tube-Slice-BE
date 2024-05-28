package TubeSlice.tubeSlice.domain.userScript;

import TubeSlice.tubeSlice.domain.script.Script;
import TubeSlice.tubeSlice.domain.script.ScriptRepository;
import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.domain.user.UserService;
import TubeSlice.tubeSlice.domain.userScript.dto.request.UserScriptRequest;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import TubeSlice.tubeSlice.global.response.code.resultCode.SuccessStatus;
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
    public ApiResponse<SuccessStatus> saveScript(@AuthenticationPrincipal UserDetails details, @RequestBody UserScriptRequest.SaveRequestDto requestDto){
        Long userId = userService.getUserId(details);
        User user = userService.findUser(userId);

        Script script = scriptRepository.findByVideoUrl(requestDto.getYoutubeUrl());
        userScriptService.saveScript(user,script, requestDto);

        return ApiResponse.onSuccess(SuccessStatus._OK);
    }
}
