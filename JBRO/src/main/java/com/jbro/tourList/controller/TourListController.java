package com.jbro.tourList.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jbro.tourList.model.dto.TourListResponseDto;
import com.jbro.tourList.model.dto.TourListSearchDto;
import com.jbro.tourList.model.service.TourListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tourList")
@RequiredArgsConstructor
public class TourListController {

    private final TourListService tourListService;

    
    @GetMapping
    public ResponseEntity<TourListResponseDto> selectTourList(
            TourListSearchDto searchDto
    ) {
        TourListResponseDto result = tourListService.selectTourList(searchDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/favorite/{contentId}")
    public ResponseEntity<?> toggleFavorite(
            @PathVariable Long contentId,
            @RequestParam(required = false) Long userId
    ) {
        if (userId == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("LOGIN_REQUIRED");
        }

        String result = tourListService.toggleFavorite(userId, contentId);

        return ResponseEntity.ok(result);
    }
}