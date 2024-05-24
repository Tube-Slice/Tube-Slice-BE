package TubeSlice.tubeSlice.domain.user;

import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import TubeSlice.tubeSlice.domain.post.dto.response.PostResponseDto;
import TubeSlice.tubeSlice.domain.user.dto.request.UserRequestDto;
import TubeSlice.tubeSlice.domain.user.dto.response.UserResponseDto;
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
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me/mypage/posts")
    @Operation(summary = "마이페이지용 나의 게시글 목록 가져오기 API",description = "PostInfoListDto를 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "page", description = "페이지 수"),
            @Parameter(name = "size", description = "페이지 사이즈"),
    })
    public ApiResponse<PostResponseDto.PostInfoListDto> getMyPostList(@AuthenticationPrincipal UserDetails details, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Long myId = userService.getUserId(details);
        User user = userService.findUser(myId);

        return ApiResponse.onSuccess(userService.getPostList(user, page, size));
    }


    @GetMapping("/{userId}/mypage/posts")
    @Operation(summary = "마이페이지용 특정유저의 게시글 목록 가져오기 API",description = "PostInfoListDto를 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "userId", description = "특정 유저의 id"),
            @Parameter(name = "page", description = "페이지 수"),
            @Parameter(name = "size", description = "페이지 사이즈"),
    })
    public ApiResponse<PostResponseDto.PostInfoListDto> getUserPostList(@PathVariable(name = "userId")Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        User user = userService.findUser(userId);

        return ApiResponse.onSuccess(userService.getPostList(user, page, size));
    }

    @GetMapping("/me/mypage/keywords")
    @Operation(summary = "마이페이지용 나의 키워드 목록 가져오기 API",description = "KeywordResponseDto list를 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<KeywordResponseDto.KeywordDtoList> getMyKeywordList(@AuthenticationPrincipal UserDetails details, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
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
    public ApiResponse<KeywordResponseDto.KeywordDtoList> getUserKeywordList(@PathVariable(name = "userId")Long userId){
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
    @Operation(summary = "나의 키워드 기반 게시글 가져오기 API",description = "keyword를 parameter로 받아 PostInfoListDto를 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "KEYWORD401", description = "키워드가 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "keyword", description = "검색할 키워드"),
            @Parameter(name = "page", description = "페이지 수"),
            @Parameter(name = "size", description = "페이지 사이즈"),

    })
    public ApiResponse<PostResponseDto.PostInfoListDto> getMyPostWithKeyword(@AuthenticationPrincipal UserDetails details, @RequestParam(name = "keyword")String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Long userId = userService.getUserId(details);
        User user = userService.findUser(userId);

        return ApiResponse.onSuccess(userService.getPostWithKeyword(user, keyword, page, size));
    }

    @GetMapping("/{userId}/posts")
    @Operation(summary = "특정유저의 키워드 기반 게시글 가져오기 API",description = "keyword를 parameter로 받아 PostInfoListDto를 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "KEYWORD401", description = "키워드가 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "keyword", description = "검색할 키워드"),
            @Parameter(name = "page", description = "페이지 수"),
            @Parameter(name = "size", description = "페이지 사이즈"),
    })
    public ApiResponse<PostResponseDto.PostInfoListDto> getUserPostWithKeyword(@PathVariable(name="userId")Long userId, @RequestParam(name = "keyword")String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        User user = userService.findUser(userId);

        return ApiResponse.onSuccess(userService.getPostWithKeyword(user, keyword, page, size));
    }

    @PatchMapping("/")
    @Operation(summary = "유저 정보 수정하기 API",description = "유저 정보를 수정")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<SuccessStatus> updateUserInfo(@AuthenticationPrincipal UserDetails details, @RequestBody UserRequestDto.UserInfoUpdateDto request){
        Long userId = userService.getUserId(details);
        User user = userService.findUser(userId);

        return userService.updateUserInfo(user, request);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "유저 탈퇴하기 API",description = "유저 탈퇴하는 기능, 실행시  login_status가 INACTIVATE로 변환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public ApiResponse<SuccessStatus> deleteUser(@AuthenticationPrincipal UserDetails details){
        Long userId = userService.getUserId(details);
        User user = userService.findUser(userId);

        return userService.deleteUser(user);
    }
}
