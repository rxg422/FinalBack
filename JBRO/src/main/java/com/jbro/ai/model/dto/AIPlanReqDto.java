package com.jbro.ai.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIPlanReqDto {

	private String duration;
	private List<String> regions;
	private List<String> styles;
	
}
