package com.jbro.admin.model.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminCourseDetailDto {
    private Long courseId;
    private String title;
    private String subtitle;
    private String duration;
    private Integer region;
    private String regionName;
    private String theme;
    private String totalDistance;
    private String historyYear;
    private String historySummary;
    private String introTitle;
    private String introText;
    private String heroImage;
    private String activeYn;
    private List<AdminCourseDayDto> days;
    private List<String> tips;
}