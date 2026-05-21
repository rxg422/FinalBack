package com.jbro.mypage.model.vo;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyPagePlannerDetailDayVo {

	private Long dayId;
	private Integer dayNo;
	private List<MyPagePlannerDetailPlaceVo> places;
}
