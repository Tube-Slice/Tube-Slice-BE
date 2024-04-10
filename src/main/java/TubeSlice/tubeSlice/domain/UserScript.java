package TubeSlice.tubeSlice.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_script")
public class UserScript {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private LocalDateTime timeline;

    private String script;

    @OneToMany(mappedBy = "userScript", cascade = CascadeType.ALL)
    private List<Record> records;
}
