package com.jbro.tourList.model.dao;

import com.jbro.tourList.model.dto.TourListSearchDto;
import com.jbro.tourList.model.vo.TourList;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TourListDaoImpl implements TourListDao {

    private static final String NS = "com.jbro.tourList.mapper.TourListMapper.";

    @Autowired
    private SqlSessionTemplate sqlSession;

    @Override
    public List<TourList> selectTourList(TourListSearchDto searchDto) {
        return sqlSession.selectList(NS + "selectTourList", searchDto);
    }

    @Override
    public int selectTourListCount(TourListSearchDto searchDto) {
        return sqlSession.selectOne(NS + "selectTourListCount", searchDto);
    }

    @Override
    public TourList selectTourDetail(Long contentId) {
        return sqlSession.selectOne(NS + "selectTourDetail", contentId);
    }

    @Override
    public int updateViewCount(Long contentId) {
        return sqlSession.update(NS + "updateViewCount", contentId);
    }

    @Override
    public int selectFavoriteExists(Long contentId, Long userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("contentId", contentId);
        map.put("userId", userId);
        return sqlSession.selectOne(NS + "selectFavoriteExists", map);
    }

    @Override
    public int insertFavorite(Long contentId, Long userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("contentId", contentId);
        map.put("userId", userId);
        return sqlSession.insert(NS + "insertFavorite", map);
    }

    @Override
    public int deleteFavorite(Long contentId, Long userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("contentId", contentId);
        map.put("userId", userId);
        return sqlSession.delete(NS + "deleteFavorite", map);
    }
}
