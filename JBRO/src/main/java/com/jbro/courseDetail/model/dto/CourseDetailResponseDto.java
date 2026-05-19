package com.jbro.courseDetail.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class CourseDetailResponseDto {
    private long courseId;
    private String title;
    private String subtitle;
    private String duration;
    private String region;
    private String theme;
    private String totalDistance;
    private String historyYear;
    private String historySummary;
    private String introTitle;
    private String introText;
    private boolean multiDay;
    private String heroImage;
    private List<CourseDayDto> days;
    private List<String> tips;
    private List<PopularPlaceDto> popularPlaces;

    @Data
    public static class CourseDayDto {
        private int dayNo;
        private String dayTitle;
        private List<CoursePlaceDto> places;
    }

    @Data
    public static class CoursePlaceDto {
        private int placeOrder;
        private String placeDesc;
        private long contentId;
        private String title;
        private String address;
        private String tel;
        private double mapX;
        private double mapY;
        private String firstImage;
    }

    @Data
    public static class PopularPlaceDto {
        private long contentId;
        private String title;
        private String firstImage;
        private String categoryName;
    }
}