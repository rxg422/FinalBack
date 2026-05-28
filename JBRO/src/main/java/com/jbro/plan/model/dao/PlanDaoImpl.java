package com.jbro.plan.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbro.plan.model.dto.PlannerDto.PlanDay;
import com.jbro.plan.model.dto.PlannerDto.PlanPlace;
import com.jbro.plan.model.dto.PlannerDto.Planner;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlanDaoImpl implements PlanDao {
	
    private final SqlSessionTemplate session;

	@Override
	public List<Planner> selectPlanners() {
		return session.selectList("PlanMapper.selectPlannerList");
	}

	@Override
	public List<String> selectPlanRegions(int id) {
		return session.selectList("PlanMapper.selectPlanRegions", id);
	}

	@Override
	public List<String> selectPlanThemas(int id) {
		return session.selectList("PlanMapper.selectPlanThemas", id);
	}

	@Override
	public List<PlanDay> selectPlanDays(int id) {
		return session.selectList("PlanMapper.selectPlanDays", id);
	}

	@Override
	public List<PlanPlace> selectPlanSchedule(int id) {
		return session.selectList("PlanMapper.selectPlanSchedule", id);
	}

	@Override
	public Planner selectPlanner(int id) {
		return session.selectOne("PlanMapper.selectPlanner", id);
	}
}
