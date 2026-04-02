
package com.medreminder.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Data
public class Medication {
 @Id @GeneratedValue
 private Long id;
 private String name;
 private String dosage;
 private LocalTime timeToTake;

 @ManyToOne
 private User user;
}
