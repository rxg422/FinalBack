package com.jbro.ai.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbro.ai.model.dao.AIDao;
import com.jbro.ai.model.dto.AIRecDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AIServiceImpl implements AIService {
	
	private final WebClient webClient = WebClient.builder().build();
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final AIDao aiDao;
	
	
	@Value("${grok.api.key}")
	private String grokKey;
	
	@Value("${grok.api.url}")
	private String grokUrl;
	
	public List<AIRecDto> aiRecommend(int areaCode[]) {
		List<AIRecDto> placeList = aiDao.selectPlaceList(areaCode);
		
		String placeInfo = placeList.stream().map(place -> String.format(
				"""
				contentId: %d
				title:  %s
				addr1: %s
                addr2: %s
				""",
				place.getContentId(),
				place.getTitle(),
				place.getAddr1(),
				place.getAddr2()
				)).collect(Collectors.joining("\n"));
		
		String prompt = """
				You are an AI that recommends tourist spots in Jeollabuk-do.
				Recommend 5 travel destinations from the list below with simple recommendation reasons.
				
				list: %s
				
				Please respond only in the JSON array format below.
				Never add explanatory sentences.
				
				[
                  {
                    "contentId": 123,
                    "title": "전주한옥마을",
                    "firstImage2": "https://...",
                    "addr1": "전북 전주시 ...",
                    "addr2": "",
                    "reason": "전통 한옥과 먹거리를 함께 즐길 수 있습니다."
                  }
                ]
				""".formatted(placeInfo);
		
		// Grok AI 요청 Body 생성
        Map<String, Object> requestBody = Map.of(
                "model", "llama-3.3-70b-versatile",
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content", "당신은 JSON만 반환하는 여행 추천 AI입니다."
                        ),
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                ),
                "temperature", 0.3
        );
        
        // xAI API 호출
        Map response = webClient.post()
                .uri(grokUrl) // https://api.x.ai/v1/chat/completions
                .header("Authorization", "Bearer " + grokKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        
        // 응답에서 content 추출
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        Map<String, Object> firstChoice = choices.get(0);
        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
        String content = (String) message.get("content");

        // ```json ... ``` 형태로 감싸져 오는 경우 제거
        content = content.replaceAll("^```json\\s*", "")
                         .replaceAll("^```\\s*", "")
                         .replaceAll("\\s*```$", "")
                         .trim();
        		
		try {
            // JSON 문자열 → List<AIRecDto>
            return objectMapper.readValue(
                    content,
                    new TypeReference<List<AIRecDto>>() {}
            );
        } catch (Exception e) {
            throw new RuntimeException("AI 응답 파싱 실패: " + content, e);
        }
	}

}
