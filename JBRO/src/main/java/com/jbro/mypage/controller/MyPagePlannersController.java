package com.jbro.mypage.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbro.mypage.model.service.MyPageService;
import com.jbro.mypage.model.vo.MyPagePlannerDetailVo;
import com.jbro.mypage.model.vo.MyPagePlannerRequestVo;
import com.jbro.mypage.model.vo.MyPagePlannerVo;

@RestController
@RequestMapping("/api/mypage/planners")
public class MyPagePlannersController {

	private final MyPageService myPageService;

	public MyPagePlannersController(MyPageService myPageService) {
		this.myPageService = myPageService;
	}

	@GetMapping
	public Map<String, Object> getMyPlanners() {
		List<MyPagePlannerVo> list = myPageService.getMyPlanners();

		return Map.of(
			"success", true,
			"list", list
		);
	}

	@GetMapping("/{plannerId}")
	public Map<String, Object> getMyPlannerDetail(@PathVariable Long plannerId) {
		MyPagePlannerDetailVo planner = myPageService.getMyPlannerDetail(plannerId);

		return Map.of(
			"success", true,
			"planner", planner
		);
	}

	@PostMapping
	public Map<String, Object> createMyPlanner(@RequestBody MyPagePlannerRequestVo request) {
		MyPagePlannerVo planner = myPageService.createMyPlanner(request);

		return Map.of(
			"success", true,
			"planner", planner
		);
	}

	@PutMapping("/{plannerId}")
	public Map<String, Object> updateMyPlanner(
		@PathVariable Long plannerId,
		@RequestBody MyPagePlannerRequestVo request
	) {
		MyPagePlannerVo planner = myPageService.updateMyPlanner(plannerId, request);

		return Map.of(
			"success", true,
			"planner", planner
		);
	}

	@DeleteMapping("/{plannerId}")
	public Map<String, Object> deleteMyPlanner(@PathVariable Long plannerId) {
		myPageService.deleteMyPlanner(plannerId);

		return Map.of(
			"success", true,
			"message", "플래너가 삭제되었습니다."
		);
	}
}
