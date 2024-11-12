package rm.backend.jwt.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import rm.backend.jwt.config.util.JWTProperties;

@Configuration
@RequiredArgsConstructor
public class CorsConfig {

    private final JWTProperties jwtProperties;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        // 인증 정보를 포함한 요청을 허용
        config.addAllowedOriginPattern("https://real-moment.kro.kr");
        config.addAllowedOriginPattern("https://real-moment-admin.kro.kr");
        config.addAllowedOriginPattern("http://43.202.0.10");
        // 모든 요청의 Origin을 허용
        config.addAllowedHeader("*");
        // 모든 요청의 헤더를 허용
        config.addAllowedMethod("*");
        // 모든 요청의 HTTP 메서드 허용

        config.addExposedHeader(jwtProperties.getACCESS_STRING());
        config.addExposedHeader(jwtProperties.getREFRESH_STRING());
        // 클라이언트(리액트, 브라우저)가 응답 헤더에 담긴 토큰을 읽을 수 있도록 허용

        source.registerCorsConfiguration("/**", config);                                     
        // /api/**로 들어오는 url(경로)에 해당 CORS규칙(config)을 적용

        return new CorsFilter(source);
    }
}