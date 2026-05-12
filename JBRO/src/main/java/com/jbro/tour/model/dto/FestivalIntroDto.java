package com.jbro.tour.model.dto;

import lombok.Data;

@Data
public class FestivalIntroDto {

	private String agelimit;			// 관람가능연령
	private String bookingplace;		// 예매처
	private String eventeddate;			// 행사종료일
	private String eventhomepage;		// 행사홈페이지
	private String spendtimefestival;	// 관람소요시간
	private String sponsor1;			// 주최자정보
	private String sponsor1tel;			// 주최자연락처
	private String sponsor2;			// 주관사정보
	private String sponsor2tel;			// 주관사연락처
	private String usetimefestival;		// 이용요금
	
}
