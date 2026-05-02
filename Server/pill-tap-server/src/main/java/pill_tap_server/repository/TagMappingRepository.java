package pill_tap_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pill_tap_server.domain.TagMapping;

public interface TagMappingRepository extends JpaRepository<TagMapping, Long>{
}
