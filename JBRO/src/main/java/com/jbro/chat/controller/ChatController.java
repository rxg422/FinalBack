package com.jbro.chat.controller;

import com.jbro.chat.model.vo.ChatVO;
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

    @PostMapping("/save")
    public ResponseEntity<ChatVO> askAi(@RequestBody ChatVO vo) {
        
        String question = vo.getQuestion().trim();
        System.out.println(">>>>>> 챗봇 요청 수신됨: " + question);

        // [임시] 유저 ID 설정
        if(vo.getUserId() == null) {
            vo.setUserId(2L); 
        }

        // 1. AI 서비스 호출 (질문에 대한 답변을 먼저 받음)
        String answer = aiRecommendService.getChatResponse(question);
        vo.setAnswer(answer);

        // 2. 저장 조건 검사 (이중 잠금)
        // - 질문이 숫자만 있거나
        // - 질문이 너무 짧거나 (1글자)
        // - 답변에 거절 메시지가 포함되어 있다면 저장을 하지 않음
        boolean isNumeric = question.matches("\\d+");
        boolean isTooShort = question.length() < 2;
        boolean isInvalidAnswer = answer.contains("기록을 찾지 못했습니다") || answer.contains("안내해 드리는 가이드");

        if (!isNumeric && !isTooShort && !isInvalidAnswer) {
            // 모든 조건을 통과했을 때만 DB 저장
            chatHistoryService.saveChat(vo);
            System.out.println(">>>>>> 유효한 문답으로 판단되어 DB 저장 완료");
        } else {
            System.out.println(">>>>>> 무의미한 질문 또는 거절 답변으로 판단되어 DB 저장을 건너뜁니다.");
        }

        // 저장을 안 하더라도 답변은 사용자에게 보여줘야 하므로 리턴은 함
        return ResponseEntity.ok(vo);
    }

    @GetMapping("/history")
    public ResponseEntity<List<ChatVO>> getHistory() {
        List<ChatVO> historyList = chatHistoryService.getAllHistory();
        return ResponseEntity.ok(historyList);
    }
}