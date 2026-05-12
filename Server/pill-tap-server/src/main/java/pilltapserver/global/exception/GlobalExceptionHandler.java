package pilltapserver.global.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import pilltapserver.global.common.ApiResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 예상 가능한 오류
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("CustomException status: {}, message: {}", errorCode.getStatus(), errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode.getStatus(), errorCode.getMessage()));
    }
    //DTO 변환 과정에서 발생하는 예외 처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Throwable mostSpecificCause = e.getMostSpecificCause();
        if (mostSpecificCause instanceof CustomException customException) {
            ErrorCode errorCode = customException.getErrorCode();
            log.warn("Jackson Parse CustomException status: {}, message: {}", errorCode.getStatus(), errorCode.getMessage());
            return ResponseEntity.status(errorCode.getStatus())
                    .body(ApiResponse.error(errorCode.getStatus(), errorCode.getMessage()));
        }
        // 괄호 누락, 타입 불일치 등
        log.warn("HttpMessageNotReadableException: {}", e.getMessage());
        return ResponseEntity.status(400)
                .body(ApiResponse.error(400, "잘못된 데이터 요청 형식입니다."));
    }
    //예기치 못한 오류
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("[InternalServerError] message: {}", e.getMessage(), e);
        return ResponseEntity.status(500)
                .body(ApiResponse.error(500, "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
    }

}
