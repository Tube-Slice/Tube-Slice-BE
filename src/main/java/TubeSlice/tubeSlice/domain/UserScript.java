package TubeSlice.tubeSlice.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

public class UserScript {

    @Id
    @GeneratedValue
    @Column(name = "userscript_id")
    private Long id;

    private LocalDateTime timeline;

    private String script;

    @OneToMany(mappedBy = "userscript", cascade = CascadeType.ALL)
    private List<Record> records;
}
