package TubeSlice.tubeSlice.domain.post;

import TubeSlice.tubeSlice.domain.follow.FollowRepository;
import TubeSlice.tubeSlice.domain.post.dto.response.PostResponseDto;
import TubeSlice.tubeSlice.domain.postLike.PostLikeRepository;
import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.global.response.code.resultCode.ErrorStatus;
import TubeSlice.tubeSlice.global.response.exception.handler.PostHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final PostLikeRepository postLikeRepository;

    public Post findPost(Long postId){
        return postRepository.findById(postId).orElseThrow(()-> new PostHandler(ErrorStatus.POST_NOT_FOUND));
    }

    public PostResponseDto.SinglePostInfoDto getSinglePostInfo(User user, Post post){
        Boolean isFollowing = false;
        if(post.getUser() == user){
            isFollowing = true;
        } else if(followRepository.existsBySenderAndReceiver(user, post.getUser())){
            isFollowing = true;
        }
        Boolean isLike = postLikeRepository.existsByUserAndPost(user, post);
        return PostConverter.toSinglePostDto(user, post, isFollowing, isLike );
    }

    public List<PostResponseDto.BoardDto> getRecentBoard(){
        List<Post> postList = postRepository.findAll();
        Collections.reverse(postList);

        return PostConverter.toRecentBoardDtoList(postList);
    }

    public List<PostResponseDto.BoardDto> getPopularBoard(){
        List<Post> postList = postRepository.findAll();

        return PostConverter.toPopularBoardDto(postList);
    }
}
