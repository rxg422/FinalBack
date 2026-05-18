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

    // GET /api/tourList?categoryId=1&keyword=전주&sort=popular&page=1&limit=6&userId=1
    @GetMapping
    public ResponseEntity<TourListResponseDto> getTourList(TourListSearchDto searchDto) {
        return ResponseEntity.ok(tourListService.getTourList(searchDto));
    }

    // GET /api/tourList/detail/12345
    @GetMapping("/detail/{contentId}")
    public ResponseEntity<TourList> getTourDetail(@PathVariable Long contentId) {
        return ResponseEntity.ok(tourListService.getTourDetail(contentId));
    }

    // POST /api/tourList/favorite/{contentId}?userId=1
    // 응답: "INSERT" or "DELETE" (TSX에서 response.text()로 받음)
    @PostMapping("/favorite/{contentId}")
    public ResponseEntity<String> toggleFavorite(
            @PathVariable Long contentId,
            @RequestParam Long userId) {
        String result = tourListService.toggleFavorite(contentId, userId);
        return ResponseEntity.ok(result);
    }
}