package pill_tap_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pill_tap_server.domain.MedicationSchedule;

public interface MedicationScheduleRepository extends JpaRepository<MedicationSchedule, Long>{
}
