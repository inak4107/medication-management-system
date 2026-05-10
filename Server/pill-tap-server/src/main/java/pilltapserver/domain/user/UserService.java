package pilltapserver.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * 유저 엔터티 생성 및 저장
     */
    @Transactional
    public void create(User user){
        userRepository.save(user);
    }
}
