package com.jbro.tourList.model.service;

import com.jbro.tourList.model.dto.TourListResponseDto;
import com.jbro.tourList.model.dto.TourListSearchDto;

public interface TourListService {

  
    
    TourListResponseDto selectTourList(TourListSearchDto searchDto);

    String toggleFavorite(Long userId, Long contentId);
}