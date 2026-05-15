package com.jbro.tourList.model.dto;

import lombok.Data;

@Data
public class TourListSearchDto {

    // 카테고리 (1:관광지 2:음식점 3:축제 4:숙소)
    private Long categoryId;

    // 지역명 텍스트 (TSX에서 "전주", "군산" 등 문자열로 넘김)
    private String region;

    // 키워드
    private String keyword;

    // 정렬 (popular / latest)
    private String sort = "popular";

    // 페이징 - TSX 파라미터명 그대로
    private int page = 1;
    private int limit = 6;

    // 찜 여부 판단용
    private Long userId;

    public int getOffset() {
        return (page - 1) * limit;
    }
}
