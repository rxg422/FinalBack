package com.jbro.tourList.model.dto;

import com.jbro.tourList.model.vo.TourList;
import lombok.Data;

import java.util.List;

@Data
public class TourListResponseDto {

    private List<TourList> list;
    private int totalCount;
    private int currentPage;  // TSX: data.currentPage
    private int limit;        // TSX: data.limit

    public TourListResponseDto(List<TourList> list, int totalCount, int currentPage, int limit) {
        this.list = list;
        this.totalCount = totalCount;
        this.currentPage = currentPage;
        this.limit = limit;
    }
}
