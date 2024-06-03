package TubeSlice.tubeSlice.domain.image;

import TubeSlice.tubeSlice.domain.image.dto.response.ImageResponseDto;
import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.domain.user.UserService;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/images")
public class ImageController {
    private final UserService userService;
    private final ImageService imageService;

    @PostMapping(path = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary ="이미지 업로드 API", description = "ImageDto 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "IMAGE402", description = "이미지 업로드 실패",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<ImageResponseDto.ImageDto> uploadImage(@AuthenticationPrincipal UserDetails details, @RequestParam("profile") MultipartFile file){
        Long userId = userService.getUserId(details);
        User user = userService.findUser(userId);

        return ApiResponse.onSuccess(imageService.uploadImage(file, user));
    }
}
