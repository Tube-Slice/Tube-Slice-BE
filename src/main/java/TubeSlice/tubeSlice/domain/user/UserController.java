package TubeSlice.tubeSlice.domain.user;

import TubeSlice.tubeSlice.domain.post.dto.PostResponseDto;
import TubeSlice.tubeSlice.domain.user.dto.response.UserResponseDto;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me/mypage/posts")
    @Operation(summary = "마이페이지용 나의 게시글 목록 가져오기 API",description = "PostInfoDto의 list를 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<List<PostResponseDto.PostInfoDto>> getMyPostList(@AuthenticationPrincipal UserDetails details){
        Long myId = userService.getUserId(details);
        User user = userService.findUser(myId);

        return ApiResponse.onSuccess(userService.getPostList(user));
    }


    @GetMapping("/{userId}/mypage/posts")
    @Operation(summary = "마이페이지용 특정유저의 게시글 목록 가져오기 API",description = "PostInfoDto의 list를 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "userId", description = "특정 유저의 id"),
    })
    public ApiResponse<List<PostResponseDto.PostInfoDto>> getUserPostList(@AuthenticationPrincipal UserDetails details,@PathVariable(name = "userId")Long userId){
        User user = userService.findUser(userId);

        return ApiResponse.onSuccess(userService.getPostList(user));
    }

    @GetMapping("/me/mypage/keywords")
    @Operation(summary = "마이페이지용 나의 키워드 목록 가져오기 API",description = "KeywordResponseDto list를 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<List<KeywordResponseDto.KeywordResultDto>> getMyKeywordList(@AuthenticationPrincipal UserDetails details){
        Long myId = userService.getUserId(details);
        User user = userService.findUser(myId);

        return ApiResponse.onSuccess(userService.getUserKeywordList(user));
    }

    @GetMapping("/{userId}/mypage/keywords")
    @Operation(summary = "마이페이지용 특정유저의 키워드 목록 가져오기 API",description = "KeywordResponseDto list를 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "userId", description = "특정 유저의 id"),
    })
    public ApiResponse<List<KeywordResponseDto.KeywordResultDto>> getUserKeywordList(@AuthenticationPrincipal UserDetails details,@PathVariable(name = "userId")Long userId){
        User user = userService.findUser(userId);

        return ApiResponse.onSuccess(userService.getUserKeywordList(user));
    }

    @GetMapping("/me/following")
    @Operation(summary = "나의 팔로잉 목록 가져오기 API",description = "FollowListDto 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<UserResponseDto.FollowListDto> getMyFollowingList(@AuthenticationPrincipal UserDetails details) {
        // 나의 기준 팔로잉 리스트 필요
        Long myId = userService.getUserId(details);
        User me = userService.findUser(myId);
        // 보여질 팔로잉 리스트
        User user = userService.findUser(myId);
        return ApiResponse.onSuccess(userService.getFollowingList(me, user));
    }

    @GetMapping("/{userId}/following")
    @Operation(summary = "특정유저의 팔로잉 목록 가져오기 API",description = "FollowListDto 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<UserResponseDto.FollowListDto> getUserFollowingList(@AuthenticationPrincipal UserDetails details,@PathVariable(name = "userId")Long userId) {
        // 나의 기준 팔로잉 리스트 필요
        Long myId = userService.getUserId(details);
        User me = userService.findUser(myId);
        // 보여질 팔로잉 리스트
        User user = userService.findUser(userId);
        return ApiResponse.onSuccess(userService.getFollowingList(me, user));
    }

    @GetMapping("/me/follower")
    @Operation(summary = "나의 팔로워 목록 가져오기 API",description = "FollowListDto 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<UserResponseDto.FollowListDto> getMyFollowerList(@AuthenticationPrincipal UserDetails details){
        Long myId = userService.getUserId(details);
        User me = userService.findUser(myId);;
        User user = userService.findUser(myId);

        return ApiResponse.onSuccess(userService.getFollowerList(me, user));
    }

    @GetMapping("/{userId}/follower")
    @Operation(summary = "특정유저의 팔로워 목록 가져오기 API",description = "FollowListDto 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<UserResponseDto.FollowListDto> getUserFollowerList(@AuthenticationPrincipal UserDetails details, @PathVariable(name = "userId")Long userId) {
        // 나의 기준 팔로잉 리스트 필요
        Long myId = userService.getUserId(details);
        User me = userService.findUser(myId);
        // 보여질 팔로잉 리스트
        User user = userService.findUser(userId);
        return ApiResponse.onSuccess(userService.getFollowerList(me, user));
    }

    @GetMapping("/me/mypage")
    @Operation(summary = "마이페이지용 나의 정보 가져오기 API",description = "MypageUserInfoDto 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<UserResponseDto.MypageUserInfoDto> getMyPageMyInfo(@AuthenticationPrincipal UserDetails details){
        Long userId = userService.getUserId(details);
        User user = userService.findUser(userId);

        return ApiResponse.onSuccess(userService.getMypageUserInfo(user, user));
    }

    @GetMapping("/{userId}/mypage")
    @Operation(summary = "마이페이지용 특정유저 정보 가져오기 API",description = "MypageUserInfoDto 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<UserResponseDto.MypageUserInfoDto> getMyPageUserInfo(@AuthenticationPrincipal UserDetails details, @PathVariable(name = "userId")Long userId){
        Long myId = userService.getUserId(details);
        User me = userService.findUser(myId);
        User user = userService.findUser(userId);

        return ApiResponse.onSuccess(userService.getMypageUserInfo(me, user));
    }

    @GetMapping("/me/posts")
    @Operation(summary = "나의 키워드 기반 게시글 가져오기 API",description = "keyword를 parameter로 받아 PostInfoDto의 List를 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "KEYWORD401", description = "키워드가 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "keyword", description = "검색할 키워드"),
    })
    public ApiResponse<List<PostResponseDto.PostInfoDto>> getMyPostWithKeyword(@AuthenticationPrincipal UserDetails details, @RequestParam(name = "keyword")String keyword){
        Long userId = userService.getUserId(details);
        User user = userService.findUser(userId);

        return ApiResponse.onSuccess(userService.getPostWithKeyword(user, keyword));
    }

    @GetMapping("/{userId}/posts")
    @Operation(summary = "특정유저의 키워드 기반 게시글 가져오기 API",description = "keyword를 parameter로 받아 PostInfoDto의 List를 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "KEYWORD401", description = "키워드가 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "keyword", description = "검색할 키워드"),
    })
    public ApiResponse<List<PostResponseDto.PostInfoDto>> getUserPostWithKeyword(@PathVariable(name="userId")Long userId,  @RequestParam(name = "keyword")String keyword){
        User user = userService.findUser(userId);

        return ApiResponse.onSuccess(userService.getPostWithKeyword(user, keyword));
    }
}
