package TubeSlice.tubeSlice.domain.post;

import TubeSlice.tubeSlice.domain.comment.Comment;
import TubeSlice.tubeSlice.domain.comment.dto.response.CommentResponseDto;
import TubeSlice.tubeSlice.domain.follow.FollowRepository;
import TubeSlice.tubeSlice.domain.keyword.Keyword;
import TubeSlice.tubeSlice.domain.keyword.KeywordRepository;
import TubeSlice.tubeSlice.domain.post.dto.request.PostRequestDto;
import TubeSlice.tubeSlice.domain.post.dto.response.PostResponseDto;
import TubeSlice.tubeSlice.domain.postKeyword.PostKeyword;
import TubeSlice.tubeSlice.domain.postKeyword.PostKeywordRepository;
import TubeSlice.tubeSlice.domain.postLike.PostLikeRepository;
import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.global.response.code.resultCode.ErrorStatus;
import TubeSlice.tubeSlice.global.response.code.resultCode.SuccessStatus;
import TubeSlice.tubeSlice.global.response.exception.handler.CommentHandler;
import TubeSlice.tubeSlice.global.response.exception.handler.PostHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final PostLikeRepository postLikeRepository;
    private final KeywordRepository keywordRepository;
    private final PostKeywordRepository postKeywordRepository;

    @Transactional
    public Long createPost(User user, PostRequestDto.PostCreateDto postRequestDto){
        Post post = Post.builder()
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .videoUrl(postRequestDto.getYoutubeUrl())
                .user(user)
                .build();

        postRepository.save(post);

        for (String pk: postRequestDto.getPostKeywords()){
            Keyword keyword = Keyword.builder()
                    .name(pk)
                    .build();
            keywordRepository.save(keyword);
            PostKeyword postKeyword = PostKeyword.builder()
                    .keyword(keyword)
                    .post(post)
                    .build();
            postKeywordRepository.save(postKeyword);
        }

        return post.getId();
    }

    @Transactional
    public Long updatePost(User user, Long postId, PostRequestDto.PostUpdateDto postRequestDto){
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

            for (String pk : updatePostKeywords) {
                Optional<Keyword> findKeyword = keywordRepository.findByName(pk);
                Keyword keyword;
                PostKeyword findPostKeyword = postKeywordRepository.findByKeywordAndPostId(findKeyword.orElse(null), postId);

                if (findKeyword.isEmpty()){ //keyword 테이블에 없으면 keyword랑 post_keyword에 둘다 저장 필요.
                     keyword = Keyword.builder()
                            .name(pk)
                            .build();
                    keywordRepository.save(keyword);
                    PostKeyword postKeyword = PostKeyword.builder()
                            .keyword(keyword)
                            .post(findPost)
                            .build();

                    postKeywordRepository.save(postKeyword);
                } else if (findPostKeyword == null){    //keyword에는 있는데 post_keyword에 없는 경우.

                    PostKeyword postKeyword = PostKeyword.builder()
                            .keyword(findKeyword.get())
                            .post(findPost)
                            .build();

                    postKeywordRepository.save(postKeyword);
                }
            }

            //기존 postKeyword 와 비교해서 삭제된 keyword를 db에서 삭제.
            List<PostKeyword> findPostKeywords = postKeywordRepository.findByPostId(postId);
            for (PostKeyword postKeyword: findPostKeywords){
                if (!updatePostKeywords.contains(postKeyword.getKeyword().getName())){
                    postKeywordRepository.delete(postKeyword);
                }
            }
        }
        return findPost.getId();
    }

    @Transactional
    public SuccessStatus deletePost(User user, Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow(()->new PostHandler(ErrorStatus.POST_NOT_FOUND));
        List<PostKeyword> postKeywords = postKeywordRepository.findByPostId(postId);

        if(findPost.getUser() != user){
            throw new PostHandler(ErrorStatus.POST_NOT_FOUND);
        }

        postRepository.delete(findPost);
        postKeywordRepository.deleteAll(postKeywords);

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
