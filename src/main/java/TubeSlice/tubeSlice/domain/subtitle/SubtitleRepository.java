package TubeSlice.tubeSlice.domain.subtitle;

import TubeSlice.tubeSlice.domain.script.Script;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubtitleRepository extends JpaRepository<Subtitle, Long> {

    Subtitle findByScript(Script script);
}
