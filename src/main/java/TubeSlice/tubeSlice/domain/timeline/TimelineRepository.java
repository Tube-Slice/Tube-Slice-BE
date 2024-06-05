package TubeSlice.tubeSlice.domain.timeline;

import TubeSlice.tubeSlice.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimelineRepository extends JpaRepository<Timeline, Long> {

    List<Timeline> findAllByPost(Post post);
}
