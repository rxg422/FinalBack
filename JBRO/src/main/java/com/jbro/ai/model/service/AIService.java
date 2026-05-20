package com.jbro.ai.model.service;

import java.util.List;

import com.jbro.ai.model.dto.AIDto.AIPlanResp;
import com.jbro.ai.model.dto.AIDto.AIPlanUserReq;
import com.jbro.ai.model.dto.AIRecDto;

public interface AIService {

	List<AIRecDto> aiRecommend(int areaCode[]);

	AIPlanResp aiPlanner(AIPlanUserReq request);

	void insertAIPlan(AIPlanResp plan);
	
}
