package com.jbro.tourList.model.dao;

import com.jbro.tourList.model.dto.TourListSearchDto;
import com.jbro.tourList.model.vo.TourList;

import java.util.List;

public interface TourListDao {

    List<TourList> selectTourList(TourListSearchDto searchDto);

    int selectTourListCount(TourListSearchDto searchDto);

    TourList selectTourDetail(Long contentId);

    int updateViewCount(Long contentId);

    // 찜 토글 - 있으면 DELETE, 없으면 INSERT
    int selectFavoriteExists(Long contentId, Long userId);

    int insertFavorite(Long contentId, Long userId);

    int deleteFavorite(Long contentId, Long userId);
}
