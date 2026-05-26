package com.jbro.plan.model.dao;

import java.util.List;

import com.jbro.plan.model.vo.PlanVo;

public interface PlanDao {

	List<PlanVo> selectPublicPlanList();

	PlanVo selectPlanDetail(Long id);

}
