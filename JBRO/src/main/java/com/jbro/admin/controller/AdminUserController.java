package com.jbro.admin.controller;

import com.jbro.admin.model.dto.AdminUserDto.UserListResponse;
import com.jbro.admin.model.service.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public ResponseEntity<UserListResponse> getUsers(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "ALL") String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(adminUserService.getUsers(keyword, status, page, size));
    }
    @PatchMapping("/{no}/status")
    public ResponseEntity<Void> updateUserStatus(
            @PathVariable long no,
            @RequestParam String status
    ) {
        adminUserService.updateUserStatus(no, status);
        return ResponseEntity.ok().build();
    }
}