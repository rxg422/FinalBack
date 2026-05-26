package com.jbro.admin.model.service;

import com.jbro.admin.model.dao.AdminUserDao;
import com.jbro.admin.model.dto.AdminUserDto.UserItem;
import com.jbro.admin.model.dto.AdminUserDto.UserListResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserDao adminUserDao;

    public AdminUserServiceImpl(AdminUserDao adminUserDao) {
        this.adminUserDao = adminUserDao;
    }

    @Override
    public UserListResponse getUsers(String keyword, String status, int page, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword != null && !keyword.isBlank() ? keyword : null);
        params.put("status", status != null && !status.equals("ALL") ? status : null);
        params.put("offset", (page - 1) * size);
        params.put("size", size);

        List<UserItem> users = adminUserDao.selectUsers(params);
        int totalCount = adminUserDao.selectUserCount(params);

        UserListResponse response = new UserListResponse();
        response.setUsers(users);
        response.setTotalCount(totalCount);
        return response;
    }
    @Override
    public void updateUserStatus(long no, String status) {
        Map<String, Object> params = new HashMap<>();
        params.put("no", no);
        params.put("status", status);
        adminUserDao.updateUserStatus(params);
    }
}