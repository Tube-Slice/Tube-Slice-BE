package TubeSlice.tubeSlice.domain.userScript;

import TubeSlice.tubeSlice.domain.script.Script;
import TubeSlice.tubeSlice.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserScriptRepository extends JpaRepository<UserScript, Long> {

    UserScript findByUserAndScript(User user, Script script);
}
