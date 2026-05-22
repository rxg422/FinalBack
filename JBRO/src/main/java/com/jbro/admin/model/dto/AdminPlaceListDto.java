package com.jbro.admin.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminPlaceListDto {
    private Long contentId;
    private String title;
    private String addr1;
    private String firstImage;
    private int imageCount;    // PLACE_IMG 등록된 이미지 수
    private int reviewImageCount; // 리뷰 이미지 수
}