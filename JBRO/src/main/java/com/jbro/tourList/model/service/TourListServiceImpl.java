package com.jbro.tourList.model.service;

import com.jbro.tourList.model.dao.TourListDao;
import com.jbro.tourList.model.dto.TourListResponseDto;
import com.jbro.tourList.model.dto.TourListSearchDto;
import com.jbro.tourList.model.vo.TourList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TourListServiceImpl implements TourListService {

    @Autowired
    private TourListDao tourListDao;

    @Override
    public TourListResponseDto getTourList(TourListSearchDto searchDto) {
        List<TourList> list = tourListDao.selectTourList(searchDto);
        int totalCount = tourListDao.selectTourListCount(searchDto);
        return new TourListResponseDto(list, totalCount, searchDto.getPage(), searchDto.getLimit());
    }

    @Override
    @Transactional
    public TourList getTourDetail(Long contentId) {
        tourListDao.updateViewCount(contentId);
        return tourListDao.selectTourDetail(contentId);
    }

    @Override
    @Transactional
    public String toggleFavorite(Long contentId, Long userId) {
        int exists = tourListDao.selectFavoriteExists(contentId, userId);
        if (exists > 0) {
            tourListDao.deleteFavorite(contentId, userId);
            return "DELETE";
        } else {
            tourListDao.insertFavorite(contentId, userId);
            return "INSERT";
        }
    }
}
