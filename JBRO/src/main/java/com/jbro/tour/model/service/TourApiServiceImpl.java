package com.jbro.tour.model.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbro.tour.model.dao.TourApiDao;
import com.jbro.tour.model.dto.CultureIntroDto;
import com.jbro.tour.model.dto.FestivalIntroDto;
import com.jbro.tour.model.dto.FoodIntroDto;
import com.jbro.tour.model.dto.LDongDto;
import com.jbro.tour.model.dto.LeportsIntroDto;
import com.jbro.tour.model.dto.LodgingIntroDto;
import com.jbro.tour.model.dto.PlaceDto;
import com.jbro.tour.model.dto.PlaceImageDto;
import com.jbro.tour.model.dto.PlaceIntroDto;
import com.jbro.tour.model.dto.ShopIntroDto;

import io.swagger.v3.oas.models.parameters.QueryParameter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class TourApiServiceImpl implements TourApiService {

	/* 의존성 주입 */
	private final WebClient webClient;
	private final ObjectMapper objectMapper;
	private final TourApiDao tourApiDao;

	/* 상수 정의 */
	private static final String BASE_URL = "https://apis.data.go.kr/B551011/KorService2";
	private static final String SERVICE_KEY = "f9627dd46c0c9f97d293b27fdb6767a789bb707a1fb167cc9b556922b080c79d";

	@Override
	public void fetchAndSaveTourData() {
		int areaCode = 37;
		int pageNo = 1;
		int numOfRows = 100;
		int lDongRegnCd = 52;

		List<PlaceDto> placeList;
		try {
			while (true) {
				/* 관광 목록 조회 */
				placeList = getTourPlaceList(areaCode, pageNo, numOfRows);

				Thread.sleep(100);

				if (placeList.isEmpty()) {
					break;
				}

				System.out.println(pageNo + "번째 데이터 저장");

				for (PlaceDto place : placeList) {
					if (place.getContentTypeId() == 25 || findPlaceByContentId(place.getContentId())) {
						System.out.println("Pass");
						continue;
					}

					System.out.println("관광 정보 저장");
					
					// 관광 기본 정보 조회 및 저장
					savePlace(place);
					Thread.sleep(100);

					// 관광 상세 정보 조회 및 저장
					saveIntro(place.getContentId(), place.getContentTypeId());
					Thread.sleep(100);

					// 관광지 이미지 정보 조회 및 저장
					saveImg(place.getContentId());
					Thread.sleep(100);
				}

				pageNo++;
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
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
				.queryParam("contentTypeId", 15)
				.build(true).toUri();

		String response = webClient.get().uri(uri).retrieve().bodyToMono(String.class).block();

		List<PlaceDto> list = new ArrayList<>();

		try {
			JsonNode items = objectMapper.readTree(response).path("response").path("body").path("items").path("item");

			if (!items.isArray()) {
				return list;
			}

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
				dto.setLDongRegnCd(getInt(item, "lDongRegnCd"));
				dto.setLDongSignguCd(getInt(item, "lDongSignguCd"));
				dto.setCreatedTime(getText(item, "createdtime"));

				switch (dto.getContentTypeId()) {
				case 12, 14, 28, 38:
					dto.setCategoryId(1);
					break;
				case 15:
					dto.setCategoryId(3);
					break;
				case 32:
					dto.setCategoryId(4);
					break;
				case 39:
					dto.setCategoryId(2);
					break;
				}

				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/* 관광 기본 정보 조회 및 저장 */
	private void savePlace(PlaceDto dto) {
		try {
			URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/detailCommon2").queryParam("MobileOS", "ETC")
					.queryParam("MobileApp", "JBRO").queryParam("serviceKey", SERVICE_KEY).queryParam("_type", "json")
					.queryParam("contentId", dto.getContentId()).build(true).toUri();

			String response = webClient.get().uri(uri).retrieve().bodyToMono(String.class).block();

			JsonNode item = objectMapper.readTree(response).path("response").path("body").path("items").path("item")
					.get(0);

			if (item == null) {
				return;
			}

			dto.setOverview(getText(item, "overview"));
			dto.setHomepage(getText(item, "homepage"));
			dto.setTel(getText(item, "tel"));
			dto.setTelName(getText(item, "telName"));

			tourApiDao.insertPlace(dto);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	/* 컨텐츠 별 세부정보 조회 및 저장 */
	private void saveIntro(int contentId, int contentTypeId) {
		try {
			URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/detailIntro2").queryParam("MobileOS", "ETC")
					.queryParam("MobileApp", "JBRO").queryParam("serviceKey", SERVICE_KEY).queryParam("_type", "json")
					.queryParam("contentId", contentId).queryParam("contentTypeId", contentTypeId).build(true).toUri();

			String response = webClient.get().uri(uri).retrieve().bodyToMono(String.class).block();

			JsonNode item = objectMapper.readTree(response).path("response").path("body").path("items").path("item")
					.get(0);

			if (item == null) {
				return;
			}

			switch (contentTypeId) {
			case 12:
				PlaceIntroDto place = new PlaceIntroDto();

				place.setContentId(contentId);
				place.setInfoCenter(getText(item, "infocenter"));
				place.setOpenDate(getText(item, "opendate"));
				place.setParking(getText(item, "parking"));
				place.setUseSeason(getText(item, "useseason"));
				place.setUseTime(getText(item, "usetime"));
				place.setRestDate(getText(item, "restdate"));

				tourApiDao.insertPlaceIntro(place);

				break;
			case 14:
				CultureIntroDto culture = new CultureIntroDto();

				culture.setContentId(contentId);
				culture.setInfoCenterCulture(getText(item, "infocenterculture")); // 문의및안내
				culture.setParkingCulture(getText(item, "parkingculture")); // 주차시설
				culture.setParkingFee(getText(item, "parkingfee")); // 주차요금
				culture.setRestDateCulture(getText(item, "restdateculture")); // 쉬는날
				culture.setUseFee(getText(item, "usefee")); // 이용요금
				culture.setUseTimeCulture(getText(item, "usetimeculture")); // 이용시간
				culture.setSpendTime(getText(item, "spendtime")); // 관람소요시간

				tourApiDao.insertCultureIntro(culture);

				break;
			case 15:
				FestivalIntroDto festival = new FestivalIntroDto();

				festival.setContentId(contentId);
				festival.setAgeLimit(getText(item, "agelimit"));
				festival.setBookingPlace(getText(item, "bookingplace"));
				festival.setEventStartDate(getText(item, "eventstartdate"));
				festival.setEventEndDate(getText(item, "eventenddate"));
				festival.setEventHomepage(getText(item, "eventhomepage"));
				festival.setSpendTimeFestival(getText(item, "spendtimefestival"));
				festival.setSponsor1(getText(item, "sponsor1"));
				festival.setSponsor1Tel(getText(item, "sponsor1tel"));
				festival.setSponsor2(getText(item, "sponsor2"));
				festival.setSponsor2Tel(getText(item, "sponsor2tel"));
				festival.setUseTimeFestival(getText(item, "usetimefestival"));
				festival.setPlayTime(getText(item, "playtime"));

				tourApiDao.insertFestivalIntro(festival);

				break;
			case 28:
				LeportsIntroDto leports = new LeportsIntroDto();

				leports.setContentId(contentId);
				leports.setExpAgeRangeLeports(getText(item, "expagerangeleports"));
				leports.setInfoCenterLeports(getText(item, "infocenterleports"));
				leports.setOpenPeriod(getText(item, "openperiod"));
				leports.setParkingFeeLeports(getText(item, "parkingfee"));
				leports.setParkingLeports(getText(item, "parking"));
				leports.setReservation(getText(item, "reservation"));

				tourApiDao.insertLeportsIntro(leports);

				break;
			case 32:
				LodgingIntroDto lodging = new LodgingIntroDto();

				lodging.setContentId(contentId);
				lodging.setAccomCountLodging(getText(item, "accomcountlodging"));
				lodging.setCheckInTime(getText(item, "checkintime"));
				lodging.setCheckOutTime(getText(item, "checkouttime"));
				lodging.setChkCooking(getText(item, "chkcooking"));
				lodging.setFoodPlace(getText(item, "foodplace"));
				lodging.setInfoCenterLodging(getText(item, "infocenterlodging"));
				lodging.setParkingLodging(getText(item, "parkinglodging"));
				lodging.setPickup(getText(item, "pickup"));
				lodging.setRoomCount(getText(item, "roomcount"));
				lodging.setReservationLodging(getText(item, "reservationlodging"));
				lodging.setReservationUrl(getText(item, "reservationurl"));
				lodging.setRoomType(getText(item, "roomtype"));
				lodging.setScaleLodging(getText(item, "scalelodging"));
				lodging.setSubFacility(getText(item, "subfacility"));
				lodging.setBarbecue(getText(item, "barbecue"));
				lodging.setBeauty(getText(item, "beauty"));
				lodging.setBeverage(getText(item, "beverage"));
				lodging.setBicycle(getText(item, "bicycle"));
				lodging.setCampfire(getText(item, "campfire"));
				lodging.setFitness(getText(item, "fitness"));
				lodging.setKaraoke(getText(item, "karaoke"));
				lodging.setPublicBath(getText(item, "publicbath"));
				lodging.setPublicPc(getText(item, "publicpc"));
				lodging.setSauna(getText(item, "sauna"));
				lodging.setSeminar(getText(item, "seminar"));
				lodging.setSports(getText(item, "sports"));
				lodging.setRefundRegulation(getText(item, "refundregulation"));

				tourApiDao.insertLodgingIntro(lodging);

				break;
			case 38:
				ShopIntroDto shopping = new ShopIntroDto();

				shopping.setContentId(contentId);
				shopping.setCultureCenter(getText(item, "culturecenter"));
				shopping.setFairDay(getText(item, "fairday"));
				shopping.setInfoCenterShopping(getText(item, "infocentershopping"));
				shopping.setOpenDateShopping(getText(item, "opendateshopping"));
				shopping.setOpenTime(getText(item, "opentime"));
				shopping.setParkingShopping(getText(item, "parkingshopping"));
				shopping.setRestDateShopping(getText(item, "restdateshopping"));
				shopping.setRestroom(getText(item, "restroom"));
				shopping.setShopGuide(getText(item, "shopguide"));

				tourApiDao.insertShopIntro(shopping);

				break;
			case 39:
				FoodIntroDto food = new FoodIntroDto();

				food.setContentId(contentId);
				food.setFirstMenu(getText(item, "firstmenu"));
				food.setInfoCenterFood(getText(item, "infocenterfood"));
				food.setKidsFacility(getText(item, "kidsfacility"));
				food.setOpenTimeFood(getText(item, "opentimefood"));
				food.setPacking(getText(item, "packing"));
				food.setParkingFood(getText(item, "parkingfood"));
				food.setReservationFood(getText(item, "reservationfood"));
				food.setRestDateFood(getText(item, "restdatefood"));

				tourApiDao.insertFoodIntro(food);

				break;
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	/* 관광장소 사진 조회 및 저장 */
	private void saveImg(int contentId) {
		try {
			URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/detailImage2").queryParam("MobileOS", "ETC")
					.queryParam("MobileApp", "JBRO").queryParam("serviceKey", SERVICE_KEY).queryParam("_type", "json")
					.queryParam("contentId", contentId).build(true).toUri();

			String response = webClient.get().uri(uri).retrieve().bodyToMono(String.class).block();

			JsonNode items = objectMapper.readTree(response).path("response").path("body").path("items").path("item");

			if (!items.isArray()) {
				return;
			}

			for (JsonNode item : items) {
				PlaceImageDto image = new PlaceImageDto();

				image.setContentId(contentId);
				image.setImgName(getText(item, "imgname"));
				image.setOriginImgUrl(getText(item, "originimgurl"));
				image.setSerialNum(getText(item, "serialnum"));
				image.setSmallImageUrl(getText(item, "smallimageurl"));

				tourApiDao.insertPlaceImg(image);
			}

		} catch (JsonProcessingException e) {
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

		return item.path(fieldName).asInt();
	}

	private double getDouble(JsonNode item, String fieldName) {
		if (item == null || item.path(fieldName).isMissingNode()) {
			return 0;
		}

		return item.path(fieldName).asDouble();
	}

	private boolean findPlaceByContentId(int contentId) {
		int result = tourApiDao.findPlaceByContentId(contentId);

		if (result > 0) {
			return true;
		}

		return false;
	}

}