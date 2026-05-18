package com.jbro.tourList.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TourListSearchDto {
    private String keyword;
    private Integer categoryId;
    private Integer regionCd;
    private String sort = "popular";   // TSX: sort=popular|latest
    private int page = 1;
    private int limit = 6;             // TSX: limit=6
    private Long userId;               // 찜 여부 확인용

    public int getOffset() {
        return (page - 1) * limit;
    }

    // 매퍼에서 sortType으로 분기할 수 있게 alias
    public String getSortType() {
        return sort;
    }
}