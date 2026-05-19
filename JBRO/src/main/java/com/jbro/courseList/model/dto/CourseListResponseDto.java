package com.jbro.courseList.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class CourseListResponseDto {
    private Long         courseId;
    private String       title;
    private String       subtitle;
    private String       duration;
    private String       theme;
    private String       heroImage;
    private String       regionName;
    private List<String> placeTitles;  // 최대 5개
}