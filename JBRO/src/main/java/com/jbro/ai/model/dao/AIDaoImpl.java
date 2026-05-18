package com.jbro.ai.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.jbro.ai.model.dto.AIRecDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AIDaoImpl implements AIDao {
	
	private final SqlSessionTemplate session;

	@Override
	public List<AIRecDto> selectPlaceList(int[] areaCode) {
		return session.selectList("aiMapper.selectPlaceList", areaCode);
	}
	
	
	
}
