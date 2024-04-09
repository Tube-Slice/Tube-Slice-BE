package TubeSlice.tubeSlice.domain;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

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
