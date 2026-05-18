package com.jbro.tourList.model.service;

import com.jbro.tourList.model.dto.TourListResponseDto;
import com.jbro.tourList.model.dto.TourListSearchDto;
import com.jbro.tourList.model.vo.TourList;

public interface TourListService {
    TourListResponseDto getTourList(TourListSearchDto searchDto);
    TourList getTourDetail(Long contentId);
    String toggleFavorite(Long contentId, Long userId); // "INSERT" or "DELETE" 반환
}