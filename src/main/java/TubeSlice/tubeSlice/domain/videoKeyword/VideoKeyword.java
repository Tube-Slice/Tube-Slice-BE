package TubeSlice.tubeSlice.domain.videoKeyword;

import TubeSlice.tubeSlice.domain.keyword.Keyword;
import TubeSlice.tubeSlice.domain.video.Video;
import jakarta.persistence.*;

@Entity
@Table(name = "video_keyword")
public class VideoKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "video_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Video video;

    @JoinColumn(name = "keyword_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Keyword keyword;

}
