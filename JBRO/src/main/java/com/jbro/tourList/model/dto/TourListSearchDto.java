package com.jbro.tourList.model.dto;

import lombok.Data;

@Data
public class TourListSearchDto {

    private Long categoryId;

    private String keyword;

    private String sort;

    private Long userId;

    private int page = 1;

    private int limit = 5;

    public int getOffset() {
        return (page - 1) * limit;
    }
}