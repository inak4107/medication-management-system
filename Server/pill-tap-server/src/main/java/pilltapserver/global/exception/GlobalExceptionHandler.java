package pilltapserver.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pilltapserver.global.common.ApiResponse;

@Slf4j
@RestControllerAdvice
// 예상 가능한 오류
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("CustomException status: {}, message: {}", errorCode.getStatus(), errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode.getStatus(), errorCode.getMessage()));
    }
    //예기치 못한 오류
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("[InternalServerError] message: {}", e.getMessage(), e);
        return ResponseEntity.status(500)
                .body(ApiResponse.error(500, "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
    }

}
