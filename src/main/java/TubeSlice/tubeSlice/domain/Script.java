package TubeSlice.tubeSlice.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class Script {

    @Id
    @GeneratedValue
    @Column(name = "script_id")
    private Long id;

    private LocalDateTime timeline;

    private String script;

    @JoinColumn(name = "video_id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Video video;
}
