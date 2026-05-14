package pilltapserver.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pilltapserver.global.exception.CustomException;
import pilltapserver.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * 유저 엔터티 생성 및 저장
     * @param user 신규 사용자 정보
     */
    @Transactional
    public void create(User user){
        userRepository.save(user);
    }

    /**
     * 현재 로그인한 사용자의 마이페이지 정보(UserDto)를 조회
     * @param loginId 로그인 아이디
     * @return UserDto 객체
     */
    @Transactional(readOnly = true)
    public UserDto getMyInfo(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_USER_NOT_FOUND));
        return new UserDto(
                user.getName(),
                user.getEmail(),
                user.getBirthDate(),
                user.getAccountType(),
                user.getLoginId()
        );
    }

    /**
     * 마이페이지 내 정보 수정 (이름, 이메일, 생년월일, 계정타입)
     * @param loginId 로그인 아이디
     * @param updateDto 수정할 정보가 담긴 객체
     */
    @Transactional
    public void updateMyInfo(String loginId, UserDto updateDto) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_USER_NOT_FOUND));
        user.updateProfile(
                updateDto.name(),
                updateDto.email(),
                updateDto.birthDate(),
                updateDto.accountType()
        );
    }
}