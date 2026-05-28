package com.jbro.plan.model.dao;

import java.util.List;

import com.jbro.plan.model.dto.PlannerDto.PlanDay;
import com.jbro.plan.model.dto.PlannerDto.PlanPlace;
import com.jbro.plan.model.dto.PlannerDto.Planner;

public interface PlanDao {

	List<Planner> selectPlanners();

	List<String> selectPlanRegions(int id);

	List<String> selectPlanThemas(int id);

	List<PlanDay> selectPlanDays(int id);

	List<PlanPlace> selectPlanSchedule(int id);

	Planner selectPlanner(int id);

}
