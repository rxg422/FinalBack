package com.jbro.plan.controller;

import com.jbro.plan.model.service.PlanService;
import com.jbro.plan.model.vo.PlanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/plans")
@CrossOrigin(origins = "http://localhost:3000")
public class PlanController {
	
    @Autowired
    private PlanService planService;

    @GetMapping("/share")
    public ResponseEntity<List<PlanVo>> getSharedPlans() {
        return ResponseEntity.ok(planService.getSharedPlanList());
    }
    
    @GetMapping("/detail/{id}")
    public ResponseEntity<PlanVo> getPlanDetail(@PathVariable Long id) {
        return ResponseEntity.ok(planService.getPlanDetail(id));
    }
    
}