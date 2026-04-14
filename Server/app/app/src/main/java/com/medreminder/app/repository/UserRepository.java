
package com.medreminder.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.medreminder.app.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
 Optional<User> findByLoginId(String loginId);
}
