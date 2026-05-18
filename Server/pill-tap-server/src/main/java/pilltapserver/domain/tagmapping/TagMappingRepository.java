package pilltapserver.domain.tagmapping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagMappingRepository extends JpaRepository<TagMapping,Integer> {
    Optional<TagMapping> findByTagUid (String tagUid);
}
