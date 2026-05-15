package com.jbro.tourList.model.dao;

import java.util.List;

import com.jbro.tourList.model.dto.TourListSearchDto;
import com.jbro.tourList.model.vo.TourList;

public interface TourListDao {

    List<TourList> selectTourList(TourListSearchDto searchDto);

    int selectTourListCount(TourListSearchDto searchDto);
    
    int selectFavoriteCount(Long userId, Long contentId);

    int insertFavorite(Long userId, Long contentId);

    int deleteFavorite(Long userId, Long contentId);
}