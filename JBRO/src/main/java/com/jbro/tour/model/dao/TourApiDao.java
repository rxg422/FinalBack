package com.jbro.tour.model.dao;

import com.jbro.tour.model.vo.TourPlace;

public interface TourApiDao {

	int existsByContentId(String contentId);

	void save(TourPlace place);

}
