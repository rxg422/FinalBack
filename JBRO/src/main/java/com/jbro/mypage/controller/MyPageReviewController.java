package com.jbro.mypage.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbro.mypage.model.service.MyPageService;
import com.jbro.mypage.model.vo.MyPageReviewVo;

@RestController
@RequestMapping("/api/mypage/reviews")
public class MyPageReviewController {

	private final MyPageService myPageService;

	public MyPageReviewController(MyPageService myPageService) {
		this.myPageService = myPageService;
	}

	@GetMapping
	public Map<String, Object> getMyReviews() {
		List<MyPageReviewVo> list = myPageService.getMyReviews();

		return Map.of(
			"success", true,
			"list", list
		);
	}
}
