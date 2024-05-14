package TubeSlice.tubeSlice.domain.post;

import TubeSlice.tubeSlice.domain.post.dto.response.PostResponseDto;
import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.domain.user.UserService;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/posts")
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @GetMapping("/{postId}")
    public ApiResponse<PostResponseDto.SinglePostInfoDto> getSinglePostInfo(@AuthenticationPrincipal UserDetails details, @PathVariable(name = "postId")Long postId){
        Post post = postService.findPost(postId);

        Long myId = userService.getUserId(details);
        User user = userService.findUser(myId);

        return ApiResponse.onSuccess(postService.getSinglePostInfo(user, post));
    }
}
