package TubeSlice.tubeSlice.domain.subtitle;

import TubeSlice.tubeSlice.domain.script.Script;
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

    private Float timeline;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "script_id")
    private Script script;
}
