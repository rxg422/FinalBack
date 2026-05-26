package com.jbro.mypage.model.vo;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyPagePlannerVo {

	private Long plannerId;
	private Long userId;
	private String title;
	private String description;
	private String isPublic;
	private String status;
	private LocalDateTime createdTime;
	private List<String> regions;
	private List<String> themas;
}
