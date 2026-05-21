package com.jbro.mypage.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyPagePlannerDayVo {

	private Long dayId;
	private Long plannerId;
	private Integer dayNo;
}
