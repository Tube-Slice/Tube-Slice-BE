package TubeSlice.tubeSlice.domain.follow;

import TubeSlice.tubeSlice.domain.user.User;

public class FollowConverter {

    public static Follow toFollow(User me, User user){
        return Follow.builder()
                .sender(me)
                .receiver(user)
                .build();
    }
}
