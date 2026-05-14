package pilltapserver.domain.hardware;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pilltapserver.domain.user.User;
import pilltapserver.domain.user.UserRepository;
import pilltapserver.global.exception.CustomException;
import pilltapserver.global.exception.ErrorCode;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HardwareService {

    private final HardwareRepository hardwareRepository;
    private final UserRepository userRepository;

    /**
     * 기기 등록(소유주 변경 및 신규 등록)
     * @param loginId 등록 시도 중인 사용자 아이디
     * @param dto 등록할 기기 정보
     */
    @Transactional
    public void registerDevice(String loginId, HardwareDto dto) {
        // 유저 존재 여부 확인
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_USER_NOT_FOUND));

        Optional<Hardware> existingHardware = hardwareRepository.findByDeviceCode(dto.deviceCode());

        String finalName = (dto.deviceName() == null || dto.deviceName().trim().isEmpty())
                ? "나의 약통" : dto.deviceName();

        if (existingHardware.isPresent()) {
            // 기기를 이미 사용중인 경우
            Hardware hw = existingHardware.get();
            if (hw.getDeletedAt() == null) {
                throw new CustomException(ErrorCode.ERROR_DEVICE_ALREADY_REGISTERED);
            }
            // 삭제된 기기, 유저 재등록
            hw.reactivate(user, finalName);
        } else {
            // 새로운 기기
            Hardware newHardware = Hardware.builder()
                    .user(user)
                    .deviceCode(dto.deviceCode())
                    .deviceName(finalName)
                    .build();
            hardwareRepository.save(newHardware);
        }
    }

    /**
     * 유저가 등록한 기기 목록을 조회
     * @param loginId 조회하려는 유저의 로그인 아이디
     * @return 하드웨어 정보 DTO 리스트
     */
    @Transactional(readOnly = true)
    public List<HardwareDto> getDeviceList(String loginId) {
        return hardwareRepository.findAllByUser_LoginId(loginId).stream()
                .map(hw -> new HardwareDto(
                        hw.getDeviceCode(),
                        hw.getDeviceName(),
                        hw.getHardwareId()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 기기 정보(이름)를 수정
     * @param loginId 수정 요청을 보낸 유저의 아이디
     * @param dto 수정할 기기 정보 (deviceCode, 새로운 deviceName )
     */
    @Transactional
    public void updateDevice(String loginId, HardwareDto dto) {
        // 기기 존재 확인
        Hardware hw = hardwareRepository.findByDeviceCode(dto.deviceCode())
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_DEVICE_NOT_FOUND));

        // 권한 검증
        if (!hw.getUser().getLoginId().equals(loginId)) {
            throw new CustomException(ErrorCode.ERROR_UNAUTHORIZED_ACCESS);
        }

        // 삭제 여부 확인
        if (hw.getDeletedAt() != null) {
            throw new CustomException(ErrorCode.ERROR_DEVICE_NOT_FOUND);
        }

        // 이름 업데이트
        String newName = (dto.deviceName() == null || dto.deviceName().trim().isEmpty())
                ? "나의 약통" : dto.deviceName();
        hw.updateDeviceName(newName);
    }
}