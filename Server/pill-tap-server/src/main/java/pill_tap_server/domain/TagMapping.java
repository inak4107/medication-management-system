package pill_tap_server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import java.time.LocalDateTime;

@Entity
@Table(name = "tag_mappings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapping_id")
    private Integer mappingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "tag_uid", nullable = false, unique = true, length = 50)
    private String tagUid; // NFC 스티커 고유 번호

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "last_tagged_at")
    private LocalDateTime lastTaggedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}