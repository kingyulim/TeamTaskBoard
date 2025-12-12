package com.teamteskboard.common.config;

import lombok.RequiredArgsConstructor;
import com.teamteskboard.common.jwt.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, SecurityContextHolderAwareRequestFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // 회원가입, 로그인은 누구나 접근 가능
                        .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/users").permitAll()
                        .anyRequest().authenticated() // 나머지는 로그인한 사용자만 접근 가능
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000"));                        // 모든 오리진(출처)에서의 요청을 허용
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // 허용되는 HTTP 메서드를 지정
        config.setAllowedHeaders(List.of("*"));                                            // 모든 헤더를 허용
        config.setExposedHeaders(List.of("Authorization"));                                // 브라우저에 노출할 응답 헤더를 지정
        config.setAllowCredentials(true);                                                      // 인증된 요청을 허용 (ex: HTTP 인증)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);                                // 모든 경로에 대해 CORS 설정을 적용

        return source;
    }
}
