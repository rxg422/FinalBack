package com.jbro.tourList.model.vo;

import lombok.Data;

@Data
public class TourList {
    private Long contentId;
    private Long contentTypeId;
    private Long categoryId;
    private String categoryName;
    private String title;
    private String firstImage;
    private String firstImage2;
    private String addr1;
    private String addr2;
    private Double mapX;
    private Double mapY;
    private Long lDongSignguCd;
    private String lDongSignguNm;
    private Long viewCount;

    // 찜
    private Long favoriteCount;
    private String likedYn; // "Y" / "N"

    // 리뷰
    private Long reviewCount;
}
