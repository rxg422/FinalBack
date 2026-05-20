package com.jbro.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.jbro.auth.filter.JwtAuthenticationFilter;
import com.jbro.auth.handler.OAuth2LoginSuccessHandler;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // ✅ @PreAuthorize 활성화
public class SecurityConfig {

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                // CORS
                .cors(Customizer.withDefaults())

                // API 서버 설정
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth -> auth


                        // 기본 허용
                        .requestMatchers("/error", "/favicon.ico").permitAll()
                        .requestMatchers("/", "/login", "/oauth2/**", "/login/oauth2/**").permitAll()

                        // 챗봇 / AI 추천
                        .requestMatchers("/api/chat/**").permitAll()
                        .requestMatchers("/api/aiRec/**").permitAll()

                        // 공개 API
                        .requestMatchers("/api/health/**").permitAll()
                        .requestMatchers("/api/members/**").permitAll()
                        .requestMatchers("/api/users2/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()

                        // 여행 코스 목록 조회
                        .requestMatchers(HttpMethod.GET, "/api/tourList").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tourList/**").permitAll()
                        .requestMatchers("/api/course/**").permitAll()
                        

                        // 찜하기는 로그인 필요
        	            .requestMatchers(HttpMethod.POST, "/api/tourList/favorite/**").authenticated()
        	            // 여행 상세 조회는 비로그인 허용
        	            .requestMatchers(HttpMethod.GET, "/api/tourDetail/**").permitAll()
        	            // 찜/리뷰/신고는 로그인 필요
        	            .requestMatchers(HttpMethod.POST, "/api/tourDetail/*/favorite").authenticated()
        	            .requestMatchers(HttpMethod.POST, "/api/tourDetail/*/reviews").authenticated()
        	            .requestMatchers(HttpMethod.PUT, "/api/tourDetail/reviews/**").authenticated()
        	            .requestMatchers(HttpMethod.DELETE, "/api/tourDetail/reviews/**").authenticated()
        	            .requestMatchers(HttpMethod.POST, "/api/tourDetail/reviews/*/report").authenticated()
        	         // 찜/리뷰/신고 임시 테스트용 (로그인 없이 허용)
//        	            .requestMatchers(HttpMethod.POST, "/api/tourDetail/*/favorite").permitAll()
//        	            .requestMatchers(HttpMethod.POST, "/api/tourDetail/*/reviews").permitAll()
//        	            .requestMatchers(HttpMethod.PUT, "/api/tourDetail/reviews/**").permitAll()
//        	            .requestMatchers(HttpMethod.DELETE, "/api/tourDetail/reviews/**").permitAll()
//        	            .requestMatchers(HttpMethod.POST, "/api/tourDetail/reviews/*/report").permitAll()
                        // 인증 필요
                        .requestMatchers(HttpMethod.POST, "/api/tourList/favorite/**").authenticated()
                        .requestMatchers("/api/mypage/**").authenticated()
                        .requestMatchers("/api/tour/**").authenticated()

                        // ========== 관리자 API (ADMIN만) ==========
                        // 사용자 목록 조회
                        .requestMatchers(HttpMethod.GET, "/api/admin/users").hasAuthority("ADMIN")
                        // 사용자 권한 변경
                        .requestMatchers(HttpMethod.PATCH, "/api/admin/users/*/role").hasAuthority("ADMIN")
                        // 신고 관리
                        .requestMatchers(HttpMethod.GET, "/api/admin/reports").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/admin/reports/**").hasAuthority("ADMIN")

                        // 나머지
                        .anyRequest().authenticated()
                )

                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2LoginSuccessHandler)
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {

                            if (request.getRequestURI().startsWith("/api/")) {

                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.setContentType("application/json;charset=UTF-8");

                                response.getWriter().write(
                                        "{\"success\":false,\"message\":\"인증이 필요합니다.\"}"
                                );

                                return;
                            }

                            response.sendRedirect("http://localhost:3000");
                        })
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:3001",
                "http://127.0.0.1:3000",
                "http://127.0.0.1:3001",
                "http://192.168.10.29:3000",
                "http://192.168.10.29:3001",
                "http://192.168.10.28:3000",
                "http://192.168.10.28:3001"
        ));

        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        // 모든 경로에 CORS 적용
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}