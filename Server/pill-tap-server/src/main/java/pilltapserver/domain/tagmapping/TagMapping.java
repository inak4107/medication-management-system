package pilltapserver.domain.tagmapping;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tag_mappings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TagMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapping_id")
    private Integer mappingId;

    @Column(name = "tag_uid", nullable = false, length = 50, unique = true)
    private String tagUid;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_tagged_at")
    private LocalDateTime last_tagged_at;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void changeUser(Integer userId) {
        this.userId = userId;
    }
    public void restore() {
        this.deletedAt = null;
    }
    /**
     * 데이터 삭제
     */
    public void deactivate() {
        this.deletedAt = LocalDateTime.now();
    }
}