package com.jbro.tour.model.dto;

import lombok.Data;

@Data
public class CultureIntroDto {

	private int contentId;
	private String infoCenterCulture;	// 문의및안내
	private String parkingCulture;		// 주차시설
	private String parkingFee; 			// 주차요금
	private String restDateCulture; 	// 쉬는날
	private String useFee; 				// 이용요금
	private String useTimeCulture;		// 이용시간
	private String spendTime;			// 관람소요시간
	
}
