package TubeSlice.tubeSlice.domain.keyword;

import TubeSlice.tubeSlice.domain.postKeyword.PostKeyword;
import TubeSlice.tubeSlice.domain.scriptKeyword.ScriptKeyword;
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
    private List<ScriptKeyword> scriptKeywords;

    @OneToMany(mappedBy = "keyword")
    private List<PostKeyword> postKeyword;
}
