package TubeSlice.tubeSlice.domain;

import jakarta.persistence.*;

import java.util.List;

public class Keyword {

    @Id @GeneratedValue
    @Column(name = "keyword_id")
    private Long id;

    private String name;


    @OneToMany(mappedBy = "keyword")
    private List<VideoKeyword> videoKeyword;
}
