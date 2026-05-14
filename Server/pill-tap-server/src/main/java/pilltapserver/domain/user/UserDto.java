package pilltapserver.domain.user;

import pilltapserver.global.exception.CustomException;
import pilltapserver.global.exception.ErrorCode;
import java.time.LocalDate;

/**
 * 유저 프로필 정보 조회 및 수정을 위한 데이터
 * @param name 사용자 이륾
 * @param email 사용자 이메일
 * @param birthDate 사용자 생년월일
 * @param accountType 사용자 계정 종류
 * @param loginId 사용자 고유 아이디
 */
public record UserDto(
        String name,
        String email,
        LocalDate birthDate,
        Integer accountType,
        String loginId
) {
    public UserDto {
        // 이름 검증
        if (name == null || name.isBlank()) {
            throw new CustomException(ErrorCode.ERROR_NAME_EMPTY);
        }
        if (name.length() > 50) {
            throw new CustomException(ErrorCode.ERROR_NAME_TOO_LONG);
        }

        // 이메일 검증
        if (email == null || !email.matches(
                "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$")) {
            throw new CustomException(ErrorCode.ERROR_EMAIL_FORMAT);
        }

        // 필수 값 검증
        if (birthDate == null) {
            throw new CustomException(ErrorCode.ERROR_BIRTH_DATE_EMPTY);
        }
        if (accountType == null) {
            throw new CustomException(ErrorCode.ERROR_ACCOUNT_TYPE_EMPTY);
        }
    }
}