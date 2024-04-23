package TubeSlice.tubeSlice.domain.video;

import TubeSlice.tubeSlice.domain.image.Image;
import TubeSlice.tubeSlice.domain.videoKeyword.VideoKeyword;
import TubeSlice.tubeSlice.domain.subtitle.Subtitle;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "video")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String title;

    private String url;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<VideoKeyword> videoKeywordList = new ArrayList<>();

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Subtitle> subtitleList = new ArrayList<>();


}
