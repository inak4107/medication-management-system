package pilltapserver.domain.hardware;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete; // [추가된 부분] 소프트 딜리트 자동화를 위한 임포트
import org.hibernate.annotations.SQLRestriction;
import pilltapserver.domain.user.User;
import java.time.LocalDateTime;

@Entity
@Table(name = "hardwares")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE hardwares SET deleted_at = NOW(), updated_at = NOW() WHERE hardware_id = ?") // [추가된 부분] delete() 호출 시 실행될 SQL 지정
public class Hardware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="hardware_id")
    private Integer hardwareId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "device_code", nullable = false, unique = true, length = 50)
    private String deviceCode;

    @Column(name = "device_name", nullable = false, length = 50)
    private String deviceName;  //유저가 설정하는 별명입니다.

    @Column(name = "registered_at", nullable = false, updatable = false)
    private LocalDateTime registeredAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;    // null 허용(삭제 시 기록)

    /**
     * Hardware 엔터티 생성자
     * @param user 연결할 유저 객체
     * @param deviceCode NFC 고유 코드
     * @param deviceName 사용자가 설정한 기기 별명
     */
    @Builder
    public Hardware(User user, String deviceCode, String deviceName) {
        this.user = user;
        this.deviceCode = deviceCode;
        this.deviceName = deviceName;
    }
    /**
     * 기기 별명 수정
     * @param newName 사용자가 입력한 새로운 기기 이름
     */
    public void updateDeviceName(String newName) {
        this.deviceName = newName;
        this.updatedAt = LocalDateTime.now();
    }
    /**
     * 기기 삭제
     */
    public void deleteDevice() {
        this.deletedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 기기 재등록
     * @param newUser 새로운 유저의 ID
     * @param newName 새로운 유저가 정한 deviceName
     */
    public void reactivate(User newUser, String newName) {
        this.user = newUser;
        this.deviceName = newName;
        this.deletedAt = null;
        this.updatedAt = LocalDateTime.now();
    }
}