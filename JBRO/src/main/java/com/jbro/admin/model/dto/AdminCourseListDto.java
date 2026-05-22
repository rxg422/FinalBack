package com.jbro.admin.model.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminCourseListDto {
    private Long courseId;
    private String title;
    private String regionName;
    private String theme;
    private String isMultiDay;
    private String activeYn;
    private LocalDateTime createdAt;
    private int dayCount;
}