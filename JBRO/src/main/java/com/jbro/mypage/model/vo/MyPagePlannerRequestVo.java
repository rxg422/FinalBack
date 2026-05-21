package com.jbro.mypage.model.vo;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyPagePlannerRequestVo {

	private String title;
	private String description;
	private String isPublic;
	private String status;
	private List<String> regions;
	private List<MyPagePlannerDayRequestVo> days;
}
