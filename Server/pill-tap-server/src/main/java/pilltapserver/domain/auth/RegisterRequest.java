package pilltapserver.domain.auth;

import pilltapserver.global.exception.CustomException;
import pilltapserver.global.exception.ErrorCode;

import java.time.LocalDate;

/**
 * 프론트엔드에서 넘어오는 회원가입 데이터를 담는 DTO
 * Record의 컴팩트 생성자를 이용해 입력값의 유효성 1차 검증
 */
public record RegisterRequest(
    String loginId,
    String password,
    String passwordConfirm,
    String name,
    String email,
    LocalDate birthDate,
    Integer accountType
){
    public RegisterRequest{
        //아이디 검증
        if (loginId==null||loginId.isBlank()){
            throw new CustomException(ErrorCode.ERROR_LOGIN_ID_EMPTY);
        }
        if (!loginId.matches("^[a-zA-Z0-9]{4,20}$")) {
            throw new CustomException(ErrorCode.ERROR_LOGIN_ID_FORMAT);
        }
        //비밀번호 검증
        if (password==null||password.isBlank()){
            throw new CustomException(ErrorCode.ERROR_PASSWORD_EMPTY);
        }
        if (password.length()<8||password.length()>16){
            throw new CustomException(ErrorCode.ERROR_PASSWORD_LENGTH);
        }
        //비밀번호 확인 검증
        if (passwordConfirm==null||passwordConfirm.isBlank()){
            throw new CustomException(ErrorCode.ERROR_PASSWORD_CONFIRM_EMPTY);
        }
        if (!password.equals(passwordConfirm)) {
            throw new CustomException(ErrorCode.ERROR_PASSWORD_MISMATCH);
        }
        //기타 필수 값 검증
        if (name==null||name.isBlank()){
            throw new CustomException(ErrorCode.ERROR_NAME_EMPTY);
        }
        if (email==null||email.isBlank()){
            throw new CustomException(ErrorCode.ERROR_EMAIL_EMPTY);
        }
        if (!email.matches(
                " \"^[a-zA-Z0-9_+&*-]+(?:\\\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,}$\";")){
            throw new CustomException(ErrorCode.ERROR_EMAIL_FORMAT);
        }
        if (birthDate==null){
            throw new CustomException(ErrorCode.ERROR_BIRTH_DATE_EMPTY);
        }
        if (accountType==null){
            throw new CustomException(ErrorCode.ERROR_ACCOUNT_TYPE_EMPTY);
        }
    }
}
