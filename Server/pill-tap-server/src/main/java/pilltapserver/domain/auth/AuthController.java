package pilltapserver.domain.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pilltapserver.global.common.ApiResponse;

/**
 * 인증 및 회원가입 관련 HTTP 요청을 처리
 */
@Tag(name = "Auth", description = "인증 관련 API (회원가입, 중복 체크 등)")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입", description = "신규 유저 등록")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody RegisterRequest request) {
        authService.userRegister(request);
        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다."));
    }
    @Operation(summary = "아이디 중복 체크")
    @GetMapping("/check-id")
    public ResponseEntity<ApiResponse<Void>> checkId(@RequestParam String loginId) {
        authService.checkDuplicateId(loginId);
        return ResponseEntity.ok(ApiResponse.success("사용 가능한 아이디입니다."));
    }
    @Operation(summary = "이메일 중복 체크")
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Void>> checkEmail(@RequestParam String email) {
        authService.checkDuplicateEmail(email);
        return ResponseEntity.ok(ApiResponse.success("사용 가능한 이메일입니다."));
    }
}
