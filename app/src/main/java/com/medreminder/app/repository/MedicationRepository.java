
package com.medreminder.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.medreminder.app.model.Medication;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
 List<Medication> findByUserId(Long userId);
}
