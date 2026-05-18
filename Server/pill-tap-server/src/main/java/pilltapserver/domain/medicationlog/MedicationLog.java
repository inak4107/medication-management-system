package pilltapserver.domain.medicationlog;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pilltapserver.domain.user.User;
import pilltapserver.domain.hardware.Hardware;
import pilltapserver.domain.tagmapping.TagMapping;
import pilltapserver.domain.medicationschedule.MedicationSchedule;
import java.time.LocalDateTime;

@Entity
@Table(name = "medication_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MedicationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hardware_id", nullable = false)
    private Hardware hardware;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mapping_id", nullable = false)
    private TagMapping tagMapping;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private MedicationSchedule medicationSchedule;

    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;

    @Column(name = "measured_weight", nullable = false, precision = 5, scale = 2)
    private BigDecimal measuredWeight;

    @Column(name = "status", nullable = false, columnDefinition = "int2")
    private Integer status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}