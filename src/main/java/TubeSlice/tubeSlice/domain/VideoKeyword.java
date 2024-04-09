package TubeSlice.tubeSlice.domain;

import jakarta.persistence.*;

import java.security.Key;

public class VideoKeyword {

    @Id
    @GeneratedValue
    @Column(name = "video_keyword_id")
    private Long id;

    @JoinColumn(name = "video_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Video video;

    @JoinColumn(name = "keyword_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Keyword keyword;

}
