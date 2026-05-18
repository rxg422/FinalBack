package com.jbro.ai.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jbro.ai.model.dto.AIRecDto;
import com.jbro.ai.model.service.AIService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/aiRec")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AIController {

    private final AIService aiService;

    @PostMapping
    public ResponseEntity<List<AIRecDto>> aiRecommend(
            @RequestBody List<AIRecDto> request) {

        List<AIRecDto> response = aiService.aiRecommend(request);

        return ResponseEntity.ok(response);
    }
}