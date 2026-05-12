package com.jbro.tour.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.jbro.tour.model.vo.TourPlace;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TourApiDaoImpl implements TourApiDao {

	private final SqlSessionTemplate session;
	
	@Override
	public int existsByContentId(String contentId) {
		int result = session.selectOne("tourMapper.isTourPlace", contentId);
		System.out.println("결과 : " + result);
		
		return result;
	}

	@Override
	public void save(TourPlace place) {
		session.insert("tourMapper.insertTourPlace", place);
		System.out.println();
	}

}
