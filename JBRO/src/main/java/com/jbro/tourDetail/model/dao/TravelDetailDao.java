package com.jbro.tourDetail.model.dao;

import com.jbro.tour.model.dto.*;
import com.jbro.tourDetail.model.vo.NearbyPlaceVo;

import java.util.List;
import java.util.Map;

public interface TravelDetailDao {
    PlaceDto            selectPlace(long contentId);
    List<PlaceImageDto> selectImages(long contentId);
    long                countFavorite(long contentId);
    long                countReview(long contentId);
    int                 isFavorited(long contentId, long userId);

    PlaceIntroDto    selectPlaceIntro(long contentId);
    CultureIntroDto  selectCultureIntro(long contentId);
    FestivalIntroDto selectFestivalIntro(long contentId);
    LeportsIntroDto  selectLeportsIntro(long contentId);
    LodgingIntroDto  selectLodgingIntro(long contentId);
    ShopIntroDto     selectShopIntro(long contentId);
    FoodIntroDto     selectFoodIntro(long contentId);

    List<NearbyPlaceVo>       selectNearbyPlaces(double mapX, double mapY, long excludeId);
    List<Map<String, Object>> selectReviews(Map<String, Object> params);

    void insertReview(Map<String, Object> params);
    void updateReview(Map<String, Object> params);
    void deleteReview(Map<String, Object> params);
    void insertFavorite(long contentId, long userId);
    void deleteFavorite(long contentId, long userId);
    void insertReport(Map<String, Object> params);
}