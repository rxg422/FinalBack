package com.jbro.tour.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

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

import lombok.RequiredArgsConstructor;

@Repository
@Primary
@RequiredArgsConstructor
public class TourApiDaoImpl implements TourApiDao {

	private final SqlSessionTemplate session;
	
	@Override
	public int findPlaceByContentId(int contentId) {
		int result = session.selectOne("tourMapper.isTourPlace", contentId);
		
		return result;
	}

	@Override
	public void insertPlace(PlaceDto dto) {
		session.insert("tourMapper.insertTourPlace", dto);
	}

	@Override
	public void insertPlaceIntro(PlaceIntroDto place) {
		session.insert("tourMapper.insertPlaceIntro", place);
	}

	@Override
	public void insertCultureIntro(CultureIntroDto culture) {
		session.insert("tourMapper.insertCultureIntro", culture);
	}

	@Override
	public void insertFestivalIntro(FestivalIntroDto festival) {
		session.insert("tourMapper.insertFestivalIntro", festival);
	}

	@Override
	public void insertLeportsIntro(LeportsIntroDto leports) {
		session.insert("tourMapper.insertLeportsIntro", leports);
	}

	@Override
	public void insertLodgingIntro(LodgingIntroDto lodging) {
		session.insert("tourMapper.insertLodgingIntro", lodging);
	}

	@Override
	public void insertShopIntro(ShopIntroDto shopping) {
		System.out.println(shopping);
		session.insert("tourMapper.insertShopIntro", shopping);
	}

	@Override
	public void insertFoodIntro(FoodIntroDto food) {
		session.insert("tourMapper.insertFoodIntro", food);
	}

	@Override
	public void insertPlaceImg(PlaceImageDto image) {
		session.insert("tourMapper.insertPlaceImg", image);
	}

	@Override
	public void insertlDong(LDongDto lDong) {
		session.insert("tourMapper.insertLDong", lDong);
	}

}
