package pill_tap_server.domain;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "medication_schedule")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MedicationSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Integer scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mapping_id", nullable = false)
    private TagMapping tagMapping;

    @Column(name = "schedule_name", nullable = false, length = 100)
    private String scheduleName;

    @Column(name = "take_time", nullable = false)
    private LocalTime takeTime;

    // 비트마스크 요일 (0~127)
    @Column(name = "days_of_week", nullable = false, columnDefinition = "int2")
    private Integer daysOfWeek;

    // 영점 조절 및 기준 무게
    @Column(name = "calibrated_weight", precision = 5, scale = 2)
    private BigDecimal calibratedWeight;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}