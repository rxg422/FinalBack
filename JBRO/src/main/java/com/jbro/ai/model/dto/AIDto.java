package com.jbro.ai.model.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

public class AIDto {
	
	@Data
	public static class AIPlanUserReq {
		private String duration;		// 여행 기간
		private List<String> regions;	// 지역
		private List<String> styles;	// 여행 테마
	}

	@Data
	@NoArgsConstructor
	public static class AIPlanReq {
		private int categoryId;
		private List<Integer> regions;
	}
	
	@Data
	public static class AIPlanPlace {
		private int contentId;
		private String title;
		private String addr1;
	}
	
	@Data
	public static class AIPlanResp {
		private String title;
		private String description;
		private PlanDays days[];
		private List<String> regions;
		private List<String> themas;
		
		@Data
		public static class PlanDays {
			private int day;
			private PlanPlace schedule[];
			
			@Data
			public static class PlanPlace {
				private int order;
				private int contentId;
				private String title;
				private String category;
				private String firstImage2;
				private String addr1;
				private String addr2;
				private double mapX;
				private double mapY;
				private String reason;
			}
		}
	}
	
	
	@Data
	public static class AIPlanner {
		private int id;
		private long userId;
		private String title;
		private String description;
	}
	
	@Data
	public static class AIRegion {
		private int plannerId;
		private int region;
	}
	
	@Data
	public static class AIThema {
		private int plannerId;
		private int thema;
	}
	
	@Data
	public static class AIDay {
		private int id;
		private int plannerId;
		private int day;
	}
	
	@Data
	public static class AIPlace {
		private int id;
		private int dayId;
		private int visitOrder;
		private int contentId;
		private String description;
	}
}
