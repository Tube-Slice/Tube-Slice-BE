package TubeSlice.tubeSlice.domain.script;

import TubeSlice.tubeSlice.domain.video.Video;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "script")
public class Script {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private LocalDateTime timeline;

    private String script;

    @JoinColumn(name = "video_id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Video video;
}
