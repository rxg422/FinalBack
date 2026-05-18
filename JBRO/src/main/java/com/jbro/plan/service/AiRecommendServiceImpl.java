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

    @Value("${grok.api.key}")
    private String groqKey;

    @Value("${tavily.api.key}")
    private String tavilyKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getChatResponse(String question) {
        // 검색 쿼리에 '전라북도'를 명시적으로 포함하여 범위를 좁힘
        String searchContext = searchTavily(question + " 전라북도 역사 문화 유적");
        return callGroqForChat(searchContext, question);
    }

    private String searchTavily(String query) {
        String url = "https://api.tavily.com/search";
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("api_key", tavilyKey);
            body.put("query", query);
            body.put("search_depth", "basic");
            body.put("max_results", 5);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);
            if (response.getBody() != null && response.getBody().containsKey("results")) {
                return response.getBody().get("results").toString();
            }
            return "검색 결과 없음";
        } catch (Exception e) {
            return "검색 서비스 이용 불가";
        }
    }

    private String callGroqForChat(String context, String question) {
        String url = "https://api.groq.com/openai/v1/chat/completions";
        try {
            String systemPrompt = "당신은 전라북도 역사 가이드 '전북路 AI'입니다.\n" +
                    "[필수 규칙]\n" +
                    "1. 전라북도의 역사와 관련된 질문에만 답하고, 그 외에는 거절하세요.\n" +
                    "2. **다양성 유지**: 추천 요청 시 전주뿐만 아니라 군산, 익산, 남원, 김제, 부안 등 전북의 다양한 시/군을 골고루 추천하세요.\n" +
                    "3. **중복 방지**: 질문자가 유래를 묻지 않는 한, 모든 답변에 한옥마을의 1930년대 형성 배경을 구구절절 설명하지 마세요. 질문에 맞는 핵심 정보만 답하세요.\n" +
                    "\n" +
                    "[지식 베이스(참고용)]\n" +
                    "- 전주: 한옥마을(일제강점기 형성), 경기전, 오목대\n" +
                    "- 익산: 미륵사지, 왕궁리 유적 (백제 역사)\n" +
                    "- 군산: 근대화 거리, 신흥동 일본식 가옥\n" +
                    "- 남원: 광한루원, 만복사지\n" +
                    "- 김제: 벽골제 (우리나라 최고 저수지)\n" +
                    "\n" +
                    "[답변 원칙]\n" +
                    "1. 친절한 존댓말('~해요')로 3~4문장 이내로 작성하세요.\n" +
                    "2. 검색 결과(참고 데이터)에 있는 새로운 장소들을 적극적으로 활용하세요.";

            String userPrompt = String.format(
                    "참고 데이터: %s\n" +
                    "질문: %s\n\n" +
                    "위 데이터를 바탕으로 질문에 답해 주세요. 특히 '추천' 질문이라면 매번 똑같은 곳만 말하지 말고 다양한 지역의 유적지를 골고루 소개해 주세요.", 
                    context, question);

            Map<String, Object> body = new HashMap<>();
            body.put("model", "llama-3.3-70b-versatile");
            body.put("max_tokens", 400); 
            
            // [핵심 변경] 온도를 0.7 정도로 높여 답변의 다양성을 확보합니다.
            body.put("temperature", 0.7); 
            
            body.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt)
            ));

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(groqKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            
            return (String) message.get("content");
        } catch (Exception e) {
            return "정보를 확인하는 중에 문제가 발생했어요.";
        }
    }
}