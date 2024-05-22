package TubeSlice.tubeSlice.domain.postKeyword;

import TubeSlice.tubeSlice.domain.keyword.Keyword;
import TubeSlice.tubeSlice.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostKeywordRepository extends JpaRepository<PostKeyword, Long> {
    List<PostKeyword> findByPostAndKeyword(Post post, Keyword keyword);
    List<PostKeyword> findByPostId(Long postId);
    PostKeyword findByPost(Post post);
    PostKeyword findByKeywordAndPostId(Keyword keyword, Long postId);
}
