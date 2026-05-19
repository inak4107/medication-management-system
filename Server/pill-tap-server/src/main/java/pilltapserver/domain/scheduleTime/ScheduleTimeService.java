package pilltapserver.domain.scheduleTime;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pilltapserver.domain.medicationschedule.MedicationSchedule;
import pilltapserver.domain.medicationschedule.MedicationScheduleRepository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleTimeService {
    private final ScheduleTimeRepository scheduleTimeRepository;
    private final MedicationScheduleRepository medicationScheduleRepository;

    /**
     * 시간 등록
     * @param scheduleId 스케줄 고유 번호 (pk)
     * @param timeRequest 스케줄에 해당 하는 각각의 시간
     */
    @Transactional
    public void registerTime(Integer scheduleId, List<ScheduleTimeRequest> timeRequest){
        MedicationSchedule schedule = medicationScheduleRepository.getReferenceById(scheduleId);

        List<ScheduleTime> scheduleTimes = timeRequest.stream().map(request ->
                ScheduleTime.builder()
                        .medicationSchedule(schedule)
                        .takeTime(request.takeTime())
                        .daysOfWeek(request.dayOfWeek())
                        .build()
        ).collect(Collectors.toList());

        scheduleTimeRepository.saveAll(scheduleTimes);
    }

    /**
     * 복약 시간 수정 (삭제, 추가, 변경)
     * @param scheduleId 부모 스케줄 고유 번호 (pk)
     * @param timeRequests 프론트에서 보낸 변경될 시간 List
     */
    @Transactional
    public void updateTime(Integer scheduleId, List<ScheduleTimeDto> timeRequests) {
        MedicationSchedule schedule = medicationScheduleRepository.getReferenceById(scheduleId);

        // 기존 DB에 저장된 시간들을 Map으로 변환하여 관리
        Map<Integer, ScheduleTime> existingTimeMap = scheduleTimeRepository.findAllByScheduleId(scheduleId)
                .stream().collect(Collectors.toMap(ScheduleTime::getTimeId, Function.identity()));

        // 새로운 시간 추가 및 기존 시간 수정
        for (ScheduleTimeDto request : timeRequests) {
            if (request.timeId() != null && existingTimeMap.containsKey(request.timeId())) {
                // timeId가 있는 경우, 기존 엔티티 값 수정
                ScheduleTime existingTime = existingTimeMap.get(request.timeId());
                existingTime.updateTimeAndDays(request.takeTime(), request.daysOfWeek());

                // 수정 완료된 엔티티는 Map에서 제거
                existingTimeMap.remove(request.timeId());
            } else {
                // timeId가 없는 경우, 새로 추가
                ScheduleTime newTime = ScheduleTime.builder()
                        .medicationSchedule(schedule)
                        .takeTime(request.takeTime())
                        .daysOfWeek(request.daysOfWeek())
                        .build();
                scheduleTimeRepository.save(newTime);
            }
        }

        // 일부 시간 삭제
        // 요청 리스트에 포함되지 않은(Map에 남아있는) 엔티티들은 삭제
        if (!existingTimeMap.isEmpty()) {
            scheduleTimeRepository.deleteAll(existingTimeMap.values());
        }
    }

    /**
     * 스케줄 삭제 시 연관된 전체 시간 삭제
     * @param scheduleId 부모 스케줄 고유 번호 (pk)
     */
    @Transactional
    public void deleteAllByScheduleId(Integer scheduleId) {
        scheduleTimeRepository.deleteAllByScheduleId(scheduleId);
    }
}