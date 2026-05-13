package com.jbro.auth.filter;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jbro.auth.model.service.JwtTokenProvider;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String token = resolveBearerToken(request);

		if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			try {
				Long memberId = jwtTokenProvider.getMemberId(token);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					memberId,
					null,
					List.of(new SimpleGrantedAuthority("ROLE_USER"))
				);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (RuntimeException exception) {
				SecurityContextHolder.clearContext();
			}
		}

		filterChain.doFilter(request, response);
	}

	private String resolveBearerToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return null;
		}

		return authorization.substring(7).trim();
	}
}
