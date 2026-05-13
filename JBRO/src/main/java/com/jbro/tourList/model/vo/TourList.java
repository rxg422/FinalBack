package com.jbro.tourList.model.vo;

import java.util.Date;

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

    private Long lDongRegnCd;
    private Long lDongSignguCd;

    private String tel;
    private String telName;

    private String homepage;
    private String overview;

    private int favoriteCount;
    private String likedYn;

    private int reviewCount;
}