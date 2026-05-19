package pilltapserver.domain.scheduleTime;

import java.time.LocalTime;

/**
 * 스케줄에 할당된 시간
 * @param timeId 각 시간의 고유 번호(pk)
 * @param takeTime 사용자가 설정한 시간
 * @param daysOfWeek 사용자가 설정한 요일
 */
public record ScheduleTimeDto(
        Integer timeId,
        LocalTime takeTime,
        Integer daysOfWeek
) {
}
