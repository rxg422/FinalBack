package com.jbro.tour.model.dto;

import lombok.Data;

@Data
public class PlaceImageDto {
	
	private int contentId;
	private String imgName;			// 이미지명
	private String originImgUrl;	// 원본 이미지
	private String serialNum;		// 이미지 일련 번호
	private String smallImageUrl;	// 썸네일 이미지
	
}
