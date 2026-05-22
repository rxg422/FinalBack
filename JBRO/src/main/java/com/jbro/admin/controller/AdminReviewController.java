package com.jbro.admin.controller;

import com.jbro.admin.model.dto.AdminReviewDetailDto;
import com.jbro.admin.model.dto.AdminReviewListDto;
import com.jbro.admin.model.service.AdminReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/reviews")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    public AdminReviewController(AdminReviewService adminReviewService) {
        this.adminReviewService = adminReviewService;
    }

    @GetMapping
    public ResponseEntity<List<AdminReviewListDto>> getReviewList() {
        return ResponseEntity.ok(adminReviewService.getReviewList());
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<AdminReviewDetailDto> getReviewDetail(@PathVariable Long reviewId) {
        return ResponseEntity.ok(adminReviewService.getReviewDetail(reviewId));
    }

    @PatchMapping("/{reviewId}/hide")
    public ResponseEntity<Void> hideReview(@PathVariable Long reviewId) {
        adminReviewService.hideReview(reviewId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{reviewId}/show")
    public ResponseEntity<Void> showReview(@PathVariable Long reviewId) {
        adminReviewService.showReview(reviewId);
        return ResponseEntity.ok().build();
    }
}