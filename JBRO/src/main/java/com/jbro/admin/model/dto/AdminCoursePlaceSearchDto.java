package com.jbro.admin.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminCoursePlaceSearchDto {
    private Long contentId;
    private String title;
    private String addr1;
    private String firstImage;
}