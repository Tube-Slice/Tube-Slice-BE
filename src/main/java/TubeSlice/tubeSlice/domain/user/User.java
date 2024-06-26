package TubeSlice.tubeSlice.domain.user;

import TubeSlice.tubeSlice.domain.comment.Comment;
import TubeSlice.tubeSlice.domain.follow.Follow;
import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.postLike.PostLike;
import TubeSlice.tubeSlice.domain.userScript.UserScript;
import TubeSlice.tubeSlice.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "login_status = 'ACTIVATE'")
@SQLDelete(sql =  "UPDATE user  SET login_status = 'INACTIVATE' WHERE id = ?")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private String nickname;

    private String email;

    private String loginId;

    private String profileUrl;

    private String introduction;

    private String socialType;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PostLike> postLikeList = new ArrayList<>();
}
