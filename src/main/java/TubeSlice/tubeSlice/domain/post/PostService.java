package TubeSlice.tubeSlice.domain.post;

import TubeSlice.tubeSlice.domain.comment.Comment;
import TubeSlice.tubeSlice.domain.comment.dto.response.CommentResponseDto;
import TubeSlice.tubeSlice.domain.follow.FollowRepository;
import TubeSlice.tubeSlice.domain.post.dto.response.PostResponseDto;
import TubeSlice.tubeSlice.domain.postLike.PostLikeRepository;
import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.global.response.code.resultCode.ErrorStatus;
import TubeSlice.tubeSlice.global.response.exception.handler.PostHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    public List<CommentResponseDto.PostCommentDto> getPostComment(User user, Post post){
        List<Comment> commentList = post.getCommentList();

        return PostConverter.toPostCommentDtoList(user, commentList);
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

    public List<PostResponseDto.BoardDto> getSearchBoard(String type, String search){
        if(type.equals("TITLE")){
            List<Post> postList = postRepository.findPostsByTitle(search);
            Collections.reverse(postList);

            return PostConverter.toRecentBoardDtoList(postList);
        } else if(type.equals("CONTENT")){
            List<Post> postList = postRepository.findPostsByContent(search);
            Collections.reverse(postList);

            return PostConverter.toRecentBoardDtoList(postList);
        } else if(type.equals("BOTH")) {
            List<Post> postList = postRepository.findPostsByTitleOrContent(search);
            Collections.reverse(postList);

            return PostConverter.toRecentBoardDtoList(postList);
        } else {
            throw new PostHandler(ErrorStatus.POST_SEARCH_BAD_REQUEST);
        }

    }
}
