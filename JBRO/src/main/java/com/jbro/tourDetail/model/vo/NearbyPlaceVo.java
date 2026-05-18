package com.jbro.tourDetail.model.vo;

import lombok.Data;

@Data
public class NearbyPlaceVo {
    private long contentId;
    private String title;
    private String firstImage;
    private String addr1;
    private int categoryId;
}