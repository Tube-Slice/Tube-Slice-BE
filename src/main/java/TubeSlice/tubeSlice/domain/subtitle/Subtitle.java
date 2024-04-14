package TubeSlice.tubeSlice.domain.subtitle;

import TubeSlice.tubeSlice.domain.video.Video;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "subtitle")
public class Subtitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String subtitle;

    private LocalDateTime timeline;

    @JoinColumn(name = "video_id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Video video;
}
