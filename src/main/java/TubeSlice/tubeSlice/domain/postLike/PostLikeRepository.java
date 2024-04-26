package TubeSlice.tubeSlice.domain.postLike;

import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByUserAndPost(User user, Post post);

    Optional<PostLike> findByUserAndPost(User user, Post post);
}
