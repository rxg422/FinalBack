package com.jbro.tourList.model.dao;

import com.jbro.tourList.model.dto.TourListSearchDto;
import com.jbro.tourList.model.vo.TourList;
import java.util.List;

public interface TourListDao {
    List<TourList> selectTourList(TourListSearchDto searchDto);
    int selectTourListCount(TourListSearchDto searchDto);
    TourList selectTourDetail(Long contentId);
    int updateViewCount(Long contentId);
    int selectFavoriteExists(Long contentId, Long userId);
    int insertFavorite(Long contentId, Long userId);
    int deleteFavorite(Long contentId, Long userId);
}