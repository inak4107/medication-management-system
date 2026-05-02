package pill_tap_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pill_tap_server.domain.MedicationLog;

public interface MedicationLogRepository extends JpaRepository<MedicationLog, Long>{
}
