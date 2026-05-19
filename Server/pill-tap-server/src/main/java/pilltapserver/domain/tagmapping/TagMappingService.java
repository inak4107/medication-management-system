package pilltapserver.domain.tagmapping;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagMappingService {

    private final TagMappingRepository tagMappingRepository;

    /**
     * 새로운 약통 등록
     * @param tagUid nfc 스티커 ID
     * @param userId 유저 고유 번호 (pk)
     * @return DB에 저장된 nfc스티커 고유 ID (pk)
     */
    @Transactional
    public TagMapping registerTag(String tagUid, Integer userId) {
        // 기존 태그 정보 존재 여부 확인
        Optional<TagMapping> existingTag = tagMappingRepository.findByTagUid(tagUid);

        // 태그가 이미 존재하는 경우
        if (existingTag.isPresent()) {
            TagMapping tagMapping = existingTag.get();
            tagMapping.changeUser(userId);
            tagMapping.restore();
            return tagMapping;
        }

        // 새로운 태그를 등록할 경우
        TagMapping newTagMapping = TagMapping.builder()
                .tagUid(tagUid)
                .userId(userId)
                .build();
        return tagMappingRepository.save(newTagMapping);
    }

    /**
     * NFC 태그 매핑 수정 및 삭제
     * @param currentMappingId 기존 매핑 고유 번호 (pk)
     * @param newTagUid 변경할 새로운 태그 UID (연동 해제 시 null)
     * @param userId 사용자 고유 번호 (pk)
     * @return 변경된 매핑 고유 번호 (pk)
     */
    @Transactional
    public Integer updateTagMapping(Integer currentMappingId, String newTagUid, Integer userId) {
        if (currentMappingId != null) {
            tagMappingRepository.findById(currentMappingId)
                    .ifPresent(TagMapping::deactivate);
        }

        if (newTagUid == null || newTagUid.trim().isEmpty()) {
            return null;
        }

        TagMapping newMapping = registerTag(newTagUid, userId);
        return newMapping.getMappingId();
    }
}