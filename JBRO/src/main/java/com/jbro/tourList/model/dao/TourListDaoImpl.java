package com.jbro.tourList.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.jbro.tourList.model.dto.TourListSearchDto;
import com.jbro.tourList.model.vo.TourList;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TourListDaoImpl implements TourListDao {

    private final SqlSessionTemplate sqlSession;

    @Override
    public List<TourList> selectTourList(TourListSearchDto searchDto) {
        return sqlSession.selectList(
                "tourListMapper.selectTourList",
                searchDto
        );
    }

    @Override
    public int selectTourListCount(TourListSearchDto searchDto) {
        return sqlSession.selectOne(
                "tourListMapper.selectTourListCount",
                searchDto
        );
    }
    @Override
    public int selectFavoriteCount(Long userId, Long contentId) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("contentId", contentId);

        return sqlSession.selectOne(
                "tourListMapper.selectFavoriteCount",
                param
        );
    }

    @Override
    public int insertFavorite(Long userId, Long contentId) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("contentId", contentId);

        return sqlSession.insert(
                "tourListMapper.insertFavorite",
                param
        );
    }

    @Override
    public int deleteFavorite(Long userId, Long contentId) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("contentId", contentId);

        return sqlSession.delete(
                "tourListMapper.deleteFavorite",
                param
        );
    }
}