package com.jbro.plan.model.service;

import com.jbro.plan.model.dao.PlanDao;
import com.jbro.plan.model.vo.PlanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {
    @Autowired
    private PlanDao planDao;

    @Override
    public List<PlanVo> getSharedPlanList() {
        List<PlanVo> plans = planDao.selectPublicPlanList();
        for (PlanVo plan : plans) {
            // 지역명 가공 (예: "전북 전주시..." -> "전주")
            if (plan.getRegion() != null && plan.getRegion().contains(" ")) {
                String[] parts = plan.getRegion().split(" ");
                if(parts.length > 1) plan.setRegion(parts[1].replace("시", ""));
            }
            // 이미지가 없으면 기본 이미지
            if (plan.getImage() == null || plan.getImage().isEmpty()) {
                plan.setImage("/planner/planImg.png");
            }
        }
        return plans;
    }
    
    @Override
    public PlanVo getPlanDetail(Long id) {
        return planDao.selectPlanDetail(id);
    }
}