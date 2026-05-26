package com.jbro.admin.model.dao;

import com.jbro.admin.model.dto.AdminUserDto.UserItem;
import java.util.List;
import java.util.Map;

public interface AdminUserDao {
    List<UserItem> selectUsers(Map<String, Object> params);
    int selectUserCount(Map<String, Object> params);
    void updateUserStatus(Map<String, Object> params);
}