package pilltapserver.domain.medicationschedule;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.Builder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import pilltapserver.domain.scheduleTime.ScheduleTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medication_schedule")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE medication_schedule SET deleted_at = NOW(), updated_at = NOW() WHERE schedule_id = ?")
public class MedicationSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Integer scheduleId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "mapping_id")
    private Integer mappingId;

    @Column(name = "schedule_name", nullable = false, length = 100)
    private String scheduleName;

    @Column(name = "calibrated_weight", precision = 5, scale = 2)
    private BigDecimal calibratedWeight;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "medicationSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleTime> scheduleTimes = new ArrayList<>();

    @Builder
    public MedicationSchedule(Integer userId, Integer mappingId, String scheduleName) {
        this.userId = userId;
        this.mappingId = mappingId;
        this.scheduleName = scheduleName;
    }

    /**
     * 스케줄 이름 수정
     * @param scheduleName 사용자가 변경할 새로운 스케줄 이름
     */
    public void updateScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
        this.updatedAt = LocalDateTime.now();
    }
    /**
     * 1회 복약 기준 무게 설정 (영점 조정)
     * 하드웨어에서 전송된 센싱 데이터를 바탕으로 1회 복약 무게가 계산된 후 호출됩니다.
     * * @param calculatedWeight 계산 완료된 1회 기준 무게
     */
    public void calibrateWeight(BigDecimal calculatedWeight) {
        this.calibratedWeight = calculatedWeight;
        this.updatedAt = LocalDateTime.now();
    }
}