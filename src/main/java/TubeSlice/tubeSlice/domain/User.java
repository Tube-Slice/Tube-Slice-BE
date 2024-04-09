package TubeSlice.tubeSlice.domain;

import jakarta.persistence.*;

import java.util.List;

public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String name;

    private String email;

    private String loginId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Record> records;

    @Enumerated(EnumType.STRING)
    private SocialLogin socialLogin;

    @Enumerated(EnumType.STRING)
    private LoginStatus loginStatus;

}
