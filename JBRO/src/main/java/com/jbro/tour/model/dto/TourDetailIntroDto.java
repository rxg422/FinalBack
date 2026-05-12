package com.jbro.tour.model.dto;

import lombok.Data;

@Data
public class TourDetailIntroDto {
	
	/* 공통 정보 */
	private String infocenter; 	// 문의 및 안내
	private String parking; 	// 주차시설
	private String restdate;	// 쉬는날
	private String useTime; 	// 이용시간
	private String spendtime;	// 관람소요시간
	private String usefee;		// 이용요금
	
	/* 관광지 세부 정보 */
	private String opendate; 	// 개장일
	private String useSeason; 	// 이용시기
	
	/* 문화시설 세부 정보 */
	private String parkingfee;	// 주차요금
	
	
	/* 행사/공연/축제 세부 정보 */
	private	String agelimit;		// 관람가능연령
	private String bookingplace;	// 예매처
	private String eventedDate;		// 행사종료일
	private String eventHomepage;	// 행사 홈페이지
	private String sponsor1;		// 주최자정보
	private String sponsor1tel;		// 주최자연락처
	private String sponsor2;		// 주관사정보
	private String sponsor2tel;		// 주관사연락처
}
