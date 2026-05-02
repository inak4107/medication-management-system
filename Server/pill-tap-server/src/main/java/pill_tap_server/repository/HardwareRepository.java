package pill_tap_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pill_tap_server.domain.Hardware;

public interface HardwareRepository extends JpaRepository<Hardware, Long>{
}
