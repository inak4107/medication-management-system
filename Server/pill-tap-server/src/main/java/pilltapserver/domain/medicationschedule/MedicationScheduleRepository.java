package pilltapserver.domain.medicationschedule;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicationScheduleRepository extends JpaRepository<MedicationSchedule, Integer>{
    List<MedicationSchedule> findAllByUserId(Integer userId);
}
