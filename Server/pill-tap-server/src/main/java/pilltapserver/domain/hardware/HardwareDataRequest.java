package pilltapserver.domain.hardware;

import pilltapserver.global.exception.CustomException;
import pilltapserver.global.exception.ErrorCode;

/**
 * 하드웨어가 서버로 센싱 데이터를 전송할 때 사용하는 DTO
 * @param deviceCode 기기 고유 식별 코드
 * @param nfcTag  감지된 NFC 태그 UID
 * @param weight 현재 측정된 무게 값
 */
public record HardwareDataRequest(
        String deviceCode,
        String nfcTag,
        Double weight
) {
    public HardwareDataRequest {
        if (deviceCode == null || deviceCode.isBlank()) {
            throw new CustomException(ErrorCode.ERROR_DEVICE_CODE_EMPTY);
        }
    }
}