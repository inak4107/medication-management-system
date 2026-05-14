package pilltapserver.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 회원 가입 관련 에러
    ERROR_DUPLICATE_LOGIN_ID(400, "이미 사용 중인 아이디입니다."),
    ERROR_LOGIN_ID_FORMAT(400,"아이디의 형식이 올바르지 않습니다."),
    ERROR_LOGIN_ID_EMPTY(400,"아이디를 입력하세요."),
    ERROR_PASSWORD_LENGTH(400,"비밀번호는 8~16자 이내여야합니다."),
    ERROR_PASSWORD_MISMATCH(400,"비밀번호가 일치하지 않습니다."),
    ERROR_PASSWORD_EMPTY(400,"비밀번호를 입력하세요."),
    ERROR_PASSWORD_CONFIRM_EMPTY(400,"비밀번호 확인이 필요합니다."),
    ERROR_NAME_EMPTY(400,"이름을 입력하세요."),
    ERROR_EMAIL_EMPTY(400,"이메일을 입력하세요."),
    ERROR_DUPLICATE_EMAIL(400,"이미 가입된 이메일입니다."),
    ERROR_EMAIL_FORMAT(400,"이메일 형식이 올바르지 않습니다."),
    ERROR_BIRTH_DATE_EMPTY(400,"생년월일을 입력하세요."),
    ERROR_ACCOUNT_TYPE_EMPTY(400,"유저 유형을 입력하세요."),
    //로그인 및 유저 관련 에러
    ERROR_USER_NOT_FOUND(401, "가입되지 않은 아이디입니다."),
    ERROR_USER_DELETED(401,"가입되지 않은 아이디입니다."),
    ERROR_INVALID_PASSWORD(401, "비밀번호가 일치하지 않습니다."),
    ERROR_NAME_TOO_LONG(400,"이름은 최대 20자까지 가능합니다."),
    //하드웨어 관련 에러
    ERROR_DEVICE_CODE_EMPTY(400,"기기 식별 코드가 누락되었습니다."),
    ERROR_DEVICE_ALREADY_REGISTERED(409,"이미 등록된 기기입니다."),
    ERROR_UNAUTHORIZED_ACCESS(403,"해당 기기에 대한 접근 권한이 없습니다."),
    ERROR_DEVICE_NOT_FOUND(404,"존재하지 않거나 삭제된 기기 정보입니다."),
    //서버 에러
    INTERNAL_SERVER_ERROR(500, "서버 내 오류가 발생했습니다.");

    private final int status;
    private final String message;

    ErrorCode(int status,String message){
        this.status=status;
        this.message=message;
    }
}
