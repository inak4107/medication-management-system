package pilltapserver.domain.hardware;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface HardwareRepository extends JpaRepository<Hardware, Integer>{
    /**
     * 기기 고유 코드로 하드웨어 정보를 조회합니다.
     * @param deviceCode NFC 고유 코드
     * @return 해당 코드를 가진 하드웨어 정보
     */
    Optional<Hardware> findByDeviceCode(String deviceCode);

    /**
     * 특정 유저가 등록한 모든 기기 목록을 조회합니다.
     * @param loginId 기기 소유 권한 확인을 위한 로그인 아이디
     * @return 유저가 소유중인 하드웨어 목록
     */
    List<Hardware> findAllByUser_LoginId(String loginId);

    /**
     * 기기 재등록을 위해 삭제된 기기를 포함하여 기기 고유 코드로 하드웨어 정보를 조회합니다.
     * @param deviceCode NFC 고유 코드
     * @return 해당 코드를 가진 하드웨어 정보 (삭제된 데이터 포함)
     */
    @Query(value = "SELECT * FROM hardwares WHERE device_code = :deviceCode", nativeQuery = true)
    Optional<Hardware> findByDeviceCodeIncludingDeleted(@Param("deviceCode") String deviceCode);
}