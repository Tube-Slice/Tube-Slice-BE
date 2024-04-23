package TubeSlice.tubeSlice.domain.script;

import TubeSlice.tubeSlice.domain.userScript.UserScript;
import TubeSlice.tubeSlice.domain.video.Video;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "script")
public class Script {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private LocalDateTime timeline;

    private String script;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    @OneToMany(mappedBy = "script", cascade = CascadeType.ALL)
    private List<UserScript> userScriptList = new ArrayList<>();

}
