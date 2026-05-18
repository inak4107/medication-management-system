package pilltapserver.domain.hardware;

import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(hidden = true)
        Integer hardwareId
) {
    public HardwareDto {
        // 기기 코드(NFC) 검증
        if (deviceCode == null || deviceCode.isBlank()) {
            throw new CustomException(ErrorCode.ERROR_DEVICE_CODE_EMPTY);
        }

        // device_name 검증
        if (deviceName != null && deviceName.length() > 20) {
            throw new CustomException(ErrorCode.ERROR_NAME_TOO_LONG);
        }
    }
}