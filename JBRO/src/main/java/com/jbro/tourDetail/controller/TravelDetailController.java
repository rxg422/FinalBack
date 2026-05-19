package com.jbro.tourDetail.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jbro.tourDetail.model.service.TravelDetailService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tourDetail")
@RequiredArgsConstructor
public class TravelDetailController {

    private final TravelDetailService travelDetailService;

    // 상세 조회
    @GetMapping("/{contentId}")
    public ResponseEntity<?> getDetail(
            @PathVariable long contentId,
            HttpServletRequest request) {
    	Long userId = null;
    	var auth = SecurityContextHolder.getContext().getAuthentication();
    	if (auth != null && auth.getPrincipal() instanceof Long) {
    	    userId = (Long) auth.getPrincipal();
    	}// JWT 필터에서 세팅
        Map<String, Object> result = travelDetailService.getDetail(contentId, userId);
        return ResponseEntity.ok(result);
    }

    // 리뷰 목록
    @GetMapping("/{contentId}/reviews")
    public ResponseEntity<?> getReviews(
            @PathVariable long contentId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "7") int size) {
        Map<String, Object> result = travelDetailService.getReviews(contentId, page, size);
        return ResponseEntity.ok(result);
    }

    // 리뷰 등록
    @PostMapping("/{contentId}/reviews")
    public ResponseEntity<?> createReview(
            @PathVariable long contentId,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image,
            HttpServletRequest request) {
    	System.out.println("파일 들어왔냐? " + (image != null));
        System.out.println("파일 비었냐? " + (image != null && image.isEmpty()));
        System.out.println("파일 이름: " + (image != null ? image.getOriginalFilename() : "null"));
    	Long userId = null;
    	var auth = SecurityContextHolder.getContext().getAuthentication();
    	if (auth != null && auth.getPrincipal() instanceof Long) {
    	    userId = (Long) auth.getPrincipal();
    	}
        if (userId == null) return ResponseEntity.status(401).build();
        travelDetailService.createReview(contentId, userId, content, image);
        return ResponseEntity.ok().build();
    }

    // 리뷰 수정
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable long reviewId,
            @RequestBody Map<String, String> body,
            HttpServletRequest request) {
    	Long userId = null;
    	var auth = SecurityContextHolder.getContext().getAuthentication();
    	if (auth != null && auth.getPrincipal() instanceof Long) {
    	    userId = (Long) auth.getPrincipal();
    	}
        if (userId == null) return ResponseEntity.status(401).build();
        travelDetailService.updateReview(reviewId, userId, body.get("content"));
        return ResponseEntity.ok().build();
    }

    // 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable long reviewId,
            HttpServletRequest request) {
    	Long userId = null;
    	var auth = SecurityContextHolder.getContext().getAuthentication();
    	if (auth != null && auth.getPrincipal() instanceof Long) {
    	    userId = (Long) auth.getPrincipal();
    	}
        if (userId == null) return ResponseEntity.status(401).build();
        travelDetailService.deleteReview(reviewId, userId);
        return ResponseEntity.ok().build();
    }

    // 찜하기 토글
    @PostMapping("/{contentId}/favorite")
    public ResponseEntity<?> toggleFavorite(
            @PathVariable long contentId,
            HttpServletRequest request) {
    	Long userId = null;
    	var auth = SecurityContextHolder.getContext().getAuthentication();
    	if (auth != null && auth.getPrincipal() instanceof Long) {
    	    userId = (Long) auth.getPrincipal();
    	}
        if (userId == null) return ResponseEntity.status(401).build();
        travelDetailService.toggleFavorite(contentId, userId);
        return ResponseEntity.ok().build();
    }

    // 신고
    @PostMapping("/reviews/{reviewId}/report")
    public ResponseEntity<?> reportReview(
            @PathVariable long reviewId,
            @RequestBody Map<String, String> body,
            HttpServletRequest request) {
    	Long userId = null;
    	var auth = SecurityContextHolder.getContext().getAuthentication();
    	if (auth != null && auth.getPrincipal() instanceof Long) {
    	    userId = (Long) auth.getPrincipal();
    	}
        if (userId == null) return ResponseEntity.status(401).build();
        travelDetailService.reportReview(reviewId, userId, body.get("reportType"), body.get("reason"));
        return ResponseEntity.ok().build();
    }
}