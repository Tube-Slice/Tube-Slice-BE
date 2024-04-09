package TubeSlice.tubeSlice.domain;

import jakarta.persistence.*;

public class Record {

    @Id
    @GeneratedValue
    @Column(name = "record_id")
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
