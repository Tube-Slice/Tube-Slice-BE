package TubeSlice.tubeSlice.domain.script;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ScriptRepository extends JpaRepository<Script, Long> {

    Script findByVideoUrl(String url);

}
