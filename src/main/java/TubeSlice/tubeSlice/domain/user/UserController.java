package TubeSlice.tubeSlice.domain.user;

import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me/keywords")
    public ApiResponse<List<KeywordResponseDto.KeywordResultDto>> getMyKeywordList(){
        User user = userService.findUser(1L);

        return ApiResponse.onSuccess(userService.getUserKeywordList(user));
    }

    @GetMapping("/{userId}/keywords")
    public ApiResponse<List<KeywordResponseDto.KeywordResultDto>> getUserKeywordList(@PathVariable(name = "userId")Long userId){
        User user = userService.findUser(userId);

        return ApiResponse.onSuccess(userService.getUserKeywordList(user));
    }
}
