package com.jbro.admin.model.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminCourseDayDto {
    private Long courseDayId;
    private int dayNo;
    private String dayTitle;
    private List<AdminCoursePlaceDto> places;
}