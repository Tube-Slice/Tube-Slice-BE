package TubeSlice.tubeSlice.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "keyword")
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;


    @OneToMany(mappedBy = "keyword")
    private List<VideoKeyword> videoKeyword;
}
