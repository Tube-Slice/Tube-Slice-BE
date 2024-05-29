package TubeSlice.tubeSlice.domain.text;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TextRepository extends JpaRepository<Text, Long> {

    Text findByText(String text);

}
