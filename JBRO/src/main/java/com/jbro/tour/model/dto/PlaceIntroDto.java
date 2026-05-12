package com.jbro.tour.model.dto;

import lombok.Data;

@Data
public class PlaceIntroDto {

	private int contentId;
	private String infoCenter; 	// 문의 및 안내
	private String openDate; 	// 개장일
	private String parking; 	// 주차시설
	private String restDate;	// 쉬는날
	private String useSeason; 	// 이용시기
	private String useTime; 	// 이용시간
	
}
