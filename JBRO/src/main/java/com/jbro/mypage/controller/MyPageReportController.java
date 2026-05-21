package com.jbro.mypage.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbro.mypage.model.service.MyPageService;
import com.jbro.mypage.model.vo.MyPageReportVo;

@RestController
@RequestMapping("/api/mypage/reports")
public class MyPageReportController {

	private final MyPageService myPageService;

	public MyPageReportController(MyPageService myPageService) {
		this.myPageService = myPageService;
	}

	@GetMapping
	public Map<String, Object> getMyReports() {
		List<MyPageReportVo> list = myPageService.getMyReports();

		return Map.of(
			"success", true,
			"list", list
		);
	}
}
