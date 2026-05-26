package com.jbro.admin.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class AdminUserDto {

    @Data
    public static class UserItem {
        private long no;
        private String email;
        private String nickname;
        private String role;
        private String status;
        private String createdAt;
    }

    @Data
    public static class UserListResponse {
        private List<UserItem> users;
        private int totalCount;
    }
}