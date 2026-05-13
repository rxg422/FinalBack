package com.jbro.tour.model.dto;

import lombok.Data;

@Data
public class ShopIntroDto {

	private int contentId;
	private String cultureCenter;       // 문화센터 바로가기
	private String fairDay;             // 장서는날
	private String infoCenterShopping;  // 문의 및 안내
	private String openDateShopping;    // 개장일
	private String openTime;            // 영업시간
	private String parkingShopping;     // 주차시설
	private String restDateShopping;    // 쉬는날
	private String restroom;            // 화장실 설명
	private String shopGuide;           // 매장 안내
	
}
