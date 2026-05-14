package pilltapserver.domain.hardware;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pilltapserver.global.common.ApiResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hardwares")
public class HardwareController {
    private final HardwareService hardwareService;

    /**
     * 기기 등록
     * @param loginId 현재 등록을 요청한 아이디
     * @param dto 등록할 기기의 정보(사용자, 식별 코드, 별칭)
     * @return 기기 등록 프로세스 완료 메시지를 포함한 표준 응답 객체
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> registerDevice(
            @AuthenticationPrincipal String loginId, // 현재 로그인한 유저의 ID
            @RequestBody HardwareDto dto) {
        hardwareService.registerDevice(loginId, dto);
        return ResponseEntity.ok(ApiResponse.success("기기 등록이 완료되었습니다."));
    }
    /**
     * 기기 목록 조회
     * @param loginId 현재 조회를 요청한 아이디
     * @return 유저가 보유한 모든 기기 정보(ID, 코드, 별칭) 리스트
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<HardwareDto>>> getMyDevices(
            @AuthenticationPrincipal String loginId) {
        List<HardwareDto> devices = hardwareService.getDeviceList(loginId);
        return ResponseEntity.ok(ApiResponse.success(devices));
    }

    /**
     * 기기 정보(이름) 수정
     * @param loginId 현재 수정을 요청한 아이디
     * @param dto 기기의 식별코드와 새로운 별칭
     * @return 기기 정보 변경 성공 메시지를 포함한 표준 응답 객체
     */
    @PatchMapping("/update")
    public ResponseEntity<ApiResponse<Void>> updateDevice(
            @AuthenticationPrincipal String loginId,
            @RequestBody HardwareDto dto) {
        hardwareService.updateDevice(loginId, dto);
        return ResponseEntity.ok(ApiResponse.success("기기 정보가 수정되었습니다."));
    }
}