package pilltapserver.domain.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pilltapserver.global.common.ApiResponse;

/**
 * 마이페이지 조회 및 정보 수정을 처리
 */
@Tag(name = "User", description = "유저 마이페이지 관련 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 마이페이지 조회
     * @param loginId 인증된 사용자의 로그인 아이디
     * @return 조회된 유저의 상세 프로필 정보
     */
    @Operation(summary = "마이페이지 조회", description = "현재 로그인한 유저의 상세 정보를 조회합니다.")
    @GetMapping("/me/profile")
    public ResponseEntity<ApiResponse<UserDto>> getMyPage(
            @AuthenticationPrincipal String loginId
    ) {
        UserDto response = userService.getMyInfo(loginId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 정보 수정
     * @param loginId 인증된 사용자의 로그인 아이디
     * @param request 수정할 새로운 유저 정보
     * @return 정보 수정 완료 안내 메시지
     */
    @Operation(summary = "정보 수정", description = "유저의 이름, 이메일, 생년월일, 계정 타입을 수정합니다.")
    @PatchMapping("/me/update")
    public ResponseEntity<ApiResponse<Void>> updateMyPage(
            @AuthenticationPrincipal String loginId,
            @RequestBody UserDto request
    ) {
        userService.updateMyInfo(loginId, request);
        return ResponseEntity.ok(ApiResponse.success("회원 정보가 수정되었습니다."));
    }
}