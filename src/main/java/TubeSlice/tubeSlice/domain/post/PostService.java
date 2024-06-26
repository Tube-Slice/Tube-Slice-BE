package TubeSlice.tubeSlice.domain.post;

import TubeSlice.tubeSlice.domain.comment.Comment;
import TubeSlice.tubeSlice.domain.comment.dto.response.CommentResponseDto;
import TubeSlice.tubeSlice.domain.follow.FollowRepository;
import TubeSlice.tubeSlice.domain.post.dto.request.PostRequestDto;
import TubeSlice.tubeSlice.domain.post.dto.response.PostResponseDto;
import TubeSlice.tubeSlice.domain.postKeyword.PostKeywordService;
import TubeSlice.tubeSlice.domain.postLike.PostLikeRepository;
import TubeSlice.tubeSlice.domain.timeline.Timeline;
import TubeSlice.tubeSlice.domain.timeline.TimelineConverter;
import TubeSlice.tubeSlice.domain.timeline.TimelineService;
import TubeSlice.tubeSlice.domain.timeline.dto.TimelineResponseDto.TimelineResponseDto;
import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import TubeSlice.tubeSlice.global.response.code.resultCode.ErrorStatus;
import TubeSlice.tubeSlice.global.response.code.resultCode.SuccessStatus;
import TubeSlice.tubeSlice.global.response.exception.handler.PostHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostKeywordService postKeywordService;
    private final TimelineService timelineService;


    @Transactional
    public ApiResponse<SuccessStatus> createPost(User user, PostRequestDto.PostCreateDto postRequestDto) {
        Post post = Post.builder()
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .videoUrl(postRequestDto.getYoutubeUrl())
                .user(user)
                .build();

        postRepository.save(post);

        timelineService.savePostTimeline(postRequestDto, post);
        postKeywordService.savePostKeyword(postRequestDto, post);

        return ApiResponse.onSuccess(SuccessStatus._OK);
    }



    @Transactional
    public SuccessStatus updatePost(User user,  Long postId, PostRequestDto.PostUpdateDto postRequestDto) {
        Post findPost = postRepository.findById(postId).orElseThrow(()-> new PostHandler(ErrorStatus.POST_NOT_FOUND));

        if(findPost.getUser() != user){
            throw new PostHandler(ErrorStatus.POST_NOT_FOUND);
        }

        if (postRequestDto.getTitle()!=null){
            findPost.setTitle(postRequestDto.getTitle());
        }
        if (postRequestDto.getContent()!=null){
            findPost.setContent(postRequestDto.getContent());
        }
        if (postRequestDto.getYoutubeUrl()!=null){
            findPost.setVideoUrl(postRequestDto.getYoutubeUrl());
        }

        postRepository.save(findPost);

        List<String> updatePostKeywords = postRequestDto.getPostKeywords();

        if (updatePostKeywords!=null) {
            postKeywordService.updatePostKeyword(postId, updatePostKeywords, findPost);
        }
        if (postRequestDto.getTimelineDtoList()!=null){
            timelineService.updatePostTimeline(postRequestDto,findPost);
        }

        return SuccessStatus._OK;
    }

    @Transactional
    public SuccessStatus deletePost(Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow(()->new PostHandler(ErrorStatus.POST_NOT_FOUND));

        postRepository.delete(findPost);

        return SuccessStatus._OK;
    }

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

    public CommentResponseDto.PostCommentListDto getPostComment(User user, Post post){
        List<Comment> commentList = post.getCommentList();

        return PostConverter.toPostCommentDtoList(user, commentList);
    }

    public TimelineResponseDto.PostTimelineDtoList getPostTimeline(User user, Post post){
        List<Timeline> timelineList = post.getTimelineList();

        return TimelineConverter.toPostTImelineDtoList(timelineList);
    }


    public PostResponseDto.BoardDtoList getRecentBoard(){
        List<Post> postList = postRepository.findAll();
        Collections.reverse(postList);

        return PostConverter.toRecentBoardDtoList(postList);
    }

    public PostResponseDto.BoardDtoList getPopularBoard(){
        List<Post> postList = postRepository.findAll();

        return PostConverter.toPopularBoardDto(postList);
    }

    public PostResponseDto.BoardDtoList getSearchBoard(String type, String search){
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
