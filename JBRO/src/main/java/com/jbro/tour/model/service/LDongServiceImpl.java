package com.jbro.tour.model.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbro.tour.model.dao.TourApiDao;
import com.jbro.tour.model.dto.LDongDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LDongServiceImpl implements LDongService {
	
	private final WebClient webClient;
	private final ObjectMapper objectMapper;
	private final TourApiDao tourApiDao;
	
	private static final String BASE_URL = "https://apis.data.go.kr/B551011/KorService2";
	private static final String SERVICE_KEY = "f9627dd46c0c9f97d293b27fdb6767a789bb707a1fb167cc9b556922b080c79d";
	
	@Override
	public void fetchAndSaveLDongData() {
		int pageNo = 1;
		int numOfRows = 100;
		int lDongRegnCd = 52;
		
		try {
			while(true) {
				List<LDongDto> list = fetchLDong(pageNo, numOfRows, lDongRegnCd);
				Thread.sleep(100);
				
				if (list.isEmpty()) {
					break;
				}
				
				for (LDongDto lDong : list) {
//					tourApiDao.insertlDong(lDong);
				}
				
				pageNo++;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private List<LDongDto> fetchLDong(int pageNo, int numOfRows, int lDongRegnCd) {
		List<LDongDto> list = new ArrayList<>();
		LDongDto lDong;

		try {
			URI uri = UriComponentsBuilder
					.fromHttpUrl(BASE_URL + "/ldongCode2")
					.queryParam("numOfRows", numOfRows)
					.queryParam("pageNo", pageNo)
					.queryParam("MobileOS", "ETC")
					.queryParam("MobileApp", "JBRO")
					.queryParam("serviceKey", SERVICE_KEY)
					.queryParam("_type", "json")
					.queryParam("lDongRegnCd", lDongRegnCd)
					.queryParam("lDongListYn", "Y")
					.build(true).toUri();
			
			String response = webClient.get().uri(uri).retrieve().bodyToMono(String.class).block();
			
			JsonNode items = objectMapper.readTree(response).path("response").path("body").path("items").path("item");
			
			
			if (!items.isArray()) {
				return list;
			}
			
			for (JsonNode item : items) {
				lDong = new LDongDto();
				
				lDong.setLDongSignguCd(item.path("lDongSignguCd").asInt());
				lDong.setLDongSignguNm(item.path("lDongSignguNm").asText());
				
				list.add(lDong);
			}
			
			
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return list;
	}

}
