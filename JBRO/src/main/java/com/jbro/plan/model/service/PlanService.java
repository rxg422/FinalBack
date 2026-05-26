package com.jbro.plan.model.service;

import com.jbro.plan.model.vo.PlanVo;
import java.util.List;

public interface PlanService {
    List<PlanVo> getSharedPlanList();

	PlanVo getPlanDetail(Long id);
}