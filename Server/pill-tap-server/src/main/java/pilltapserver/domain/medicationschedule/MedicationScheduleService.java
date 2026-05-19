package pilltapserver.domain.medicationschedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pilltapserver.domain.hardware.HardwareRepository;
import pilltapserver.domain.scheduleTime.ScheduleTimeService;
import pilltapserver.domain.scheduleTime.ScheduleTimeRequest;
import pilltapserver.domain.tagmapping.TagMapping;
import pilltapserver.domain.tagmapping.TagMappingService;
import pilltapserver.domain.user.User;
import pilltapserver.domain.user.UserRepository;
import pilltapserver.domain.scheduleTime.ScheduleTime;
import pilltapserver.domain.scheduleTime.ScheduleTimeDto;
import pilltapserver.global.exception.CustomException;
import pilltapserver.global.exception.ErrorCode;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MedicationScheduleService {
    private final UserRepository userRepository;
    private final ScheduleTimeService scheduleTimeService;
    private final HardwareRepository hardwareRepository;
    private final TagMappingService tagMappingService;
    private final MedicationScheduleRepository medicationScheduleRepository;

    /**
     * 스케줄 등록
     * @param request 사용자가 등록한 스케줄 이름, 시간, 요일
     * @param loginId 인증된 사용자의 로그인 아이디
     */
    @Transactional
    public void registerSchedule(MedicationScheduleDto.Request request, String loginId) {
        User user = getUserByLoginId(loginId);
        Integer userId = user.getUserId();

        String receivedUid = request.tagUid();
        TagMapping savedMapping = null;

        if (receivedUid != null) {
            savedMapping = tagMappingService.registerTag(receivedUid, userId);
        }

        MedicationSchedule schedule = MedicationSchedule.builder()
                .userId(userId)
                .mappingId(savedMapping != null ? savedMapping.getMappingId() : null)
                .scheduleName(request.scheduleName())
                .build();
        MedicationSchedule savedSchedule = medicationScheduleRepository.save(schedule);

        List<ScheduleTimeRequest> timeRequests = new ArrayList<>();
        for (ScheduleTimeDto dto : request.scheduleTimeRequests()) {
            timeRequests.add(new ScheduleTimeRequest(dto.takeTime(), dto.daysOfWeek()));
        }

        scheduleTimeService.registerTime(savedSchedule.getScheduleId(), timeRequests);
    }

    /**
     * 스케줄 조회
     * @param loginId 인증된 사용자의 로그인 아이디
     * @return 사용자의 스케줄 List
     */
    @Transactional(readOnly = true)
    public List<MedicationScheduleDto.Response> readSchedule(String loginId) {
        User user = getUserByLoginId(loginId);
        List<MedicationSchedule> schedules = medicationScheduleRepository.findAllByUserId(user.getUserId());
        List<MedicationScheduleDto.Response> responses = new ArrayList<>();

        for (MedicationSchedule schedule : schedules) {
            List<ScheduleTimeDto> scheduleTimeRequestList = new ArrayList<>();
            for (ScheduleTime time : schedule.getScheduleTimes()) {
                scheduleTimeRequestList.add(new ScheduleTimeDto(
                        time.getTimeId(),
                        time.getTakeTime(),
                        time.getDaysOfWeek()
                ));
            }
            responses.add(new MedicationScheduleDto.Response(
                    schedule.getScheduleId(),
                    schedule.getScheduleName(),
                    scheduleTimeRequestList
            ));
        }

        return responses;
    }

    /**
     * 스케줄 수정
     * @param scheduleId 수정할 스케줄 고유 번호 (pk)
     * @param request 사용자가 변경할 스케줄 이름, 태그 UID, 시간 List
     * @param loginId 인증된 사용자의 로그인 아이디
     */
    @Transactional
    public void updateSchedule(Integer scheduleId, MedicationScheduleDto.Request request, String loginId) {
        User user = getUserByLoginId(loginId);

        MedicationSchedule schedule = medicationScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_SCHEDULE_NOT_FOUND));

        if (!schedule.getUserId().equals(user.getUserId())) {
            throw new CustomException(ErrorCode.ERROR_UNAUTHORIZED_ACCESS);
        }

        Integer updatedMappingId = tagMappingService.updateTagMapping(schedule.getMappingId(), request.tagUid(), user.getUserId());
        schedule.updateSchedule(request.scheduleName(), updatedMappingId);
        scheduleTimeService.updateTime(scheduleId, request.scheduleTimeRequests());
    }

    /**
     * 스케줄 삭제
     * @param scheduleId 삭제할 스케줄 고유 번호 (pk)
     * @param loginId 인증된 사용자의 로그인 아이디
     */
    @Transactional
    public void deleteSchedule(Integer scheduleId, String loginId) {
        User user = getUserByLoginId(loginId);

        MedicationSchedule schedule = medicationScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_SCHEDULE_NOT_FOUND));

        if (!schedule.getUserId().equals(user.getUserId())) {
            throw new CustomException(ErrorCode.ERROR_UNAUTHORIZED_ACCESS);
        }

        scheduleTimeService.deleteAllByScheduleId(scheduleId);
        tagMappingService.updateTagMapping(schedule.getMappingId(), null, user.getUserId());
        schedule.deactivate();
    }

    private User getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_USER_NOT_FOUND));
    }
}