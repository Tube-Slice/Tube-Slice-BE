package TubeSlice.tubeSlice.domain.user;


import TubeSlice.tubeSlice.domain.follow.Follow;
import TubeSlice.tubeSlice.domain.follow.FollowRepository;
import TubeSlice.tubeSlice.domain.image.Image;
import TubeSlice.tubeSlice.domain.image.ImageRepository;
import TubeSlice.tubeSlice.domain.keyword.Keyword;
import TubeSlice.tubeSlice.domain.keyword.KeywordConverter;
import TubeSlice.tubeSlice.domain.keyword.KeywordRepository;
import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.post.PostConverter;
import TubeSlice.tubeSlice.domain.post.PostRepository;
import TubeSlice.tubeSlice.domain.post.dto.response.PostResponseDto;
import TubeSlice.tubeSlice.domain.user.dto.request.UserRequestDto;
import TubeSlice.tubeSlice.domain.user.dto.response.UserResponseDto;
import TubeSlice.tubeSlice.global.jwt.UserDetailsImpl;
import TubeSlice.tubeSlice.global.jwt.UserDetailsServiceImpl;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import TubeSlice.tubeSlice.global.response.code.resultCode.ErrorStatus;
import TubeSlice.tubeSlice.global.response.code.resultCode.SuccessStatus;
import TubeSlice.tubeSlice.global.response.exception.handler.KeywordHandler;
import TubeSlice.tubeSlice.global.response.exception.handler.PostHandler;
import TubeSlice.tubeSlice.global.response.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final KeywordRepository keywordRepository;
    private final PostRepository postRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final FollowRepository followRepository;
    private final ImageRepository imageRepository;

    public Long getUserId(UserDetails user){
        String username = user.getUsername();
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
        return userDetails.getUserId();
    }

    public User findUser(Long userId){
        return userRepository.findById(userId).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    public PostResponseDto.PostInfoListDto getPostList(User user, Integer page, Integer size){
        Page<Post> postList = postRepository.findAllByUser(user, PageRequest.of(page, size));
        return PostConverter.toPostInfoDtoList(postList);
    }

    public KeywordResponseDto.KeywordDtoList getUserKeywordList(User user){
        List<Post> postList = user.getPostList();

        return KeywordConverter.toKeywordDtoList(postList);
    }

    // 나 혹은 특정유저가 팔로우 중인 사람이 담긴 테이블
    // 추후에 팔로우 유무에 쓰이는 함수
    public List<Long> getUserFollowingIdList(User user){
        List<Follow> myFollowingList = user.getFollowingList();
        List<Long> followingIdList = new ArrayList<>();

        for (Follow following : myFollowingList){
            Long followingId = following.getReceiver().getId();
            followingIdList.add(followingId);
        }

        return followingIdList;
    }

    public UserResponseDto.FollowListDto getFollowingList(User me, User user){
        List<Long> myFollowingIdList = getUserFollowingIdList(me);
        List<Follow> followList = user.getFollowingList();

        return UserConverter.toFollowingListDto(myFollowingIdList, followList, user);
    }

    public UserResponseDto.FollowListDto getFollowerList(User me, User user){
        List<Long> myFollowingIdList = getUserFollowingIdList(me);
        List<Follow> followList = user.getFollowerList();

        return UserConverter.toFollowerListDto(myFollowingIdList, followList, user);
    }

    public UserResponseDto.MypageUserInfoDto getMypageUserInfo(User me, User user){
        boolean isFollowing = false;
        if(me == user) {
            isFollowing = true;
        }
        else if(followRepository.existsBySenderAndReceiver(me, user)){
            isFollowing = true;
        }
        return UserConverter.toMypageUserInfoDto(user, isFollowing);
    }

    public PostResponseDto.PostInfoListDto getPostWithKeyword(User user, String keyword, Integer page, Integer size){
        List<Post> postList = user.getPostList();
        if(postList == null){
            return null;
        }
        Keyword search = keywordRepository.findByName(keyword).orElseThrow(()->new KeywordHandler(ErrorStatus.KEYWORD_NOT_FOUND));

        List<Post> postWithKeywordList = postList.stream()
                .filter(post->post.getPostKeywordList().stream().anyMatch(pk -> pk.getKeyword().equals(search)))
                .toList();

        Pageable pageable = PageRequest.of(page,size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), postWithKeywordList.size());
        List<Post> paginatedList = postWithKeywordList.subList(start, end);

        Page<Post> postPage = new PageImpl<>(paginatedList, pageable, postWithKeywordList.size());

        return PostConverter.toPostInfoDtoList(postPage);
    }

    @Transactional
    public ApiResponse<SuccessStatus> updateUserInfo(User user, UserRequestDto.UserInfoUpdateDto request){
        if(request.getNickname() != null){
            user.setNickname(request.getNickname());
        }
        if(request.getIntroduction() != null){
            user.setIntroduction(request.getIntroduction());
        }
        if(request.getImageId() != null){
            Image image = imageRepository.getReferenceById(request.getImageId());
            user.setProfileUrl(image.getUrl());
        }

        userRepository.save(user);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @Transactional
    public ApiResponse<SuccessStatus> deleteUser(User user){
        userRepository.delete(user);

        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    public PostResponseDto.PostInfoListDto getMyPageSearch(User user, String type, String search, Integer page, Integer size){
        List<Post> userPostList = user.getPostList();
        if(userPostList == null){
            return null;
        }

        if(type.equals("TITLE")){
            Page<Post> postList = postRepository.findPostsByTitleAndUser(search, user, PageRequest.of(page, size));

            return PostConverter.toPostInfoDtoList(postList);
        } else if(type.equals("CONTENT")){
            Page<Post> postList = postRepository.findPostsByContentAndUser(search, user,  PageRequest.of(page, size));

            return PostConverter.toPostInfoDtoList(postList);
        } else if(type.equals("BOTH")) {
            Page<Post> postList = postRepository.findPostsByTitleOrContentAndUser(search, user ,PageRequest.of(page, size));

            return PostConverter.toPostInfoDtoList(postList);
        } else {
            throw new PostHandler(ErrorStatus.POST_SEARCH_BAD_REQUEST);
        }


    }

    public UserResponseDto.FollowListDto getSearchFollowList(User me, User user, String type, String nickname){
        List<Long> myFollowingIdList = getUserFollowingIdList(me);

        if(type.equals("FOLLOWING")){
            List<Follow> followList = user.getFollowingList();

            List<Follow> nicknameList = followList.stream()
                    .filter(follow -> {return follow.getReceiver().getNickname().contains(nickname);})
                    .toList();

            return UserConverter.toFollowingListDto(myFollowingIdList, nicknameList, user);

        } else if (type.equals("FOLLOWER")){
            List<Follow> followList = user.getFollowerList();

            List<Follow> nicknameList = followList.stream()
                    .filter(follow -> {return follow.getSender().getNickname().contains(nickname);})
                    .toList();

            return UserConverter.toFollowerListDto(myFollowingIdList, nicknameList, user);

        } else {
            throw new UserHandler(ErrorStatus.USER_TYPE_NOT_VALID);
        }


    }
}

