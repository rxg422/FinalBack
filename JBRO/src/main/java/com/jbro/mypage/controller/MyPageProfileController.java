package com.jbro.mypage.controller;

import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jbro.mypage.model.service.MyPageService;
import com.jbro.mypage.model.vo.MemberVo;
import com.jbro.mypage.model.vo.ProfileUpdateResponse;

@RestController
@RequestMapping("/api/mypage/profile")
public class MyPageProfileController {

	private final MyPageService myPageService;

	public MyPageProfileController(MyPageService myPageService) {
		this.myPageService = myPageService;
	}

	@GetMapping
	public MemberVo getProfile() {
		return myPageService.getMyPageProfile();
	}

	@GetMapping("/nickname-check")
	public Map<String, Object> checkNickname(@RequestParam String nickname) {
		String trimmedNickname = nickname.trim();
		boolean available = myPageService.isNicknameAvailable(trimmedNickname);

		return Map.of(
			"available", available,
			"message", available ? "사용 가능한 닉네임입니다." : "이미 사용 중인 닉네임입니다."
		);
	}

	@PutMapping
	public Map<String, Object> updateProfile(@RequestBody Map<String, String> request) {
		String nickname = request.getOrDefault("nickname", "").trim();

		if (nickname.length() < 2 || nickname.length() > 12) {
			return Map.of(
				"success", false,
				"message", "닉네임은 2자 이상 12자 이하로 입력해야 합니다."
			);
		}

		if (!myPageService.isNicknameAvailable(nickname)) {
			return Map.of(
				"success", false,
				"message", "이미 사용 중인 닉네임입니다."
			);
		}

		MemberVo profile = myPageService.modifyMyPageNickname(nickname);

		return Map.of(
			"success", true,
			"message", "닉네임이 수정되었습니다.",
			"profile", profile
		);
	}

	@PostMapping("/image")
	public ResponseEntity<ProfileUpdateResponse> uploadProfileImage(
		@RequestParam("profileImage") MultipartFile profileImage,
		HttpSession session
	) {
		ProfileUpdateResponse response = myPageService.updateProfileImage(profileImage, session);
		return ResponseEntity.ok(response);
	}
}
