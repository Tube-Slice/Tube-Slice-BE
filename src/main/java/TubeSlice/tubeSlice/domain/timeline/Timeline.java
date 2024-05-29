package TubeSlice.tubeSlice.domain.timeline;

import TubeSlice.tubeSlice.domain.post.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "timeline")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Timeline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable = false)
    private Integer startTime;

    @Column(nullable = false)
    private Integer endTime;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
