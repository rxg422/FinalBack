package com.jbro.tour.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbro.tour.model.service.TourApiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tours")
@RequiredArgsConstructor
public class TourController {

	private final TourApiService tourService;
	
	// 외부 API 데이터를 가져와서 저장
	@PostMapping("/fetch")
	public String fetchTourData() {
		tourService.fetchAndSaveTourData();
		return "데이터 저장 완료";
	}
	
}
