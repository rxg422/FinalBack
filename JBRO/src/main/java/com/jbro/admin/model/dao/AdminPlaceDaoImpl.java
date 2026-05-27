package com.jbro.admin.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.jbro.admin.model.dto.AdminPlaceListDto;
import com.jbro.admin.model.dto.AdminPlaceReviewImageDto;

@Repository
public class AdminPlaceDaoImpl implements AdminPlaceDao {

    private final SqlSession sqlSession;
    private static final String NS = "com.jbro.admin.adminPlaceMapper.";

    public AdminPlaceDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public List<AdminPlaceListDto> selectPlaceList(Map<String, Object> param) {
        return sqlSession.selectList(NS + "selectPlaceList", param);
    }

    @Override
    public List<AdminPlaceReviewImageDto> selectReviewImages(Long contentId) {
        return sqlSession.selectList(NS + "selectReviewImages", contentId);
    }

    @Override
    public int insertPlaceImage(Map<String, Object> param) {
        return sqlSession.insert(NS + "insertPlaceImage", param);
    }

    @Override
    public int deletePlaceImage(Long imageId) {
        return sqlSession.delete(NS + "deletePlaceImage", imageId);
    }
    @Override
    public List<Map<String, Object>> selectPlaceImages(Long contentId) {
        return sqlSession.selectList(NS + "selectPlaceImages", contentId);
    }
    @Override
    public int updatePlaceFirstImage(Long contentId, String imageUrl) {
        Map<String, Object> param = new HashMap<>();
        param.put("contentId", contentId);
        param.put("imageUrl", imageUrl);
        return sqlSession.update(NS + "updatePlaceFirstImage", param);
    }
    @Override
    public int updatePlaceFirstImageIfEmpty(Long contentId, String imageUrl) {
        Map<String, Object> param = new HashMap<>();
        param.put("contentId", contentId);
        param.put("imageUrl", imageUrl);
        return sqlSession.update(NS + "updatePlaceFirstImageIfEmpty", param);
    }
    @Override
    public Map<String, Object> selectPlaceImageById(Long imageId) {
        return sqlSession.selectOne(NS + "selectPlaceImageById", imageId);
    }

    @Override
    public int clearFirstImageIfMatch(Long contentId, String imageUrl) {
        Map<String, Object> param = new HashMap<>();
        param.put("contentId", contentId);
        param.put("imageUrl", imageUrl);
        return sqlSession.update(NS + "clearFirstImageIfMatch", param);
    }
}