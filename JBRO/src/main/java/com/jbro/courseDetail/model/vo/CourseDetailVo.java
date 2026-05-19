package com.jbro.courseDetail.model.vo;

import lombok.Data;

@Data
public class CourseDetailVo {
    // COURSE
    private long courseId;
    private String title;
    private String subtitle;
    private String duration;
    private int regionCode;
    private String region;
    private String theme;
    private String totalDistance;
    private String historyYear;
    private String historySummary;
    private String introTitle;
    private String introText;
    private String isMultiDay;
    private String heroImage;

    // COURSE_DAY
    private int dayNo;
    private String dayTitle;

    // COURSE_PLACE
    private int placeOrder;
    private String placeDesc;

    // TOUR_PLACE
    private long contentId;
    private String placeName;
    private String addr1;
    private String tel;
    private double mapX;
    private double mapY;
    private String firstImage;
}