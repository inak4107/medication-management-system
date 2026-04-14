package com.medreminder.app.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "medication_schedule")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "schedule_name")
    private String name;

    @Column(name = "take_time")
    private LocalTime timeToTake;

    @Column(name = "calibrated_weight")
    private Double calibratedWeight;

    @ManyToOne
@JoinColumn(name = "user_id")
private User user;

public void setUser(User user) {
    this.user = user;
}

public User getUser() {
    return user;
}
}