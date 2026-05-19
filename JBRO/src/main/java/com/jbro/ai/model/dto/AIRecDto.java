package com.jbro.ai.model.dto;

import lombok.Data;

// AI 추천 관광지 DTO
@Data
public class AIRecDto {

	private int contentId;
	private String title;	// 관광지 이름
	private String firstImage2;	// 대표 이미지
	private String addr1;	// 주소
	private String addr2;	// 상세주소
	private String reason;	// 추천 이유
	
}
