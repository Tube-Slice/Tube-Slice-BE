package TubeSlice.tubeSlice.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private String email;

    private String loginId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Record> records;

    @Enumerated(EnumType.STRING)
    private SocialType socialLogin;

    @Enumerated(EnumType.STRING)
    private Status loginStatus;

}
