package com.jbro;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.jbro.auth.filter.JwtAuthenticationFilter;
import com.jbro.auth.handler.OAuth2LoginSuccessHandler;

@Configuration
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
			.cors(Customizer.withDefaults())
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/login", "/oauth2/**", "/login/oauth2/**").permitAll()
				.requestMatchers("/api/health/**").permitAll()
				.requestMatchers("/api/members/**").permitAll()
				.requestMatchers("/api/users2/**").permitAll()
				.requestMatchers("/uploads/**").permitAll()
				.requestMatchers("/api/mypage/**").authenticated()
				.anyRequest().authenticated()
			)
			.csrf(csrf -> csrf.disable())
			.formLogin(form -> form.disable())
			.oauth2Login(oauth2 -> oauth2
				.successHandler(oAuth2LoginSuccessHandler)
			)
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint((request, response, authException) -> {
					if (request.getRequestURI().startsWith("/api/")) {
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						response.setContentType("application/json;charset=UTF-8");
						response.getWriter().write("{\"success\":false,\"message\":\"로그인이 필요합니다.\"}");
						return;
					}

					response.sendRedirect("/login");
				})
			)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of(
			"http://localhost:3000",
			"http://127.0.0.1:3000",
			"http://192.168.10.29:3000",
			"http://192.168.10.28:3000"
		));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/api/**", configuration);
		return source;
	}
}
