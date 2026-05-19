package com.jbro.mypage.controller;

import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbro.mypage.model.service.MyPageService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/mypage/member")
public class MyPageMemberController {

	private final MyPageService myPageService;

	public MyPageMemberController(MyPageService myPageService) {
		this.myPageService = myPageService;
	}

	@DeleteMapping
	public Map<String, Object> withdrawMember(HttpServletRequest request, HttpServletResponse response) {
		boolean success = myPageService.withdrawMember();

		if (!success) {
			return Map.of(
				"success", false,
				"message", "회원탈퇴 처리에 실패했습니다."
			);
		}

		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		SecurityContextHolder.clearContext();
		deleteCookie(response, "JSESSIONID");
		deleteCookie(response, "accessToken");
		deleteCookie(response, "refreshToken");

		return Map.of(
			"success", true,
			"message", "회원탈퇴가 완료되었습니다.",
			"redirectUrl", "/"
		);
	}

	private void deleteCookie(HttpServletResponse response, String name) {
		Cookie cookie = new Cookie(name, null);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
}
