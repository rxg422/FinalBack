package com.jbro.admin.model.service;

import com.jbro.admin.model.dto.AdminUserDto.UserListResponse;

public interface AdminUserService {
    UserListResponse getUsers(String keyword, String status, int page, int size);
    void updateUserStatus(long no, String status);
}