package com.jbro.tour.model.dao;

import com.jbro.tour.model.dto.CultureIntroDto;
import com.jbro.tour.model.dto.FestivalIntroDto;
import com.jbro.tour.model.dto.FoodIntroDto;
import com.jbro.tour.model.dto.LDongDto;
import com.jbro.tour.model.dto.LeportsIntroDto;
import com.jbro.tour.model.dto.LodgingIntroDto;
import com.jbro.tour.model.dto.PlaceDto;
import com.jbro.tour.model.dto.PlaceImageDto;
import com.jbro.tour.model.dto.PlaceIntroDto;
import com.jbro.tour.model.dto.ShopIntroDto;
import com.jbro.tour.model.vo.TourPlace;

public interface TourApiDao {

	int findPlaceByContentId(int contentId);

	void insertPlace(PlaceDto dto);

	void insertPlaceIntro(PlaceIntroDto place);

	void insertCultureIntro(CultureIntroDto culture);

	void insertFestivalIntro(FestivalIntroDto festival);

	void insertLeportsIntro(LeportsIntroDto leports);

	void insertLodgingIntro(LodgingIntroDto lodging);

	void insertShopIntro(ShopIntroDto shopping);

	void insertFoodIntro(FoodIntroDto food);

	void insertPlaceImg(PlaceImageDto image);

	void insertlDong(LDongDto lDong);

}
