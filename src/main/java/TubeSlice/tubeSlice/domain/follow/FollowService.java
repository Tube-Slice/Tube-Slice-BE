package TubeSlice.tubeSlice.domain.follow;

import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.domain.user.UserRepository;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import TubeSlice.tubeSlice.global.response.code.resultCode.ErrorStatus;
import TubeSlice.tubeSlice.global.response.code.resultCode.SuccessStatus;
import TubeSlice.tubeSlice.global.response.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public ApiResponse<SuccessStatus> createFollow(Long myId, Long userId){
        User me = userRepository.findById(myId).orElseThrow(()->new UserHandler(ErrorStatus.USER_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        boolean existFollow = followRepository.existsBySenderAndReceiver(me, user);

        if(existFollow){
            return ApiResponse.onSuccess(SuccessStatus._OK);
        }

        Follow follow = FollowConverter.toFollow(me, user);

        followRepository.save(follow);

        return ApiResponse.onSuccess(SuccessStatus._OK);

    }

    @Transactional
    public ApiResponse<SuccessStatus> deleteFollow(Long myId, Long userId){
        User me = userRepository.findById(myId).orElseThrow(()->new UserHandler(ErrorStatus.USER_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Follow follow = followRepository.findBySenderAndReceiver(me, user);

        followRepository.delete(follow);

        return ApiResponse.onSuccess(SuccessStatus._OK);
    }
}
