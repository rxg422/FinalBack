package com.jbro.mypage.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbro.mypage.model.service.MyPageService;
import com.jbro.mypage.model.vo.MemberVo;

@RestController
@RequestMapping("/api/members")
public class MyPageController {

	private final MyPageService myPageService;

	public MyPageController(MyPageService myPageService) {
		this.myPageService = myPageService;
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
}

