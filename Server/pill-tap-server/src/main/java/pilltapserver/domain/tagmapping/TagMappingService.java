package pilltapserver.domain.tagmapping;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagMappingService {

    private final TagMappingRepository tagMappingRepository;

    @Transactional
    public TagMapping registerTag(String tagUid, Integer userId) {
        // 기존 태그 정보 존재 여부 확인
        Optional<TagMapping> existingTag = tagMappingRepository.findByTagUid(tagUid);

        // 태그가 이미 존재하는 경우
        if (existingTag.isPresent()) {
            TagMapping tagMapping = existingTag.get();
            tagMapping.changeUser(userId);
            tagMapping.restore();
            tagMapping.updateLastTaggedAt();
            return tagMapping;
        }

        // 새로운 태그를 등록할 경우
        TagMapping newTagMapping = TagMapping.builder()
                .tagUid(tagUid)
                .userId(userId)
                .build();
        return tagMappingRepository.save(newTagMapping);
    }
}