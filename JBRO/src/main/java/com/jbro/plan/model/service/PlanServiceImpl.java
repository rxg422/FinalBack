package com.jbro.plan.model.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jbro.ai.model.dao.AIDao;
import com.jbro.ai.model.dto.AIDto.AIDay;
import com.jbro.ai.model.dto.AIDto.AIPlace;
import com.jbro.ai.model.dto.AIDto.AIPlanner;
import com.jbro.ai.model.dto.AIDto.AIRegion;
import com.jbro.ai.model.dto.AIDto.AIThema;
import com.jbro.plan.model.dao.PlanDao;
import com.jbro.plan.model.dto.PlannerDto.PlanDay;
import com.jbro.plan.model.dto.PlannerDto.PlanPlace;
import com.jbro.plan.model.dto.PlannerDto.Planner;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
	
	private final PlanDao planDao;
	private final AIDao aiDao;

	@Override
	public List<Planner> getSharedPlanList() {
		// 플래너 리스트 조회
		List<Planner> plans = planDao.selectPlanners();
		
		for(Planner plan : plans) {
			// 플래너 지역 조회 및 저장
			plan.setRegions(planDao.selectPlanRegions(plan.getId()));
			// 플래너 테마 조회 및 저장
			plan.setThemas(planDao.selectPlanThemas(plan.getId()));
			// Day 리스트 조회 및 저장
			plan.setDays(planDao.selectPlanDays(plan.getId()));
			
			for(PlanDay day : plan.getDays()) {
				// 일정 조회 및 저장
				day.setSchedule(planDao.selectPlanSchedule(day.getId()));
			}
			
			String image = plan.getDays().get(0).getSchedule().get(0).getFirstImage2();
			if (image == null || image.isEmpty()) {
				image = "/planner/planImg.png";
			}
			plan.setImage(image);
			
			String duration = plan.getDays().size()-1 + "박 " + plan.getDays().size() + "일";
			plan.setDuration(duration);
		}

		return plans;
	}

	@Override
	public Planner getPlanDetail(int id) {
		Planner plan = planDao.selectPlanner(id);
		
		plan.setRegions(planDao.selectPlanRegions(plan.getId()));
		plan.setThemas(planDao.selectPlanThemas(plan.getId()));
		plan.setDays(planDao.selectPlanDays(id));
		
		for(PlanDay day : plan.getDays()) {
			day.setSchedule(planDao.selectPlanSchedule(day.getId()));
		}
		
		return plan;
	}

	@Override
	public void insertPlanner(Planner plan) {
		AIPlanner planner = new AIPlanner();
		
		planner.setTitle(plan.getTitle());
		planner.setUserId(getCurrentUserId());
		planner.setDescription(plan.getDescription());
		
		aiDao.insertAIPlan(planner);
		
		for (String regionNm : plan.getRegions()) {
			AIRegion region = new AIRegion();
			int regionCd = 110;
			
			switch (regionNm) {
			case "전주시" : regionCd = 110; break;
			case "군산시" : regionCd = 130; break;
			case "익산시" : regionCd = 140; break;
			case "정읍시" : regionCd = 180; break;
			case "남원시" : regionCd = 190; break;
			case "김제시" : regionCd = 210; break;
			case "완주군" : regionCd = 710; break;
			case "진완군" : regionCd = 720; break;
			case "무주군" : regionCd = 730; break;
			case "장수군" : regionCd = 740; break;
			case "임실군" : regionCd = 750; break;
			case "순창군" : regionCd = 770; break;
			case "고창군" : regionCd = 790; break;
			case "부안군" : regionCd = 800; break;
			}
			
			region.setPlannerId(planner.getId());
			region.setRegion(regionCd);
			
			aiDao.insertPlanRegion(region);
		}
		
		for (String themaNm : plan.getThemas()) {
			AIThema thema = new AIThema();
			
			int themaId = 1;
			
			switch (themaNm) {
			case "역사" : themaId = 1; break;
			case "문화" : themaId = 2; break;
			case "자연·힐링" : themaId = 3; break;
			case "야경·감성" : themaId = 4; break;
			case "체험·액티비티" : themaId = 5; break;
			case "가족·아이" : themaId = 6; break;
			case "혼자 여행" : themaId = 7; break;
			case "반려동물" : themaId = 8; break;
			}
			
			thema.setPlannerId(planner.getId());
			thema.setThema(themaId);
			
			aiDao.insertPlanThema(thema);
		}
		
		for (PlanDay dayPlan : plan.getDays()) {
			AIDay aiDay = new AIDay();
			
			aiDay.setPlannerId(planner.getId());
			aiDay.setDay(dayPlan.getDay());
			
			aiDao.insertAIDay(aiDay);
			
			for (PlanPlace place : dayPlan.getSchedule()) {
				AIPlace aiPlace = new AIPlace();
				
				aiPlace.setDayId(aiDay.getId());
				aiPlace.setVisitOrder(place.getVisitOrder());
				aiPlace.setContentId(place.getContentId());
				aiPlace.setDescription(place.getDescription());
				
				aiDao.insertAIPlace(aiPlace);
			}
		}
			
	}
	
	private Long getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Long) {
            return (Long) auth.getPrincipal();
        }
        return null;
    }
}