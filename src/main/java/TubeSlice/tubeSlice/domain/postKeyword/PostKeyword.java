package TubeSlice.tubeSlice.domain.postKeyword;

import TubeSlice.tubeSlice.domain.keyword.Keyword;
import TubeSlice.tubeSlice.domain.post.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_keyword")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

}
