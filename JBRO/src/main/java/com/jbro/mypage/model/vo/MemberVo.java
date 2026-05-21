package com.jbro.mypage.model.vo;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberVo {

	private Long id;
	private String email;
	private String nickname;
	private String profile;
	private LocalDateTime createdAt;
	private String status;
	private String role;  // ADMIN, USER, GUEST
}

