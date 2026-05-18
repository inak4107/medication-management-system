package pilltapserver.domain.scheduleTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import pilltapserver.domain.medicationschedule.MedicationSchedule;

import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "schedule_time")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE schedule_time SET deleted_at = NOW(), updated_at = NOW() WHERE time_id = ?")
public class ScheduleTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "time_id")
    private Integer timeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private MedicationSchedule medicationSchedule;

    @Column(name = "take_time", nullable = false)
    private LocalTime takeTime;

    @Column(name = "days_of_week", nullable = false, columnDefinition = "int2")
    private Integer daysOfWeek;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * ScheduleTime 엔터티 생성자
     * @param medicationSchedule 연관된 부모 복약 스케줄 객체
     * @param takeTime 설정된 복약 예정 시간
     * @param daysOfWeek 설정된 복약 요일 (비트마스크 값 0~127)
     */
    @Builder
    public ScheduleTime(MedicationSchedule medicationSchedule, LocalTime takeTime, Integer daysOfWeek) {
        this.medicationSchedule = medicationSchedule;
        this.takeTime = takeTime;
        this.daysOfWeek = daysOfWeek;
    }

    /**
     * 복약 시간 및 요일 정보 수정
     * @param takeTime 변경할 복약 시간
     * @param daysOfWeek 변경할 복약 요일 (비트마스크 값)
     */
    public void updateTimeAndDays(LocalTime takeTime, Integer daysOfWeek) {
        this.takeTime = takeTime;
        this.daysOfWeek = daysOfWeek;
        this.updatedAt = LocalDateTime.now();
    }
}