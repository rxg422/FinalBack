package com.jbro.ai.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.jbro.ai.model.dto.AIDto.AIDay;
import com.jbro.ai.model.dto.AIDto.AIPlace;
import com.jbro.ai.model.dto.AIDto.AIPlanPlace;
import com.jbro.ai.model.dto.AIDto.AIPlanReq;
import com.jbro.ai.model.dto.AIDto.AIPlanResp.PlanDays.PlanPlace;
import com.jbro.ai.model.dto.AIDto.AIPlanner;
import com.jbro.ai.model.dto.AIDto.AIRegion;
import com.jbro.ai.model.dto.AIDto.AIThema;
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

	@Override
	public List<Integer> selectSignguLsit(List<String> regions) {
		return session.selectList("aiMapper.selectSignguList", regions);
	}

	@Override
	public List<AIPlanPlace> selectPlanPlaceList(AIPlanReq aiPlanReq) {
		return session.selectList("aiMapper.selectPlanPlaceList", aiPlanReq);
	}

	@Override
	public PlanPlace selectPlaceById(int contentId) {
		return session.selectOne("aiMapper.selectPlaceById", contentId);
	}

	@Override
	public void insertAIPlan(AIPlanner aiPlan) {
		session.insert("aiMapper.insertAIPlan", aiPlan);
	}

	@Override
	public void insertAIDay(AIDay aiDay) {
		session.insert("aiMapper.insertAIDay", aiDay);
	}

	@Override
	public void insertAIPlace(AIPlace aiPlace) {
		session.insert("aiMapper.insertAIPlace", aiPlace);
	}

	@Override
	public void insertPlanRegion(AIRegion region) {
		session.insert("aiMapper.insertPlanRegion", region);
	}

	@Override
	public void insertPlanThema(AIThema thema) {
		session.insert("aiMapper.insertPlanThema", thema);
	}
	
	
	
}
