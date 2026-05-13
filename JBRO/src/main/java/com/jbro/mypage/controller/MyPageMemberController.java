package com.jbro.mypage.controller;

import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbro.mypage.model.service.MyPageService;

@RestController
@RequestMapping("/api/mypage/member")
public class MyPageMemberController {

	private final MyPageService myPageService;

	public MyPageMemberController(MyPageService myPageService) {
		this.myPageService = myPageService;
	}

	@DeleteMapping
	public Map<String, Object> withdrawMember(HttpSession session) {
		boolean success = myPageService.withdrawMember(session);

		if (success) {
			session.invalidate();
		}

		return Map.of(
			"success", success,
			"message", success ? "회원탈퇴가 완료되었습니다." : "회원탈퇴 처리에 실패했습니다.",
			"redirectUrl", "/"
		);
	}
}
