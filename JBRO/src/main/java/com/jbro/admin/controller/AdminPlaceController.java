package com.jbro.admin.controller;

import com.jbro.admin.model.dto.AdminPlaceListDto;
import com.jbro.admin.model.dto.AdminPlaceReviewImageDto;
import com.jbro.admin.model.service.AdminPlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/places")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPlaceController {

    private final AdminPlaceService adminPlaceService;

    public AdminPlaceController(AdminPlaceService adminPlaceService) {
        this.adminPlaceService = adminPlaceService;
    }

    // 여행지 목록
    @GetMapping
    public ResponseEntity<List<AdminPlaceListDto>> getPlaceList(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false, defaultValue = "false") Boolean noImageOnly
    ) {
        return ResponseEntity.ok(adminPlaceService.getPlaceList(keyword, noImageOnly));
    }

    // 여행지 리뷰 이미지 목록
    @GetMapping("/{contentId}/review-images")
    public ResponseEntity<List<AdminPlaceReviewImageDto>> getReviewImages(
        @PathVariable Long contentId
    ) {
        return ResponseEntity.ok(adminPlaceService.getReviewImages(contentId));
    }

    // 이미지 등록
    @PostMapping("/{contentId}/images")
    public ResponseEntity<Void> registerPlaceImage(
        @PathVariable Long contentId,
        @RequestBody Map<String, String> body
    ) {
        adminPlaceService.registerPlaceImage(contentId, body.get("imageUrl"), body.get("imgName"));
        return ResponseEntity.ok().build();
    }

    // 이미지 삭제
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<Void> deletePlaceImage(@PathVariable Long imageId) {
        adminPlaceService.deletePlaceImage(imageId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{contentId}/images")
    public ResponseEntity<List<Map<String, Object>>> getPlaceImages(@PathVariable Long contentId) {
        return ResponseEntity.ok(adminPlaceService.getPlaceImages(contentId));
    }
}