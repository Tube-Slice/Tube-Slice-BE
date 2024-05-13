package TubeSlice.tubeSlice.domain.user;


import TubeSlice.tubeSlice.domain.follow.Follow;
import TubeSlice.tubeSlice.domain.keyword.Keyword;
import TubeSlice.tubeSlice.domain.keyword.KeywordConverter;
import TubeSlice.tubeSlice.domain.keyword.KeywordRepository;
import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.post.PostConverter;
import TubeSlice.tubeSlice.domain.post.dto.PostResponseDto;
import TubeSlice.tubeSlice.domain.postKeyword.PostKeyword;
import TubeSlice.tubeSlice.domain.postKeyword.PostKeywordRepository;
import TubeSlice.tubeSlice.domain.user.dto.response.UserResponseDto;
import TubeSlice.tubeSlice.global.response.code.resultCode.ErrorStatus;
import TubeSlice.tubeSlice.global.response.exception.handler.CommentHandler;
import TubeSlice.tubeSlice.global.response.exception.handler.KeywordHandler;
import TubeSlice.tubeSlice.global.response.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final PostKeywordRepository postKeywordRepository;

    public User findUser(Long userId){
        return userRepository.findById(userId).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    public List<PostResponseDto.PostInfoDto> getPostList(User user){
        List<Post> postList = user.getPostList();

        Collections.reverse(postList);

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

    public UserResponseDto.MypageUserInfoDto getMypageUserInfo(User user){
        return UserConverter.toMypageUserInfoDto(user);
    }

    public List<PostResponseDto.PostInfoDto> getPostWithKeyword(User user, String keyword){
        List<Post> postList = user.getPostList();
        Keyword search = keywordRepository.findByName(keyword).orElseThrow(()->new KeywordHandler(ErrorStatus.KEYWORD_NOT_FOUND));

        List<Post> postWithKeywordList = postList.stream()
                .filter(post->post.getPostKeywordList().stream().anyMatch(pk -> pk.getKeyword().equals(search)))
                .toList();

        return PostConverter.toPostInfoDtoList(postWithKeywordList);
    }
}

