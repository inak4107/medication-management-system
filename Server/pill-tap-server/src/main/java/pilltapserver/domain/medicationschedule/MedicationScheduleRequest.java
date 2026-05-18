package pilltapserver.domain.medicationschedule;

import pilltapserver.domain.scheduleTime.ScheduleTimeRequest;
import pilltapserver.global.exception.CustomException;
import pilltapserver.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 사용자가 서버로 스케줄 데이터를 전송할 때 사용하는 DTO
 * @param scheduleName 사용자가 입력한 스케줄 이름
 * @param tagUid nfc 스티커 고유번호
 * @param scheduleTimeRequests 사용자가 입력한 스케줄 시간들
 */
@Schema(description = "복약 스케줄 등록 요청")
public record MedicationScheduleRequest(
        @Schema(description = "복약 스케줄 이름")
        String scheduleName,
        @Schema(description = "약통 고유 번호 (NFC 태그 UID)", nullable = true)
        String tagUid,
        // 다수의 스케줄 시간 고려
        @Schema(description = "설정할 복약 시간 리스트")
        List<ScheduleTimeRequest> scheduleTimeRequests
) {
    public MedicationScheduleRequest {
        if(scheduleName==null || scheduleName.isBlank()){
            throw new CustomException(ErrorCode.ERROR_SCHEDULE_NAME_EMPTY);
        }
        if(scheduleTimeRequests==null || scheduleTimeRequests.isEmpty()){
            throw new CustomException(ErrorCode.ERROR_SCHEDULE_TIME__EMPTY);
        }
    }
}
