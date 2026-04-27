package pill_tap_server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
