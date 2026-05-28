package com.jbro.plan.model.service;

import java.util.List;

import com.jbro.plan.model.dto.PlannerDto.Planner;
import com.jbro.plan.model.vo.PlanVo;

public interface PlanService {
    List<Planner> getSharedPlanList();

	Planner getPlanDetail(int id);

	void insertPlanner(Planner plan);
}