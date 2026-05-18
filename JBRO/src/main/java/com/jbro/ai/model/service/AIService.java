package com.jbro.ai.model.service;

import java.util.List;

import com.jbro.ai.model.dto.AIRecDto;

public interface AIService {

	List<AIRecDto> aiRecommend(int areaCode[]);
	
}
