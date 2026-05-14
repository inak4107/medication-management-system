package pilltapserver.domain.hardware;

import pilltapserver.global.exception.CustomException;
import pilltapserver.global.exception.ErrorCode;

/**
 *
 * @param deviceCode
 * @param deviceName
 * @param hardwareId
 */
public record HardwareDto(
        String deviceCode,
        String deviceName,
        Integer hardwareId
) {
    public HardwareDto {
        // 1. 기기 코드(NFC) 검증
        if (deviceCode == null || deviceCode.isBlank()) {
            throw new CustomException(ErrorCode.ERROR_DEVICE_CODE_EMPTY);
        }

        // 2. 기기 별명 길이 검증 (DBA 민지가 설정한 VARCHAR(50) 준수)
        if (deviceName != null && deviceName.length() > 20) {
            throw new CustomException(ErrorCode.ERROR_NAME_TOO_LONG);
        }
    }
}