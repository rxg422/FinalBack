package com.jbro.mypage.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jbro.mypage.model.service.MyPageService;
import com.jbro.mypage.model.vo.MyPagePlannerCandidateVo;

@RestController
@RequestMapping("/api/mypage/planner")
public class MyPagePlannerController {

	private final MyPageService myPageService;

	public MyPagePlannerController(MyPageService myPageService) {
		this.myPageService = myPageService;
	}

	@GetMapping("/candidates")
	public Map<String, Object> getPlannerCandidates(
		@RequestParam(defaultValue = "search") String source,
		@RequestParam(required = false) String category,
		@RequestParam(required = false) String keyword,
		@RequestParam(required = false) String regions,
		@RequestParam(required = false) String excludeContentIds
	) {
		List<MyPagePlannerCandidateVo> list = myPageService.getPlannerCandidates(
			source,
			category,
			keyword,
			regions,
			excludeContentIds
		);

		return Map.of(
			"success", true,
			"list", list
		);
	}
}
