package com.jbro.courseDetail.model.vo;

import lombok.Data;

@Data
public class CoursePopularPlaceVo {
    private long contentId;
    private String title;
    private String firstImage;
    private String categoryName;
    private int favoriteCount;
}