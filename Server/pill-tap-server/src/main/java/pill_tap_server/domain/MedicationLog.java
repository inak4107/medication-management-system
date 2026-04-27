package pill_tap_server.domain;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "medication_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MedicationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;

    //스티커(약)과 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mapping_id", nullable = false)
    private TagMapping tagMapping;

    //스케줄과의 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private MedicationSchedule schedule;

    //유저와의 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "scheduled_time", nullable = false)
    private LocalTime scheduledTime;

    @Column(name = "take_time", nullable = false)
    private LocalDateTime takeTime = LocalDateTime.now();

    @Column(name = "measured_weight", nullable = false, precision = 5, scale = 2)
    private BigDecimal measuredWeight;

    @Column(name = "status", nullable = false, columnDefinition = "int2")
    private Integer status; // 0(FAIL), 1(AUTO_SUCCESS), 2(MANUAL_CONFIRMED)

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}