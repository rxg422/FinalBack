package com.jbro.tourList.model.vo;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TourList {
    private Long contentId;
    private Integer contentTypeId;
    private Integer categoryId;
    private String categoryName;
    private String title;
    private String firstImage;
    private String firstImage2;
    private String addr1;
    private String addr2;
    private Double mapX;
    private Double mapY;
    private Integer lDongRegnCd;
    private Integer lDongSignguCd;
    private String tel;
    private String telName;
    private String homepage;
    private String overview;
    private LocalDateTime createdTime;
    private Long viewCount;
    private String regionName;

    // TSX에서 필요한 필드
    private int favoriteCount;
    private String likedYn = "N"; // "Y" | "N"
    private int reviewCount;
}