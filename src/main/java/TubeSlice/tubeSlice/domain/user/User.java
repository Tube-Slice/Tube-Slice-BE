package TubeSlice.tubeSlice.domain.user;

import TubeSlice.tubeSlice.domain.record.Record;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private String nickname;

    private String email;

    private String loginId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Record> records;

//    @Enumerated(EnumType.STRING)
//    private SocialType socialType;

    private String socialType;

    @Enumerated(EnumType.STRING)
    private Status loginStatus;

    private String role;
}
