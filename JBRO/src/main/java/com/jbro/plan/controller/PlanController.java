package com.jbro.plan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbro.plan.model.dto.PlannerDto.Planner;
import com.jbro.plan.model.dto.PlannerDto.PlannerReq;
import com.jbro.plan.model.service.PlanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plans")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class PlanController {
	
    private final PlanService planService;

    @GetMapping("/share")
    public ResponseEntity<List<Planner>> getSharedPlans() {
        return ResponseEntity.ok(planService.getSharedPlanList());
    }
    
    @GetMapping("/detail/{id}")
    public ResponseEntity<Planner> getPlanDetail(@PathVariable int id) {
        return ResponseEntity.ok(planService.getPlanDetail(id));
    }
    
    @PostMapping("/insertPlan")
    public ResponseEntity<Void> insertPlanner(@RequestBody Planner plan) {
    	planService.insertPlanner(plan);
    	
    	return ResponseEntity.ok().build();
    }
    
}