package TubeSlice.tubeSlice.domain.scriptKeyword;

import TubeSlice.tubeSlice.domain.keyword.Keyword;
import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.script.Script;
import TubeSlice.tubeSlice.domain.userScript.UserScript;
import TubeSlice.tubeSlice.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "script_keyword")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScriptKeyword extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_script_id")
    private UserScript userScript;

}
