package com.jbro.tour.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
public class TourPlace {
	
	private Long id;
	
	/* TourPlace */
	private String contentId;		// 콘텐츠 ID
	private String contentTypeId;	// 관광 콘텐츠 구분
	private String title;			// 이름
	private String firstImage;		// 대표 이미지(원본)
	private String firstImage2;		// 대표 이미지(프로필)
	private String areaCode;		// 지역 코드
	private String addr1;			// 주소
	private String addr2;			// 상세 주소
	private double mapX;			// 경도
	private double mapY;			// 위도

	/* TourDetailCommon */
	private String telName;			// 전화번호명
	private String tel;				// 전화번호
	private String homepage;		// 홈페이지 주소
	private String overview;		// 콘텐츠 개요
	
	/* TourDetailInfo */
	private String infocenter; 		// 문의 및 안내
	private String parking; 		// 주차시설
	private String restdate;		// 쉬는날
	private String useTime; 		// 이용시간
	private String usefee;			// 이용요금
	private String spendtime;		// 관람소요시간
	
	private String opendate; 		// 개장일
	private String useSeason; 		// 이용시기
	
	private String parkingfee;		// 주차요금
	
	private	String agelimit;		// 관람가능연령
	private String bookingplace;	// 예매처
	private String eventedDate;		// 행사종료일
	private String eventHomepage;	// 행사 홈페이지
	private String sponsor1;		// 주최자정보
	private String sponsor1tel;		// 주최자연락처
	private String sponsor2;		// 주관사정보
	private String sponsor2tel;		// 주관사연락처
	
	
//	/* AreaBasedList */
//	private int contentId; // 콘텐츠 ID
//	private int contentTypeId; // 관광타입 ID
//
//	private String title; // 제목
//	
//	private String firstImage; // 대표 이미지(원본)
//	private String firstImage2; // 대포 이미지(썸네일)
//
//	private int lDongRegnCd; // 법정동 시도 코드
//	private int lDongSignguCd; // 법정동 시군구 코드
//
//	private String addr1; // 주소
//	private String addr2; // 상세 주소
//	
//	private double mapX; // GPS X좌표
//	private double mapY; // GPS Y좌표
//
//	private Date createdTime; // 등록일
//	private Date modifiedTime; // 수정일
//
//	private String tel; // 전화번호
//
//	private String lclsSystm1; // 분류체계 대분류
//	private String lclsSystm2; // 분류체계 중분류
//	private String lclsSystm3; // 분류체계 소분류
	
}