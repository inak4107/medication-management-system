package pilltapserver.domain.medicationschedule;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pilltapserver.global.common.ApiResponse;
import java.util.List;

/**
 * 복약 스케줄 등록, 조회, 수정, 삭제를 처리
 */
@Tag(name = "MedicationSchedule", description = "복약 스케줄 관련 API")
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class MedicationScheduleController {
    private final MedicationScheduleService medicationScheduleService;

    /**
     * 스케줄 등록
     * @param loginId 인증된 사용자의 로그인 아이디
     * @param request 등록할 새로운 스케줄 정보
     * @return 스케줄 등록 완료 안내 메시지
     */
    @Operation(summary = "스케줄 등록", description = "새로운 복약 스케줄을 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registerSchedule(
            @AuthenticationPrincipal String loginId,
            @RequestBody MedicationScheduleDto.Request request
    ) {
        medicationScheduleService.registerSchedule(request, loginId);
        return ResponseEntity.ok(ApiResponse.success("복약 스케줄이 등록되었습니다."));
    }

    /**
     * 스케줄 조회
     * @param loginId 인증된 사용자의 로그인 아이디
     * @return 조회된 스케줄 목록
     */
    @Operation(summary = "스케줄 조회", description = "현재 로그인한 유저의 전체 복약 스케줄 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicationScheduleDto.Response>>> getSchedules(
            @AuthenticationPrincipal String loginId
    ) {
        List<MedicationScheduleDto.Response> response = medicationScheduleService.readSchedule(loginId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 스케줄 수정
     * @param scheduleId 수정할 스케줄 고유 번호 (pk)
     * @param loginId 인증된 사용자의 로그인 아이디
     * @param request 수정할 새로운 스케줄 정보
     * @return 스케줄 수정 완료 안내 메시지
     */
    @Operation(summary = "스케줄 수정", description = "특정 복약 스케줄의 이름, 태그 UID, 시간 목록을 수정합니다.")
    @PutMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<Void>> updateSchedule(
            @PathVariable Integer scheduleId,
            @AuthenticationPrincipal String loginId,
            @RequestBody MedicationScheduleDto.Request request
    ) {
        medicationScheduleService.updateSchedule(scheduleId, request, loginId);
        return ResponseEntity.ok(ApiResponse.success("복약 스케줄이 수정되었습니다."));
    }

    /**
     * 스케줄 삭제
     * @param scheduleId 삭제할 스케줄 고유 번호 (pk)
     * @param loginId 인증된 사용자의 로그인 아이디
     * @return 스케줄 삭제 완료 안내 메시지
     */
    @Operation(summary = "스케줄 삭제", description = "특정 복약 스케줄을 삭제합니다.")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<Void>> deleteSchedule(
            @PathVariable Integer scheduleId,
            @AuthenticationPrincipal String loginId
    ) {
        medicationScheduleService.deleteSchedule(scheduleId, loginId);
        return ResponseEntity.ok(ApiResponse.success("복약 스케줄이 삭제되었습니다."));
    }
}