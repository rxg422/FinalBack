package com.jbro.tour.model.dto;

import lombok.Data;

@Data
public class FestivalIntroDto {

	private int contentId;
	private String ageLimit;			// 관람가능연령
	private String bookingPlace;		// 예매처
	private String eventedDate;			// 행사종료일
	private String eventHomepage;		// 행사홈페이지
	private String spendTimeFestival;	// 관람소요시간
	private String sponsor1;			// 주최자정보
	private String sponsor1Tel;			// 주최자연락처
	private String sponsor2;			// 주관사정보
	private String sponsor2Tel;			// 주관사연락처
	private String useTimeFestival;		// 이용요금
	
}
