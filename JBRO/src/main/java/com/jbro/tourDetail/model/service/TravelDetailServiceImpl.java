package com.jbro.tourDetail.model.service;


import com.jbro.tour.model.dto.*;
import com.jbro.tourDetail.model.dao.TravelDetailDao;
import com.jbro.tourDetail.model.vo.NearbyPlaceVo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TravelDetailServiceImpl implements TravelDetailService {

    private final TravelDetailDao travelDetailDao;

    // 이미지 저장 경로 (환경에 맞게 수정)
    private static final String UPLOAD_DIR =
            System.getProperty("user.dir") + "/uploads/reviews/";
    @Override
    public Map<String, Object> getDetail(long contentId, Long userId) {
        PlaceDto place = travelDetailDao.selectPlace(contentId);
        if (place == null) return Collections.emptyMap();

        // 찜 여부 / 카운트
        long favoriteCount = travelDetailDao.countFavorite(contentId);
        long reviewCount   = travelDetailDao.countReview(contentId);
        boolean favorited  = userId != null && travelDetailDao.isFavorited(contentId, userId) > 0;

        // 이미지
        List<PlaceImageDto> images = travelDetailDao.selectImages(contentId);

        // 카테고리별 상세정보
        int typeId = place.getContentTypeId();
        Object intro = null;
        String introKey = null;
        if      (typeId == 12) { intro = travelDetailDao.selectPlaceIntro(contentId);    introKey = "placeIntro";    }
        else if (typeId == 14) { intro = travelDetailDao.selectCultureIntro(contentId);  introKey = "cultureIntro";  }
        else if (typeId == 15) { intro = travelDetailDao.selectFestivalIntro(contentId); introKey = "festivalIntro"; }
        else if (typeId == 28) { intro = travelDetailDao.selectLeportsIntro(contentId);  introKey = "leportsIntro";  }
        else if (typeId == 32) { intro = travelDetailDao.selectLodgingIntro(contentId);  introKey = "lodgingIntro";  }
        else if (typeId == 38) { intro = travelDetailDao.selectShopIntro(contentId);     introKey = "shopIntro";     }
        else if (typeId == 39) { intro = travelDetailDao.selectFoodIntro(contentId);     introKey = "foodIntro";     }

        // 주변 여행지 (반경 5km, 최대 6개)
        List<NearbyPlaceVo> nearby = travelDetailDao.selectNearbyPlaces(
                place.getMapX(), place.getMapY(), contentId);

        // 응답 조립
        Map<String, Object> detail = new LinkedHashMap<>();
        detail.put("contentId",      place.getContentId());
        detail.put("contentTypeId",  place.getContentTypeId());
        detail.put("categoryId",     place.getCategoryId());
        detail.put("title",          place.getTitle());
        detail.put("firstImage",     place.getFirstImage());
        detail.put("addr1",          place.getAddr1());
        detail.put("addr2",          place.getAddr2());
        detail.put("mapX",           place.getMapX());
        detail.put("mapY",           place.getMapY());
        detail.put("tel",            place.getTel());
        detail.put("homepage",       place.getHomepage());
        detail.put("overview",       place.getOverview());
        detail.put("favoriteCount",  favoriteCount);
        detail.put("reviewCount",    reviewCount);
        detail.put("favorited",      favorited);

        Map<String, Object> detailInfo = new LinkedHashMap<>();
        if (intro != null) detailInfo.put(introKey, intro);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("detail",       detail);
        result.put("images",       images);
        result.put("detailInfo",   detailInfo);
        result.put("nearbyPlaces", nearby);
        return result;
    }

    @Override
    public Map<String, Object> getReviews(long contentId, int page, int size) {
        int offset = (page - 1) * size;
        Map<String, Object> params = new HashMap<>();
        params.put("contentId", contentId);
        params.put("offset",    offset);
        params.put("size",      size);

        List<Map<String, Object>> reviews = travelDetailDao.selectReviews(params);
        long totalCount = travelDetailDao.countReview(contentId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("reviews",    reviews);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    public void createReview(long contentId, long userId, String content, MultipartFile image) {
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = saveImage(image);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("userId",    userId);
        params.put("contentId", contentId);
        params.put("content",   content);
        params.put("imageUrl",  imageUrl);
        travelDetailDao.insertReview(params);
    }

    @Override
    public void updateReview(long reviewId, long userId, String content) {
        Map<String, Object> params = new HashMap<>();
        params.put("reviewId", reviewId);
        params.put("userId",   userId);
        params.put("content",  content);
        travelDetailDao.updateReview(params);
    }

    @Override
    public void deleteReview(long reviewId, long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("reviewId", reviewId);
        params.put("userId",   userId);
        travelDetailDao.deleteReview(params);
    }

    @Override
    public void toggleFavorite(long contentId, long userId) {
        int exists = travelDetailDao.isFavorited(contentId, userId);
        if (exists > 0) {
            travelDetailDao.deleteFavorite(contentId, userId);
        } else {
            travelDetailDao.insertFavorite(contentId, userId);
        }
    }

    @Override
    public void reportReview(long reviewId, long userId, String reportType, String reason) {
        Map<String, Object> params = new HashMap<>();
        params.put("reviewId",   reviewId);
        params.put("userId",     userId);
        params.put("reportType", reportType);
        params.put("reason",     reason);
        travelDetailDao.insertReport(params);
    }

    private String saveImage(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File dest = new File(UPLOAD_DIR + fileName);
            dest.getParentFile().mkdirs();
            file.transferTo(dest);
            return "/uploads/reviews/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }
}