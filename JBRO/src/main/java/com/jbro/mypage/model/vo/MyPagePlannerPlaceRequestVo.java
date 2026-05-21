package com.jbro.mypage.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyPagePlannerPlaceRequestVo {

	private Integer visitOrder;
	private Long contentId;
	private String title;
	private String description;
}
