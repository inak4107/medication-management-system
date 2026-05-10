package pilltapserver.config;

// 스프링 기본 설정 라이브러리
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// 보안 라이브러리
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
// 비밀번호 암호화 라이브러리
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// 프론트엔드 연결 에러 방지
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// 자바 기본
import java.util.List;

/**
 * Spring Security 전역 설정 클래스
 * - 인증 및 인가 정책 설정
 * - CORS 정책 및 세션 관리(JWT 전환 대비) 설정
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig{
    /**
     * 비밀번호 단방향 해시 암호화 인코더 빈 등록
     * BCrypt 알고리즘을 사용하여 평문 비밀번호를 안전하게 암호화하여 DB에 저장
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**
     * HTTP 보안 필터 체인 설정
     * API 서버 환경에 맞추어 불필요한 기본 보안 설정을 비활성화하고, 라우팅 권한을 제어
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 프론트엔드 통신 허용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 불필요한 기본 보안 기능 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // 세션 관리 정책
                // JWS사용 예정으로 STATELESS(서버에 저장하지 않음)를 사용
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //엔드포인트별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 회원가입/로그인 API 및 Swagger API 문서 접근은 인증 없이 모두 허용
                        .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // 그 외의 요청은 인증 필요
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    /**
     * CORS 글로벌 설정 소스 빈 등록
     * 프론트엔드 애플리케이션에서의 API 접근을 허용하기 위한 규칙 정의
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 개발 끝나고 바꾸기 List.of("https://프론트-도메인.com")
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));

        // 위에서 설정한 규칙을 모든 URL 경로(/**)에 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
