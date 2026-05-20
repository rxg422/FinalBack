package com.jbro.mypage.model.vo;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyPageReviewVo {

	private Long reviewId;
	private Long contentId;
	private String title;
	private String content;
	private String reviewImage;
	private LocalDateTime createdAt;
	private String reviewActive;
}
