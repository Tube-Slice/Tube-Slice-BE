package TubeSlice.tubeSlice.domain.user;


import TubeSlice.tubeSlice.domain.follow.FollowRepository;
import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.post.dto.response.PostResponeDto;
import TubeSlice.tubeSlice.domain.user.dto.response.UserResponseDto;
import TubeSlice.tubeSlice.global.response.code.resultCode.ErrorStatus;
import TubeSlice.tubeSlice.global.response.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public UserResponseDto.PageResponseDto getMyPage(Long myId, Long userId){
        User me = userRepository.findById(myId).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        boolean follow = followRepository.existsBySenderAndReceiver(me, user);

        UserResponseDto.UserInfoDto userInfoDto = UserConverter.toUserInfoDto(me,user, follow);

        List<Post> postList = user.getPostList();

        List<PostResponeDto.PostInfoDto> postInfoDto = UserConverter.toPostInfoDtoList(postList);

        List<String> userKeyword = UserConverter.toUserKeyword(user);

        return UserResponseDto.PageResponseDto.builder()
                .user(userInfoDto)
                .post(postInfoDto)
                .keyword(userKeyword)
                .build();

    }

}

