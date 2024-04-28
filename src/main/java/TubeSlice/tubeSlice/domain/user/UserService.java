package TubeSlice.tubeSlice.domain.user;


import TubeSlice.tubeSlice.domain.keyword.KeywordConverter;
import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.post.PostConverter;
import TubeSlice.tubeSlice.domain.post.dto.PostResponseDto;
import TubeSlice.tubeSlice.global.response.code.resultCode.ErrorStatus;
import TubeSlice.tubeSlice.global.response.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

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

        return KeywordConverter.toKeywordResultDtoList(postList);
    }

}

