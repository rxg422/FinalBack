package com.jbro.tour.model.dto;

import lombok.Data;

@Data
public class FoodIntroDto {

	private int contentId;
	private String firstMenu;        // 대표메뉴
	private String infoCenterFood;   // 문의 및 안내
	private String kidsFacility;     // 어린이놀이방 여부
	private String openTimeFood;     // 영업시간
	private String packing;          // 포장 가능
	private String parkingFood;      // 주차시설
	private String reservationFood;  // 예약안내
	private String restDateFood;     // 쉬는날
	
}
