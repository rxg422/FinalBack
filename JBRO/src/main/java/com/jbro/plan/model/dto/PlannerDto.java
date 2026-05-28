package com.jbro.plan.model.dto;

import java.util.List;

import com.jbro.ai.model.dto.AIDto;

import lombok.Data;

public class PlannerDto {

	@Data
	public static class Planner {
		private int id;
		private String nickName;
		private String title;
		private List<String> regions;
		private List<String> themas;
		private String description;
		private List<PlanDay> days;
		private String image;
		private String duration;
	}
	
	@Data
	public static class PlanDay {
		private int id;
		private int day;
		private List<PlanPlace> schedule;
	}
	
	@Data
	public static class PlanPlace {
		private int visitOrder;
		private int contentId;
		private String title;
		private String categoryId;
		private String firstImage2;
		private String addr1;
		private String addr2;
		private double mapX;
		private double mapY;
		private String description;
	}
	
	@Data
	public static class PlannerReq {
		private int id;
		private Long userId;
		private String title;
		private String description;
	}
	
	@Data
	public static class RegionReq {
		private int plannerId;
		private int region;
	}
	
	@Data
	public static class ThemaReq {
		private int plannerId;
		private int thema;
	}
	
	@Data
	public static class DayReq {
		private int id;
		private int plannerId;
		private int day;
	}
	
	@Data
	public static class PlaceReq {
		private int id;
		private int dayId;
		private int visitOrder;
		private int contentId;
		private String description;
	}
	
}
