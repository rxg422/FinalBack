package com.jbro.tour.model.dto;

import lombok.Data;

@Data
public class LeportsIntroDto {

	private int contentId;
	private String expAgeRangeLeports;	// 체험 가능 연령
	private String infoCenterLeports;	// 문의 및 안내
	private String openPeriod;			// 개장 기간
	private String parkingLeports;		// 주차 시설
	private String parkingFeeLeports;	// 주차요금
	private String reservation; 		// 예약안내
	
}
