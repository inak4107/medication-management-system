package pilltapserver.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pilltapserver.domain.user.User;
import pilltapserver.domain.user.UserRepository;
import pilltapserver.domain.user.UserService;
import pilltapserver.global.exception.CustomException;
import pilltapserver.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // 실시간 개별 중복 체크
    @Transactional(readOnly = true)
    public void checkDuplicateId(String loginId){
        if (userRepository.existsByLoginId(loginId)){
            throw new CustomException(ErrorCode.ERROR_DUPLICATE_LOGIN_ID);
        }
    }
    @Transactional(readOnly = true)
    public void checkDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.ERROR_DUPLICATE_EMAIL);
        }
    }
    /**
     * 최종 회원가입 로직
     * 유저 아이디와 이메일 재검증
     * 가입 데이터 생성
     */
    @Transactional
    public void userRegister(RegisterRequest request) {
        checkDuplicateId(request.loginId());
        checkDuplicateEmail(request.email());
        User user = User.builder()
                .loginId(request.loginId())
                .name(request.name())
                .email(request.email())
                .birthDate(request.birthDate())
                .accountType(request.accountType())
                .password(passwordEncoder.encode(request.password()))
                .build();
        userService.create(user);
    }

}
