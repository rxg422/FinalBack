package com.jbro.mypage.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyPagePlannerDetailPlaceVo {

	private Long placePlanId;
	private Integer visitOrder;
	private Long contentId;
	private String title;
	private String firstImage;
	private String firstImage2;
	private String addr1;
	private String categoryName;
	private String region;
	private Double mapX;
	private Double mapY;
	private String description;
}
