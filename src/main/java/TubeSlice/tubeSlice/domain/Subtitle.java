package TubeSlice.tubeSlice.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class Subtitle {

    @Id
    @GeneratedValue
    @Column(name = "subtitle_id")
    private Long id;

    private String subtitle;

    private LocalDateTime timeline;

    @JoinColumn(name = "video_id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Video video;
}
