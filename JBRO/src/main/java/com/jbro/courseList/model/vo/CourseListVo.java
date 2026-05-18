package com.jbro.courseList.model.vo;

import lombok.Data;

@Data
public class CourseListVo {
    private Long   courseId;
    private String title;
    private String subtitle;
    private String duration;
    private String theme;
    private String heroImage;
    private String regionName;   // LDONG.L_DONG_SIGNGU_NM
}