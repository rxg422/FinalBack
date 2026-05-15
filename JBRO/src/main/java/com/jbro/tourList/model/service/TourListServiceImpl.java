package com.jbro.tourList.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jbro.tourList.model.dao.TourListDao;
import com.jbro.tourList.model.dto.TourListResponseDto;
import com.jbro.tourList.model.dto.TourListSearchDto;
import com.jbro.tourList.model.vo.TourList;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TourListServiceImpl implements TourListService {

    private final TourListDao tourListDao;

    @Override
    public TourListResponseDto selectTourList(TourListSearchDto searchDto) {

        List<TourList> list = tourListDao.selectTourList(searchDto);

        int totalCount = tourListDao.selectTourListCount(searchDto);

        return new TourListResponseDto(
                list,
                totalCount,
                searchDto.getPage(),
                searchDto.getLimit()
        );
    }
    @Override
    public String toggleFavorite(Long userId, Long contentId) {

        int count = tourListDao.selectFavoriteCount(userId, contentId);

        if (count > 0) {
            tourListDao.deleteFavorite(userId, contentId);
            return "DELETE";
        }

        tourListDao.insertFavorite(userId, contentId);
        return "INSERT";
    }
}