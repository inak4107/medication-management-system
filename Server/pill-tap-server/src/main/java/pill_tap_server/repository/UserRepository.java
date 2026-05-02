package pill_tap_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pill_tap_server.domain.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    // loginId 기반으로 정보를 가져오기 위한 기능 추가
    Optional<User> findByLoginId(String loginId);
}
