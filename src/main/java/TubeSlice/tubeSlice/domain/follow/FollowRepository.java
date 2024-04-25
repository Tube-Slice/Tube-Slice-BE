package TubeSlice.tubeSlice.domain.follow;

import TubeSlice.tubeSlice.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsBySenderAndReceiver(User sender, User receiver);

    Follow findBySenderAndReceiver(User sender, User receiver);
}
