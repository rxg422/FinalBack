package com.jbro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer; // ★ 이 임포트로 바꿔야 합니다!
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            
            .cors(Customizer.withDefaults())

            .authorizeHttpRequests(auth -> auth
                // 1. 채팅 관련 API 허용
                .requestMatchers("/api/chat/**").permitAll() 
                
                // 2. ★ 여행 추천 관련 API도 누구나 접근 가능하게 허용 ★
                .requestMatchers("/api/plan/**").permitAll() 
                
                // 나머지 요청은 로그인이 필요함
                .anyRequest().authenticated()
            )
            
            .formLogin(Customizer.withDefaults());

        return http.build();
    }
}