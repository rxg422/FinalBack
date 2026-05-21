package com.jbro.mypage.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbro.mypage.model.service.MyPageService;
import com.jbro.mypage.model.vo.MyPageFavoriteVo;

@RestController
@RequestMapping("/api/mypage/favorites")
public class MyPageFavoriteController {

	private final MyPageService myPageService;

	public MyPageFavoriteController(MyPageService myPageService) {
		this.myPageService = myPageService;
	}

	@GetMapping
	public Map<String, Object> getMyFavorites() {
		List<MyPageFavoriteVo> list = myPageService.getMyFavorites();

		return Map.of(
			"success", true,
			"list", list
		);
	}
}
