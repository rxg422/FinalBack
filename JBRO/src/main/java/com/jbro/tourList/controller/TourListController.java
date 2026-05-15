package com.jbro.tourList.controller;

import com.jbro.tourList.model.dto.TourListResponseDto;
import com.jbro.tourList.model.dto.TourListSearchDto;
import com.jbro.tourList.model.service.TourListService;
import com.jbro.tourList.model.vo.TourList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tourList")
public class TourListController {

    @Autowired
    private TourListService tourListService;

    /**
     * GET /api/tourList
     * ?categoryId=1&region=전주&keyword=&sort=popular&page=1&limit=6&userId=1
     */
    @GetMapping
    public ResponseEntity<TourListResponseDto> getTourList(TourListSearchDto searchDto) {
        TourListResponseDto response = tourListService.getTourList(searchDto);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/tourList/detail/{contentId}
     */
    @GetMapping("/detail/{contentId}")
    public ResponseEntity<TourList> getTourDetail(@PathVariable Long contentId) {
        TourList detail = tourListService.getTourDetail(contentId);
        if (detail == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(detail);
    }

    /**
     * POST /api/tourList/favorite/{contentId}?userId={userId}
     * 응답: "INSERT" or "DELETE" (text/plain)
     */
    @PostMapping("/favorite/{contentId}")
    public ResponseEntity<String> toggleFavorite(
            @PathVariable Long contentId,
            @RequestParam Long userId) {
        String result = tourListService.toggleFavorite(contentId, userId);
        return ResponseEntity.ok(result);
    }
}
