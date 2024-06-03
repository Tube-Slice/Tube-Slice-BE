package TubeSlice.tubeSlice.domain.userScript;


import TubeSlice.tubeSlice.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserScriptRepository extends JpaRepository<UserScript, Long> {

    List<UserScript> findAllByUser(User user);
}
