package com.jbro.ai.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbro.ai.model.dto.AIRecDto;
import com.jbro.ai.model.service.AIService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/aiRec")
@RequiredArgsConstructor
public class AIController {
	

	private final AIService aiService;
	
	@PostMapping
	public ResponseEntity<List<AIRecDto>> aiRecommend(@RequestBody int areaCode[]) {
		List<AIRecDto> response = aiService.aiRecommend(areaCode);
		
		System.out.println(response);
		
		return ResponseEntity.ok(response);
	}

	
}
