package com.jbro.tourList.model.service;

import com.jbro.tourList.model.dto.TourListResponseDto;
import com.jbro.tourList.model.dto.TourListSearchDto;
import com.jbro.tourList.model.vo.TourList;

public interface TourListService {

    TourListResponseDto getTourList(TourListSearchDto searchDto);

    TourList getTourDetail(Long contentId);

    // TSX: 응답 "INSERT" or "DELETE" 문자열 반환
    String toggleFavorite(Long contentId, Long userId);
}
