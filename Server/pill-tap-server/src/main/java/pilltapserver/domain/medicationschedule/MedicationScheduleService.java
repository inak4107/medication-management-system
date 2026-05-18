package pilltapserver.domain.medicationschedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pilltapserver.domain.hardware.HardwareRepository;
import pilltapserver.domain.scheduleTime.ScheduleTimeService;
import pilltapserver.domain.tagmapping.TagMapping;
import pilltapserver.domain.tagmapping.TagMappingService;
import pilltapserver.domain.user.User;
import pilltapserver.domain.user.UserRepository;
import pilltapserver.global.exception.CustomException;
import pilltapserver.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class MedicationScheduleService {
    private final UserRepository userRepository;
    private final ScheduleTimeService scheduleTimeService;
    private final HardwareRepository hardwareRepository;
    private final TagMappingService tagMappingService;
    private final MedicationScheduleRepository medicationScheduleRepository;

    @Transactional
    public void registerSchedule(MedicationScheduleRequest request, Integer userId){
        // 유저 존재 여부 확인
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_USER_NOT_FOUND));

        // tagUid(약통과 연결된 NFC스티커)의 유뮤 확인
        String receivedUid = request.tagUid();
        TagMapping savedMapping = null;

        // tagUid와 연결할 경우
        if (receivedUid != null) {
            savedMapping = tagMappingService.registerTag(receivedUid, userId);
        }

        MedicationSchedule schedule = MedicationSchedule.builder()
                .userId(userId)
                .mappingId(savedMapping != null ? savedMapping.getMappingId() : null)
                .scheduleName(request.scheduleName())
                .build();
        MedicationSchedule savedSchedule = medicationScheduleRepository.save(schedule);

        // 복약 시간 등록
        scheduleTimeService.registerTime(savedSchedule.getScheduleId(), request.scheduleTimeRequests());
    }
}