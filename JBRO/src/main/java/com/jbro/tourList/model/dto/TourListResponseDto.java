package com.jbro.tourList.model.dto;

import com.jbro.tourList.model.vo.TourList;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class TourListResponseDto {
    private List<TourList> list;
    private int totalCount;
    private int currentPage;
    private int limit;

    public TourListResponseDto(List<TourList> list, int totalCount, int currentPage, int limit) {
        this.list = list;
        this.totalCount = totalCount;
        this.currentPage = currentPage;
        this.limit = limit;
    }
}