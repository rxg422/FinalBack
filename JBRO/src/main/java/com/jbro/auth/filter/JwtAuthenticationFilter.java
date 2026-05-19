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

import com.jbro.auth.model.dao.AuthDAO;
import com.jbro.auth.model.service.JwtTokenProvider;
import com.jbro.mypage.model.vo.MemberVo;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final AuthDAO authDAO;

	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, AuthDAO authDAO) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.authDAO = authDAO;
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String token = resolveBearerToken(request);

		if (token != null) {
			try {
				Long memberId = jwtTokenProvider.getMemberId(token);
				MemberVo member = authDAO.selectMemberById(memberId);

				if (member == null) {
					SecurityContextHolder.clearContext();
					filterChain.doFilter(request, response);
					return;
				}

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					memberId,
					null,
					List.of(new SimpleGrantedAuthority("ROLE_USER"))
				);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (RuntimeException exception) {
				System.out.println("❌ JWT 파싱 실패: " + exception.getMessage());
				exception.printStackTrace();
				SecurityContextHolder.clearContext();
			}
		} else {
			System.out.println("⚠️ 토큰 없음");
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
