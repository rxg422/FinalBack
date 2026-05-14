package com.jbro.chat.controller;

import com.jbro.chat.dto.ChatRequest;
import com.jbro.chat.entity.ChatHistory;
import com.jbro.chat.service.ChatHistoryService;
import com.jbro.plan.service.AiRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ChatController {

    private final ChatHistoryService chatHistoryService;
    private final AiRecommendService aiRecommendService;

    /**
     * AI 역사 챗봇 질문하기
     */
    @PostMapping("/ask")
    public ResponseEntity<ChatRequest> askAi(@RequestBody ChatRequest dto) {
        // 1. AI 에이전트 서비스 호출하여 답변 생성
        String answer = aiRecommendService.getChatResponse(dto.getQuestion());

        // 2. 결과 DTO에 세팅
        dto.setAnswer(answer);

        // 3. DB에 질문과 답변 내역 저장
        chatHistoryService.saveChat(dto);

        return ResponseEntity.ok(dto);
    }

    /**
     * 이전 채팅 내역 불러오기
     */
    @GetMapping("/history")
    public ResponseEntity<List<ChatHistory>> getHistory() {
        return ResponseEntity.ok(chatHistoryService.getAllHistory());
    }
}