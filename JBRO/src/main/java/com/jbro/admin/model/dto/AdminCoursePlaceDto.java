package com.jbro.admin.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminCoursePlaceDto {
    private Long coursePlaceId;
    private Long placeId;
    private String placeTitle;
    private String placeDesc;
    private int placeOrder;
}