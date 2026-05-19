package pilltapserver.domain.scheduleTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScheduleTimeRepository extends JpaRepository<ScheduleTime, Integer> {

    @Query("SELECT s FROM ScheduleTime s WHERE s.medicationSchedule.scheduleId = :scheduleId")
    List<ScheduleTime> findAllByScheduleId(@Param("scheduleId") Integer scheduleId);

    @Modifying
    @Query("DELETE FROM ScheduleTime s WHERE s.medicationSchedule.scheduleId = :scheduleId")
    void deleteAllByScheduleId(@Param("scheduleId") Integer scheduleId);
}