package com.jbro.mypage.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jbro.auth.model.service.JwtTokenProvider;
import com.jbro.mypage.model.service.MyPageService;
import com.jbro.mypage.model.vo.MemberVo;

@RestController
@RequestMapping({"/api/members", "/api/users"})
public class MyPageController {

	private final MyPageService myPageService;
	private final JwtTokenProvider jwtTokenProvider;

	public MyPageController(MyPageService myPageService, JwtTokenProvider jwtTokenProvider) {
		this.myPageService = myPageService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@GetMapping
	public List<MemberVo> getMemberList() {
		return myPageService.getMemberList();
	}

	@GetMapping("/{memberId}")
	public MemberVo getMember(@PathVariable Long memberId) {
		return myPageService.getMember(memberId);
	}

	@PostMapping
	public int addMember(@RequestBody MemberVo member) {
		return myPageService.addMember(member);
	}

	@PutMapping("/{memberId}")
	public int modifyMember(@PathVariable Long memberId, @RequestBody MemberVo member) {
		member.setId(memberId);
		return myPageService.modifyMember(member);
	}

	@DeleteMapping("/{memberId}")
	public int removeMember(@PathVariable Long memberId) {
		return myPageService.removeMember(memberId);
	}

	// ========== 닉네임 유효성 검사 ==========
	private String validateNickname(String nickname) {
		if (nickname == null || nickname.trim().isEmpty()) {
			return "닉네임을 입력해주세요.";
		}
		if (nickname.length() < 2 || nickname.length() > 12) {
			return "닉네임은 2~12자리로 입력해주세요.";
		}
		if (!nickname.matches("^[가-힣a-zA-Z0-9]{2,12}$")) {
			return "닉네임은 한글,영문,숫자만 사용 가능합니다.";
		}
		return null;
	}

	// ========== 닉네임 중복 체크 ==========
	@GetMapping("/check-nickname")
	public Map<String, Object> checkNickname(@RequestParam String nickname) {
		String validationError = validateNickname(nickname);
		if (validationError != null) {
			return Map.of(
				"duplicate", true,
				"message", validationError
			);
		}
		boolean available = myPageService.isNicknameAvailable(nickname);
		return Map.of(
			"duplicate", !available,
			"message", available ? "사용 가능한 닉네임입니다." : "이미 사용 중인 닉네임입니다."
		);
	}

	// ========== 닉네임 업데이트 ==========
	@PatchMapping("/nickname")
	public Map<String, Object> updateNickname(@RequestBody Map<String, String> request) {
		String nickname = request.get("nickname");
		String validationError = validateNickname(nickname);
		if (validationError != null) {
			return Map.of("success", false, "message", validationError);
		}

		if (!myPageService.isNicknameAvailable(nickname)) {
			return Map.of("success", false, "message", "이미 사용 중인 닉네임입니다.");
		}

		MemberVo updatedMember = myPageService.modifyMyPageNickname(nickname);
		String newAccessToken = jwtTokenProvider.createAccessToken(updatedMember);

		return Map.of(
			"success", true,
			"message", "닉네임이 저장되었습니다.",
			"token", newAccessToken
		);
	}
}

