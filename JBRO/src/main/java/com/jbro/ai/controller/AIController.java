package com.jbro.ai.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbro.ai.model.dto.AIDto.AIPlanResp;
import com.jbro.ai.model.dto.AIDto.AIPlanUserReq;
import com.jbro.ai.model.dto.AIRecDto;
import com.jbro.ai.model.service.AIService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {
	

	private final AIService aiService;
	
	@PostMapping("recommend")
	public ResponseEntity<List<AIRecDto>> aiRecommend(@RequestBody int areaCode[]) {
		List<AIRecDto> response = aiService.aiRecommend(areaCode);
		
		return ResponseEntity.ok(response);
	}

	
	@PostMapping("planner")
	public ResponseEntity<AIPlanResp> aiPlanner(@RequestBody AIPlanUserReq request) {
		AIPlanResp response = aiService.aiPlanner(request);
		
		return ResponseEntity.ok(response);
	}
	
	
	
}
