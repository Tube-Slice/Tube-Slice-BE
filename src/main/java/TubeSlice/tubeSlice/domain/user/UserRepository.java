package TubeSlice.tubeSlice.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByLoginId(String loginId);

    Optional<User> findById(long userId);

    Optional<User> findByEmail(String email);

}
