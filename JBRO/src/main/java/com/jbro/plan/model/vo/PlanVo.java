package com.jbro.plan.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanVo {
    // 1. 플래너 기본 정보 (리스트/상세 공통)
    private Long id;
    private String title;
    private String summary;
    private String author;
    private String region;
    private String theme;
    private String duration;
    private String image;

    // 2. 상세 페이지를 위한 날짜별 일정 리스트
    private List<DayPlanVo> days; 
}

/**
 * 날짜별 일정 그룹 (DAY 1, DAY 2...)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class DayPlanVo {
    private int day;
    private List<PlaceVo> schedule;
}

/**
 * 개별 장소 정보 (관광지, 음식점, 숙소)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class PlaceVo {
    private int order;        // 방문 순서
    private Long contentId;   // 장소 고유 ID
    private String title;     // 장소명
    private String category;  // 관광지/음식점/숙소
    private String firstImage2; // 이미지 URL
    private String reason;    // 사용자 설명 (PLACE_PLAN.DESCRIPTION)
    private double mapX;      // 경도
    private double mapY;      // 위도
}