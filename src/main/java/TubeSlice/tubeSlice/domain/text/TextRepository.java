package TubeSlice.tubeSlice.domain.text;

import TubeSlice.tubeSlice.domain.userScript.UserScript;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TextRepository extends JpaRepository<Text, Long> {

    Text findByText(String text);
    Text findAllByUserScriptAndTimeline(UserScript userScript, Double timeline);

}
