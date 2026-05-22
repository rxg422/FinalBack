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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tourDetail")
@RequiredArgsConstructor
public class TravelDetailController {

    private final TravelDetailService travelDetailService;

    private Long getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Long) {
            return (Long) auth.getPrincipal();
        }
        return null;
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<?> getDetail(@PathVariable long contentId) {
        Long userId = getCurrentUserId();
        System.out.println("👤 getDetail userId: " + userId);
        Map<String, Object> result = travelDetailService.getDetail(contentId, userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{contentId}/reviews")
    public ResponseEntity<?> getReviews(
            @PathVariable long contentId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "7") int size) {
        Map<String, Object> result = travelDetailService.getReviews(contentId, page, size);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{contentId}/reviews")
    public ResponseEntity<?> createReview(
            @PathVariable long contentId,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        Long userId = getCurrentUserId();
        if (userId == null) return ResponseEntity.status(401).build();
        travelDetailService.createReview(contentId, userId, content, image);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable long reviewId,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        Long userId = getCurrentUserId();
        if (userId == null) return ResponseEntity.status(401).build();
        travelDetailService.updateReview(reviewId, userId, content, image);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable long reviewId) {
        Long userId = getCurrentUserId();
        if (userId == null) return ResponseEntity.status(401).build();
        travelDetailService.deleteReview(reviewId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{contentId}/favorite")
    public ResponseEntity<?> toggleFavorite(@PathVariable long contentId) {
        Long userId = getCurrentUserId();
        if (userId == null) return ResponseEntity.status(401).build();
        travelDetailService.toggleFavorite(contentId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reviews/{reviewId}/report")
    public ResponseEntity<?> reportReview(
            @PathVariable long reviewId,
            @RequestBody Map<String, String> body) {
        Long userId = getCurrentUserId();
        if (userId == null) return ResponseEntity.status(401).build();
        travelDetailService.reportReview(reviewId, userId, body.get("reportType"), body.get("reason"));
        return ResponseEntity.ok().build();
    }
}