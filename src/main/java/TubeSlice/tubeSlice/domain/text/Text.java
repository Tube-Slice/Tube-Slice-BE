package TubeSlice.tubeSlice.domain.text;

import TubeSlice.tubeSlice.domain.userScript.UserScript;
import TubeSlice.tubeSlice.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "text")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Text extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Double timeline;

    @Column(length = 300)
    private String text;

    private Boolean highlight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_script_id")
    private UserScript userScript;
}
