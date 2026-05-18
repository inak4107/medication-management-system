package pilltapserver.domain.scheduleTime;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pilltapserver.domain.medicationschedule.MedicationSchedule;
import pilltapserver.domain.medicationschedule.MedicationScheduleRepository;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleTimeService {
    private final ScheduleTimeRepository scheduleTimeRepository;
    private final MedicationScheduleRepository medicationScheduleRepository;

    @Transactional
    public void registerTime(Integer scheduleId, List<ScheduleTimeRequest> timeRequest){
        MedicationSchedule schedule = medicationScheduleRepository.getReferenceById(scheduleId);

        List<ScheduleTime> scheduleTimes = new ArrayList<>();
        for(ScheduleTimeRequest request : timeRequest){
            ScheduleTime time = ScheduleTime.builder()
                    .medicationSchedule(schedule)
                    .takeTime(request.takeTime())
                    .daysOfWeek(request.dayOfWeek())
                    .build();
            scheduleTimes.add(time);
        }
        scheduleTimeRepository.saveAll(scheduleTimes);
    }
}