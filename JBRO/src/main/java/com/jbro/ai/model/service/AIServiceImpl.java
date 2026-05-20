package com.jbro.ai.model.service;

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
import com.jbro.ai.model.dto.AIDto.AIPlanPlace;
import com.jbro.ai.model.dto.AIDto.AIPlanReq;
import com.jbro.ai.model.dto.AIDto.AIPlanResp;
import com.jbro.ai.model.dto.AIDto.AIPlanUserReq;
import com.jbro.ai.model.dto.AIDto.AIPlanResp.PlanDays;
import com.jbro.ai.model.dto.AIDto.AIPlanResp.PlanDays.PlanPlace;
import com.jbro.ai.model.dto.AIRecDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AIServiceImpl implements AIService {
	
	private final WebClient webClient = WebClient.builder().build();
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final AIDao aiDao;
	
	
	@Value("${groq.api.key}")
	private String grokKey;
	
	@Value("${groq.api.url}")
	private String grokUrl;
	
	@Override
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
        
        String content = callGroq(prompt);
        		
		try {
            // JSON 문자열 → List<AIRecDto>
            return objectMapper.readValue(
                    content,
                    new TypeReference<List<AIRecDto>>() {}
            );
        }
		catch (Exception e) {
            throw new RuntimeException("AI 응답 파싱 실패: " + content, e);
        }
	}

	@Override
	public AIPlanResp aiPlanner(AIPlanUserReq request) {		
		AIPlanReq aiPlanReq = new AIPlanReq();

		List<Integer> regions = aiDao.selectSignguLsit(request.getRegions());
		aiPlanReq.setRegions(regions);
		
		// 관광지 리스트 조회
		aiPlanReq.setCategoryId(1);
		List<AIPlanPlace> planPlaceList = aiDao.selectPlanPlaceList(aiPlanReq);
		
		// 음식점 리스트 조회
		aiPlanReq.setCategoryId(2);
		List<AIPlanPlace> planFoodList = aiDao.selectPlanPlaceList(aiPlanReq);
		
		// 숙소 리스트 조회
		aiPlanReq.setCategoryId(4);
		List<AIPlanPlace> planLodgingList = aiDao.selectPlanPlaceList(aiPlanReq);
		
		String prompt = """
			You are a travel planner AI who specializes in North Jeolla Province, South Korea.
			
			[User Conditions]
			region: %s
			duration: %s
			travel thema: %s
			
			[Schedule Generation Rules]
			- Be sure to only select from the list of places provided below.
			- You should consider the path of travel based on the address.
			- Do not use the same place repeatedly.
			
			[Last day or One day trip pattern]
			1. 관광지
			2. 음식점
			3. 관광지
			4. 관광지
			
			[Day schedule pattern]
			1. 관광지
			2. 음식점
			3. 관광지
			4. 관광지
			5. 음식점
			6. 숙소
			
			[response format]
			{
				"title": "여행 제목",
				"description": "여행 설명",
				"days": [
					{
						"day": 1,
						"schedule": [
							{
								"order": 1,
								"contentId": 123,
								"title": "경복궁",
								"category": "관광지",
								"reason": "대표 명소"
							}
						]
					}
				]
			}
			
			Be sure to return JSON only.
			Answer in Korean.
			
			[place list]
			관광지 list: %s
			음식점 list: %s
			숙소 list: %s
			""".formatted(
					request.getRegions(),
					request.getDuration(),
					request.getStyles(),
					planPlaceList,
					planFoodList,
					planLodgingList
			);
		
		String content = callGroq(prompt);
		
		try {
            // JSON 문자열 → List<AIRecDto>
			AIPlanResp response = objectMapper.readValue(content, new TypeReference<AIPlanResp>() {});
			
			for(PlanDays day : response.getDays()) {
				for(PlanPlace place : day.getSchedule()) {
					PlanPlace result = aiDao.selectPlaceById(place.getContentId());
					
					place.setFirstImage2(result.getFirstImage2());
					place.setAddr2(result.getAddr2());
					place.setMapX(result.getMapX());
					place.setMapY(result.getMapY());
				}
			}
						
			return response;
        } catch (Exception e) {
            throw new RuntimeException("AI 응답 파싱 실패: " + content, e);
        }
		
	}
	
	private String callGroq(String prompt) {
		// Groq AI 요청 Body 생성
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
        
        // (Groq) API 호출
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
        
        return content;
	}
	

}
