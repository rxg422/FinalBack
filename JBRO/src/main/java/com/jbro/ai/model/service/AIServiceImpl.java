package com.jbro.ai.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jbro.ai.model.dto.AIRecDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AIServiceImpl implements AIService {
	
	public List<AIRecDto> aiRecommend(List<AIRecDto> request) {
		return null;
	}

}
