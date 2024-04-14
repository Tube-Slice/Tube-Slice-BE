package TubeSlice.tubeSlice.domain.video;

import TubeSlice.tubeSlice.domain.videoKeyword.VideoKeyword;
import TubeSlice.tubeSlice.domain.record.Record;
import TubeSlice.tubeSlice.domain.subtitle.Subtitle;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "video")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String title;

    private String thumbnail;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Record> records;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<VideoKeyword> videoKeywords;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Subtitle> subtitles;
}
