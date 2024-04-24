package TubeSlice.tubeSlice.domain.keyword;

import TubeSlice.tubeSlice.domain.postKeyword.PostKeyword;
import TubeSlice.tubeSlice.domain.videoKeyword.VideoKeyword;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "keyword")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "keyword")
    private List<VideoKeyword> videoKeyword;

    @OneToMany(mappedBy = "keyword")
    private List<PostKeyword> postKeyword;
}
