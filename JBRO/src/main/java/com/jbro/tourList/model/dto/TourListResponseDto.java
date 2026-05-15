package com.jbro.tourList.model.dto;

import java.util.List;

import com.jbro.tourList.model.vo.TourList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourListResponseDto {

    private List<TourList> list;

    private int totalCount;

    private int currentPage;

    private int limit;
}