package TubeSlice.tubeSlice.domain.subtitle;

import TubeSlice.tubeSlice.domain.script.Script;
import TubeSlice.tubeSlice.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "subtitle")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subtitle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String subtitle;

    private Double timeline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "script_id")
    private Script script;
}
