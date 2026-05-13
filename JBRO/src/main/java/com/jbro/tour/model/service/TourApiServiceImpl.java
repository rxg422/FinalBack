package com.jbro.tour.model.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbro.tour.model.dao.TourApiDaoImpl;
import com.jbro.tour.model.dto.CultureIntroDto;
import com.jbro.tour.model.dto.PlaceDetailDto;
import com.jbro.tour.model.dto.TourDetailIntroDto;
import com.jbro.tour.model.dto.PlaceDto;
import com.jbro.tour.model.dto.PlaceIntroDto;
import com.jbro.tour.model.vo.TourPlace;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TourApiServiceImpl implements TourApiService {

	/* 의존성 주입 */
	private final WebClient webClient;
	private final ObjectMapper objectMapper;
	private final TourApiDaoImpl tourApiDao;

	/* 상수 정의 */
	private static final String BASE_URL = "https://apis.data.go.kr/B551011/KorService2";
	private static final String SERVICE_KEY = "f9627dd46c0c9f97d293b27fdb6767a789bb707a1fb167cc9b556922b080c79d";

	@Override
	public void fetchAndSaveTourData() {
		int areaCode = 37;
		int pageNo = 1;
		int numOfRows = 100;

		List<PlaceDto> placeList;
		
		while (true) {
			/* 관광 목록 조회 */
			placeList = getTourPlaceList(areaCode, pageNo, numOfRows);

			if (placeList.isEmpty()) {
				break;
			}

			for (PlaceDto place : placeList) {
				try {
					if (place.getContentTypeId() != 12) {
						continue;
					}

					// 관광 기본 정보 조회 및 저장
					savePlace(place);

					// 관광 상세 정보 조회 및 저장
//					saveIntro(place.getContentId(), place.getContentTypeId());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			pageNo++;
		}
	}

	/* 관광 목록 조회 */
	private List<PlaceDto> getTourPlaceList(int areaCode, int pageNo, int numOfRows) {
		URI uri = UriComponentsBuilder
				.fromHttpUrl(BASE_URL + "/areaBasedList2")
				.queryParam("numOfRows", numOfRows)
				.queryParam("pageNo", pageNo)
				.queryParam("MobileOS", "ETC")
				.queryParam("MobileApp", "JBRO")
				.queryParam("serviceKey", SERVICE_KEY)
				.queryParam("_type", "json")
				.queryParam("areaCode", areaCode)
				.build(true).toUri();

		String response = webClient.get().uri(uri).retrieve().bodyToMono(String.class).block();
		
		List<PlaceDto> list = new ArrayList<>();

		try {
			JsonNode items = objectMapper.readTree(response).path("response").path("body").path("items").path("item");

			if (!items.isArray()) {
				return list;
			}
			
			System.out.println(items);

			for (JsonNode item : items) {
				PlaceDto dto = new PlaceDto();


				dto.setContentId(getInt(item, "contentid"));
				dto.setContentTypeId(getInt(item, "contenttypeid"));
				dto.setTitle(getText(item, "title"));
				dto.setFirstImage(getText(item, "firstimage"));
				dto.setFirstImage2(getText(item, "firstimage2"));
				dto.setAddr1(getText(item, "addr1"));
				dto.setAddr2(getText(item, "addr2"));
				dto.setMapX(getDouble(item, "mapx"));
				dto.setMapY(getDouble(item, "mapy"));

				list.add(dto);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/* 관광 기본 정보 조회 및 저장 */
	private void savePlace(PlaceDto dto) {
		try {
			URI uri = UriComponentsBuilder
					.fromHttpUrl(BASE_URL + "/detailCommon2")
					.queryParam("MobileOS", "ETC")
					.queryParam("MobileApp", "JBRO")
					.queryParam("serviceKey", SERVICE_KEY)
					.queryParam("_type", "json")
					.queryParam("contentId", dto.getContentId())
					.queryParam("defaultYN", "Y")
					.queryParam("overviewYN", "Y")
					.build(true).toUri();
			
			String response = webClient.get().uri(uri).retrieve().bodyToMono(String.class).block();
			
			JsonNode item = objectMapper.readTree(response).path("response").path("body").path("items").path("item").get(0);

			if (item == null) {
				return;
			}
			
			dto.setOverview(getText(item, "overview"));
			dto.setHomepage(getText(item, "homepage"));
			dto.setTel(getText(item, "tel"));
			dto.setTelName(getText(item, "telName"));
			
//			tourApiDao.insertPlace(dto);
//			System.out.println(dto);
		}
		catch (JsonMappingException e) {
			e.printStackTrace();
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	/* 소개 정보 조회 */
	private void saveIntro(int contentId, int contentTypeId) {
		try {
			URI uri = UriComponentsBuilder
					.fromHttpUrl(BASE_URL + "/detailIntro2")
					.queryParam("serviceKey", SERVICE_KEY)
					.queryParam("MobileOS", "ETC")
					.queryParam("MobileApp", "tour-app")
					.queryParam("_type", "json")
					.queryParam("contentId", contentId)
					.queryParam("contentTypeId", contentTypeId)
					.build(true).toUri();
			
			String response = webClient.get().uri(uri).retrieve().bodyToMono(String.class).block();
			
			JsonNode item = objectMapper.readTree(response).path("response").path("body").path("items").path("item").get(0);
			
			if (item == null) {
				return;
			}
			
			switch (contentTypeId) {
				case 12 :
					PlaceIntroDto placeDto = new PlaceIntroDto();
					
					placeDto.setContentId(contentId);
					placeDto.setInfoCenter(getText(item, "infocenter"));
					placeDto.setOpenDate(getText(item, "opendate"));
					placeDto.setParking(getText(item, "parking"));
					placeDto.setUseSeason(getText(item, "useseason"));
					placeDto.setUseTime(getText(item, "usetime"));
					
//					System.out.println(placeDto);
					
					break;
			}
		}
		catch (JsonMappingException e) {
			e.printStackTrace();
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	

	/* 공통 메서드 */
	private String getText(JsonNode item, String fieldName) {
		if (item == null || item.path(fieldName).isMissingNode()) {
			return "";
		}

		return item.path(fieldName).asText("");
	}
	
	private int getInt(JsonNode item, String fieldName) {
		if (item == null || item.path(fieldName).isMissingNode()) {
			return 0;
		}
		
		System.out.println(item.path(fieldName).asInt());
		
		return item.path(fieldName).asInt();
	}

	private double getDouble(JsonNode item, String fieldName) {
		if (item == null || item.path(fieldName).isMissingNode()) {
			return 0;
		}
		
		return item.path(fieldName).asDouble();
	}

}