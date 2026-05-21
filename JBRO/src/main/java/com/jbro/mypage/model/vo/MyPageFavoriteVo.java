package com.jbro.mypage.model.vo;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyPageFavoriteVo {

	private Long favoriteId;
	private Long contentId;
	private Integer contentTypeId;
	private Integer categoryId;
	private String categoryName;
	private String title;
	private String firstImage;
	private String addr1;
	private String region;
	private LocalDateTime createdAt;
}
