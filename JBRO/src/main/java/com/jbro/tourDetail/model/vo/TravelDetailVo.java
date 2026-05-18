package com.jbro.tourDetail.model.vo;

import java.util.List;

import com.jbro.tour.model.dto.CultureIntroDto;
import com.jbro.tour.model.dto.FestivalIntroDto;
import com.jbro.tour.model.dto.FoodIntroDto;
import com.jbro.tour.model.dto.LeportsIntroDto;
import com.jbro.tour.model.dto.LodgingIntroDto;
import com.jbro.tour.model.dto.PlaceImageDto;
import com.jbro.tour.model.dto.PlaceIntroDto;
import com.jbro.tour.model.dto.ShopIntroDto;

import lombok.Data;

@Data
public class TravelDetailVo {
    // 기본 장소 정보
    private long contentId;
    private int contentTypeId;
    private int categoryId;
    private String title;
    private String firstImage;
    private String addr1;
    private String addr2;
    private double mapX;
    private double mapY;
    private String tel;
    private String homepage;
    private String overview;
    private long favoriteCount;
    private long reviewCount;
    private boolean favorited;

    // 이미지 목록
    private List<PlaceImageDto> images;

    // 카테고리별 상세정보 (하나만 채워짐)
    private PlaceIntroDto    placeIntro;
    private CultureIntroDto  cultureIntro;
    private FestivalIntroDto festivalIntro;
    private LeportsIntroDto  leportsIntro;
    private LodgingIntroDto  lodgingIntro;
    private ShopIntroDto     shopIntro;
    private FoodIntroDto     foodIntro;

    // 주변 여행지
    private List<NearbyPlaceVo> nearbyPlaces;
}