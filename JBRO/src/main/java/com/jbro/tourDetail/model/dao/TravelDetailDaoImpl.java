package com.jbro.tourDetail.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.jbro.tour.model.dto.CultureIntroDto;
import com.jbro.tour.model.dto.FestivalIntroDto;
import com.jbro.tour.model.dto.FoodIntroDto;
import com.jbro.tour.model.dto.LeportsIntroDto;
import com.jbro.tour.model.dto.LodgingIntroDto;
import com.jbro.tour.model.dto.PlaceDto;
import com.jbro.tour.model.dto.PlaceImageDto;
import com.jbro.tour.model.dto.PlaceIntroDto;
import com.jbro.tour.model.dto.ShopIntroDto;
import com.jbro.tourDetail.model.vo.NearbyPlaceVo;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TravelDetailDaoImpl implements TravelDetailDao {

    private final SqlSessionTemplate sqlSession;
    private static final String NS = "com.jbro.tourDetail.mapper.TravelDetailMapper.";

    @Override public PlaceDto            selectPlace(long contentId)       { return sqlSession.selectOne(NS + "selectPlace", contentId); }
    @Override public List<PlaceImageDto> selectImages(long contentId)      { return sqlSession.selectList(NS + "selectImages", contentId); }
    @Override public long                countFavorite(long contentId)     { return sqlSession.selectOne(NS + "countFavorite", contentId); }
    @Override public long                countReview(long contentId)       { return sqlSession.selectOne(NS + "countReview", contentId); }
    @Override public int                 isFavorited(long contentId, long userId) {
        Map<String, Object> p = new HashMap<>(); p.put("contentId", contentId); p.put("userId", userId);
        return sqlSession.selectOne(NS + "isFavorited", p);
    }

    @Override public PlaceIntroDto    selectPlaceIntro(long contentId)    { return sqlSession.selectOne(NS + "selectPlaceIntro",    contentId); }
    @Override public CultureIntroDto  selectCultureIntro(long contentId)  { return sqlSession.selectOne(NS + "selectCultureIntro",  contentId); }
    @Override public FestivalIntroDto selectFestivalIntro(long contentId) { return sqlSession.selectOne(NS + "selectFestivalIntro", contentId); }
    @Override public LeportsIntroDto  selectLeportsIntro(long contentId)  { return sqlSession.selectOne(NS + "selectLeportsIntro",  contentId); }
    @Override public LodgingIntroDto  selectLodgingIntro(long contentId)  { return sqlSession.selectOne(NS + "selectLodgingIntro",  contentId); }
    @Override public ShopIntroDto     selectShopIntro(long contentId)     { return sqlSession.selectOne(NS + "selectShopIntro",     contentId); }
    @Override public FoodIntroDto     selectFoodIntro(long contentId)     { return sqlSession.selectOne(NS + "selectFoodIntro",     contentId); }

    @Override public List<NearbyPlaceVo>       selectNearbyPlaces(double mapX, double mapY, long excludeId) {
        Map<String, Object> p = new HashMap<>();
        p.put("mapX", mapX); p.put("mapY", mapY); p.put("excludeId", excludeId);
        return sqlSession.selectList(NS + "selectNearbyPlaces", p);
    }
    @Override public List<Map<String, Object>> selectReviews(Map<String, Object> params) { return sqlSession.selectList(NS + "selectReviews", params); }

    @Override public void insertReview(Map<String, Object> params)  { sqlSession.insert(NS + "insertReview",  params); }
    @Override public void updateReview(Map<String, Object> params)  { sqlSession.update(NS + "updateReview",  params); }
    @Override public void deleteReview(Map<String, Object> params)  { sqlSession.update(NS + "deleteReview",  params); }
    @Override public void insertReport(Map<String, Object> params)  { sqlSession.insert(NS + "insertReport",  params); }

    @Override public void insertFavorite(long contentId, long userId) {
        Map<String, Object> p = new HashMap<>(); p.put("contentId", contentId); p.put("userId", userId);
        sqlSession.insert(NS + "insertFavorite", p);
    }
    @Override public void deleteFavorite(long contentId, long userId) {
        Map<String, Object> p = new HashMap<>(); p.put("contentId", contentId); p.put("userId", userId);
        sqlSession.delete(NS + "deleteFavorite", p);
    }
}