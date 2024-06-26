package TubeSlice.tubeSlice.domain.script;

import TubeSlice.tubeSlice.domain.keyword.Keyword;
import TubeSlice.tubeSlice.domain.scriptKeyword.ScriptKeyword;
import TubeSlice.tubeSlice.domain.subtitle.Subtitle;
import TubeSlice.tubeSlice.domain.text.Text;
import TubeSlice.tubeSlice.domain.user.User;

import TubeSlice.tubeSlice.domain.userScript.UserScript;
import TubeSlice.tubeSlice.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "script")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Script extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String videoUrl;

    private String videoTitle;

    private String scriptTitle;

    @OneToMany(mappedBy = "script", cascade = CascadeType.ALL)
    private List<UserScript> userScriptList = new ArrayList<>();

    @OneToMany(mappedBy = "script", cascade = CascadeType.ALL)
    private List<Subtitle> subtitles = new ArrayList<>();

}
