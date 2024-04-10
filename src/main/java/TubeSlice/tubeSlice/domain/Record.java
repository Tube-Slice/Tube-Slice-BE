package TubeSlice.tubeSlice.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "record")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "video_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Video video;

    @JoinColumn(name = "user_script_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserScript userScript;


}
