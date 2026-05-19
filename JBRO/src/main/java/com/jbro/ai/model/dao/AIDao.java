package com.jbro.ai.model.dao;

import java.util.List;

import com.jbro.ai.model.dto.AIDto.AIPlanPlace;
import com.jbro.ai.model.dto.AIDto.AIPlanReq;
import com.jbro.ai.model.dto.AIDto.AIPlanResp.PlanDays.PlanPlace;
import com.jbro.ai.model.dto.AIRecDto;

public interface AIDao {

	List<AIRecDto> selectPlaceList(int[] areaCode);

	List<Integer> selectSignguLsit(List<String> regions);

	List<AIPlanPlace> selectPlanPlaceList(AIPlanReq aiPlanReq);

	PlanPlace selectPlaceById(int contentId);

}
