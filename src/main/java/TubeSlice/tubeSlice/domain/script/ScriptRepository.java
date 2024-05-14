package TubeSlice.tubeSlice.domain.script;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScriptRepository extends JpaRepository<Script, Long> {

    Optional<Script> findByVideoUrl(String url);
}
