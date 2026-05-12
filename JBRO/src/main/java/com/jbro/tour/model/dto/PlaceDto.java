package com.jbro.tour.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PlaceDto {

	private int contentId; // 콘텐츠 ID
	private String category; // 관광 타입(전북로 기준)
	private int ContentTypeId; // 관광 타입 (API 기준)
	private String title; // 관광지 이름	
	private String firstImage; // 대표 이미지
	private String firstImage2;
	private String addr1; // 주소
	private String addr2; // 상세 주소
	private double mapX; // 경도
	private double mapY; // 위도
	private String lDongRegnCd; // 지역 코드


	private String tel;	// 전화번호
	private String telName; // 전화명
	private String homepage; // 홈페이지
	private String overview; // 개요

}