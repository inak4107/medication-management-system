package pilltapserver.domain.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(description = "로그인 아이디",example="tester001")
        String loginId,
        @Schema(description = "비밀번호",example="testpassword")
        String password
){
}
