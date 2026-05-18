package pilltapserver.domain.scheduleTime;

import java.time.LocalTime;

public record ScheduleTimeRequest(
        LocalTime takeTime,
        // 비트마스크 요일 (0~127)
        Integer dayOfWeek
) {
}
