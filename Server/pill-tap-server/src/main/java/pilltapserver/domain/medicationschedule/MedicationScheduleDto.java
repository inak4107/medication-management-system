package pilltapserver.domain.medicationschedule;

import pilltapserver.domain.scheduleTime.ScheduleTimeDto;
import java.util.List;

public class MedicationScheduleDto {

    public record Request(
            String scheduleName,
            String tagUid,
            List<ScheduleTimeDto> scheduleTimeRequests
    ) {}

    public record Response(
            Integer scheduleId,
            String scheduleName,
            List<ScheduleTimeDto> scheduleTimeRequestList
    ) {}
}