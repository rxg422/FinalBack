package com.jbro.mypage.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyPagePlannerCandidateVo {

	private Long contentId;
	private String title;
	private Integer contentTypeId;
	private Integer categoryId;
	private String categoryName;
	private String firstImage;
	private String firstImage2;
	private String addr1;
	private String region;
	private String description;
	private String reason;
}
