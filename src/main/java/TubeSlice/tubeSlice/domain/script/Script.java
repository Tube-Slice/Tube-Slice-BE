package TubeSlice.tubeSlice.domain.script;

import TubeSlice.tubeSlice.domain.keyword.Keyword;
import TubeSlice.tubeSlice.domain.subtitle.Subtitle;
import TubeSlice.tubeSlice.domain.text.Text;
import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.domain.userScript.UserScript;
import TubeSlice.tubeSlice.domain.video.Video;
import TubeSlice.tubeSlice.global.entity.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "script")
public class Script extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String videoUrl;

    private String videoTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "script", cascade = CascadeType.ALL)
    private List<Text> userScriptList = new ArrayList<>();

    @OneToMany(mappedBy = "script_keyword", cascade = CascadeType.ALL)
    private List<Keyword> keywords = new ArrayList<>();

    @OneToMany(mappedBy = "subtitle", cascade = CascadeType.ALL)
    private List<Subtitle> subtitles = new ArrayList<>();

}
