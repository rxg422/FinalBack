package com.jbro.ai.model.dao;

import java.util.List;

import com.jbro.ai.model.dto.AIRecDto;

public interface AIDao {

	List<AIRecDto> selectPlaceList(int[] areaCode);

}
