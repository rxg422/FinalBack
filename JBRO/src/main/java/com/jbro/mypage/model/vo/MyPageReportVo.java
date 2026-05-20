package com.jbro.mypage.model.vo;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyPageReportVo {

	private Long reportId;
	private Long reviewId;
	private Long contentId;
	private String title;
	private String reportType;
	private String reason;
	private String reportStatus;
	private LocalDateTime createdAt;
}
