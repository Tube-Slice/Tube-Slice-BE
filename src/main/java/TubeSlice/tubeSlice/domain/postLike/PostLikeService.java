package TubeSlice.tubeSlice.domain.postLike;

import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.post.PostRepository;
import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.domain.user.UserRepository;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import TubeSlice.tubeSlice.global.response.code.resultCode.ErrorStatus;
import TubeSlice.tubeSlice.global.response.code.resultCode.SuccessStatus;
import TubeSlice.tubeSlice.global.response.exception.handler.PostHandler;
import TubeSlice.tubeSlice.global.response.exception.handler.PostLikeHandler;
import TubeSlice.tubeSlice.global.response.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public ApiResponse<SuccessStatus> createPostLike(Long userId, Long postId){
        User user = userRepository.findById(userId).orElseThrow(()->new UserHandler(ErrorStatus.USER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(()-> new PostHandler(ErrorStatus.POST_NOT_FOUND));

        boolean exists = postLikeRepository.existsByUserAndPost(user, post);

        if(exists) {
            return ApiResponse.onSuccess(SuccessStatus._OK);
        }

        PostLike postLike = PostLike.builder()
                .post(post)
                .user(user)
                .build();

        postLikeRepository.save(postLike);

        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @Transactional
    public ApiResponse<SuccessStatus> deletePostLike(Long userId, Long postId){
        User user = userRepository.findById(userId).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(()-> new PostHandler(ErrorStatus.POST_NOT_FOUND));

        PostLike postLike = postLikeRepository.findByUserAndPost(user, post).orElseThrow(()-> new PostLikeHandler(ErrorStatus.POST_LIKE_NOT_FOUND));

        postLikeRepository.delete(postLike);

        return ApiResponse.onSuccess(SuccessStatus._OK);
    }
}
