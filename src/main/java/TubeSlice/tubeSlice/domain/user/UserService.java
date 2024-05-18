package TubeSlice.tubeSlice.domain.user;


import TubeSlice.tubeSlice.domain.follow.Follow;
import TubeSlice.tubeSlice.domain.follow.FollowRepository;
import TubeSlice.tubeSlice.domain.keyword.Keyword;
import TubeSlice.tubeSlice.domain.keyword.KeywordConverter;
import TubeSlice.tubeSlice.domain.keyword.KeywordRepository;
import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.post.PostConverter;
import TubeSlice.tubeSlice.domain.post.PostRepository;
import TubeSlice.tubeSlice.domain.post.dto.response.PostResponseDto;
import TubeSlice.tubeSlice.domain.postKeyword.PostKeywordRepository;
import TubeSlice.tubeSlice.domain.user.dto.request.UserRequestDto;
import TubeSlice.tubeSlice.domain.user.dto.response.UserResponseDto;
import TubeSlice.tubeSlice.global.jwt.UserDetailsImpl;
import TubeSlice.tubeSlice.global.jwt.UserDetailsServiceImpl;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import TubeSlice.tubeSlice.global.response.code.resultCode.ErrorStatus;
import TubeSlice.tubeSlice.global.response.code.resultCode.SuccessStatus;
import TubeSlice.tubeSlice.global.response.exception.handler.KeywordHandler;
import TubeSlice.tubeSlice.global.response.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public List<KeywordResponseDto.KeywordResultDto> getUserKeywordList(User user){
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
        if(request.getProfileUrl() != null){
            user.setProfileUrl(request.getProfileUrl());
        }

        userRepository.save(user);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @Transactional
    public ApiResponse<SuccessStatus> deleteUser(User user){
        userRepository.delete(user);

        return ApiResponse.onSuccess(SuccessStatus._OK);
    }
}

