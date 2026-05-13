package com.jbro.auth.model.service;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LoginMemberProvider {

	private static final Long TEMP_LOGIN_MEMBER_ID = 1L;

	public Long getLoginMemberId() {
		Long memberId = getMemberIdFromAuthentication();
		return memberId != null ? memberId : TEMP_LOGIN_MEMBER_ID;
	}

	public Long getLoginMemberId(HttpSession session) {
		Long memberId = getMemberIdFromAuthentication();
		if (memberId != null) {
			return memberId;
		}

		memberId = getMemberIdFromSession(session);
		return memberId != null ? memberId : TEMP_LOGIN_MEMBER_ID;
	}

	private Long getMemberIdFromAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}

		Object principal = authentication.getPrincipal();

		if (principal instanceof Long memberId) {
			return memberId;
		}

		if (principal instanceof Integer memberId) {
			return memberId.longValue();
		}

		if (principal instanceof String memberId && memberId.matches("\\d+")) {
			return Long.parseLong(memberId);
		}

		return null;
	}

	private Long getMemberIdFromSession(HttpSession session) {
		if (session == null) {
			return null;
		}

		Object sessionMemberId = session.getAttribute("memberId");

		if (sessionMemberId instanceof Long memberId) {
			return memberId;
		}

		if (sessionMemberId instanceof Integer memberId) {
			return memberId.longValue();
		}

		if (sessionMemberId instanceof String memberId) {
			return Long.parseLong(memberId);
		}

		return null;
	}
}
