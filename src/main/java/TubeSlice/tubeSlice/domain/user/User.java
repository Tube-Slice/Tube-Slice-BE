package TubeSlice.tubeSlice.domain.user;

import TubeSlice.tubeSlice.domain.follow.Follow;
import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.userScript.UserScript;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    private String socialType;

    private String introduction;

    private String profileUrl;

    @Enumerated(EnumType.STRING)
    private Status loginStatus;

    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserScript> userScriptList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Follow> followingList = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Follow> followerList = new ArrayList<>();


}
