package TubeSlice.tubeSlice.domain.post;

import TubeSlice.tubeSlice.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p join fetch p.user")
    Page<Post> findAllByUser(User user, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:title%")
    List<Post> findPostsByTitle(String title);

    @Query("SELECT p FROM Post p WHERE p.content LIKE %:content%")
    List<Post> findPostsByContent(String content);

    @Query("SELECT DISTINCT p FROM Post p WHERE p.title LIKE %:search% OR p.content LIKE %:search%")
    List<Post> findPostsByTitleOrContent(String search);

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:title% AND p.user = :user")
    Page<Post> findPostsByTitleAndUser(String title, User user, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.content LIKE %:content% AND p.user = :user")
    Page<Post> findPostsByContentAndUser(String content, User user, Pageable pageable);


    @Query("SELECT DISTINCT p FROM Post p WHERE p.title LIKE %:search% OR p.content LIKE %:search% AND p.user = :user")
    Page<Post> findPostsByTitleOrContentAndUser(String search, User user, Pageable pageable);
}
