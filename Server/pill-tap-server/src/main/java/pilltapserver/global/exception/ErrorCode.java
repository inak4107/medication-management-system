package pilltapserver.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    //로그인 및 유저 관련 에러
    USER_NOT_FOUND(404, "가입되지 않은 아이디입니다."),
    INVALID_PASSWORD(400, "비밀번호가 일치하지 않습니다."),
    //서버 에러
    INTERNAL_SERVER_ERROR(500, "서버 내 오류가 발생했습니다.");

    private final int status;
    private final String message;

    ErrorCode(int status,String message){
        this.status=status;
        this.message=message;
    }
}
