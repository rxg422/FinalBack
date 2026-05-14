package com.jbro.plan.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import lombok.RequiredArgsConstructor;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AiRecommendServiceImpl implements AiRecommendService {

    @Value("${xai.api.key}") // Groq API Key
    private String groqKey;

    @Value("${tavily.api.key}") // Tavily API Key
    private String tavilyKey;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 역사 AI 챗봇 메인 로직
     */
    @Override
    public String getChatResponse(String question) {
        // 1. Tavily 검색을 통해 전북 역사 관련 최신/정확한 데이터 확보
        String searchContext = searchTavily(question + " 전라북도 역사 문화 유산 공식 정보");
        
        // 2. 검색 데이터를 바탕으로 Groq(Llama)이 답변 생성
        return callGroqForChat(searchContext, question);
    }

    /**
     * Tavily API 호출 (에이전트의 정보 수집 단계)
     */
    private String searchTavily(String query) {
        String url = "https://api.tavily.com/search";
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("api_key", tavilyKey);
            body.put("query", query);
            body.put("search_depth", "basic");
            body.put("max_results", 5);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);
            return response.getBody().toString();
        } catch (Exception e) {
            System.err.println("Tavily 검색 에러: " + e.getMessage());
            return "검색 결과 없음";
        }
    }

    /**
     * Groq API 호출 (에이전트의 판단 및 답변 생성 단계)
     */
    private String callGroqForChat(String context, String question) {
        String url = "https://api.groq.com/openai/v1/chat/completions";
        try {
            // 강력한 페르소나 및 출력 규칙 설정 (할루시네이션 및 외국어 방지)
            String systemPrompt = "당신은 전라북도 역사와 문화를 안내하는 전문 가이드 '전북路 AI'입니다.\n" +
                    "[규칙]\n" +
                    "1. 반드시 100% 한국어로만 답변하십시오. (베트남어, 한자, 영어 노출 금지)\n" +
                    "2. 제공된 참고 데이터를 기반으로 팩트 위주로 설명하되, 문체는 매우 친절한 '~해요'체를 사용하십시오.\n" +
                    "3. 역사적 사실이 불분명할 경우 지어내지 말고 정중히 모른다고 답변하십시오.";

            String userPrompt = String.format("참고 데이터: %s\n\n사용자 질문: %s", context, question);

            Map<String, Object> body = new HashMap<>();
            body.put("model", "llama-3.3-70b-versatile");
            body.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt)
            ));

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(groqKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            // API 응답 데이터 파싱
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            
            return (String) message.get("content");
        } catch (Exception e) {
            System.err.println("Groq 호출 에러: " + e.getMessage());
            return "죄송합니다. 정보를 정리하는 중에 문제가 발생했습니다. 잠시 후 다시 질문해 주세요.";
        }
    }
}