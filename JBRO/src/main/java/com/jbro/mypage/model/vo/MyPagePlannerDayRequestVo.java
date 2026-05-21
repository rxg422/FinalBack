package com.jbro.mypage.model.vo;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyPagePlannerDayRequestVo {

	private Integer dayNo;
	private List<MyPagePlannerPlaceRequestVo> places;
}
