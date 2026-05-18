package pilltapserver.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer userId;

    @Column(name="login_id", length = 50, nullable = false, unique= true)
    private String loginId;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="name",length = 50, nullable = false)
    private String name;

    @Column(name="email",length = 100, nullable = false, unique = true)
    private String email;

    @Column(name="birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name="account_type", nullable = false, columnDefinition = "int2")
    private Integer accountType; // 0(PATIENT), 1(GUARDIAN)

    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

	@Column(name = "deleted_at") // NULL 허용 (삭제된 경우에만 기록)
    private LocalDateTime deletedAt;

    /**
     * User 엔티티 생성자
     * @param loginId 사용자 로그인 아이디
     * @param password 암호화된 비밀번호
     * @param name 사용자 성명
     * @param email 사용자 이메일
     * @param birthDate 사용자 생년월일
     * @param accountType 계정 타입 (0: 환자, 1: 보호자 등)
     */
    @Builder
    public User(String loginId, String password, String name, String email, LocalDate birthDate, Integer accountType) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.accountType = accountType;
    }

    /**
     * 유저 프로필 수정
     * @param name 새로운 사용자 이름
     * @param email 새로운 사용자 이메일
     * @param birthDate 새로운 생년월일
     * @param accountType 새로운 계정 타입
     */
    public void updateProfile(String name, String email, LocalDate birthDate, Integer accountType) {
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.accountType = accountType;
    }
}
