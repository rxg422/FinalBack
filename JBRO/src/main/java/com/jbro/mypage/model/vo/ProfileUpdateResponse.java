package com.jbro.mypage.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileUpdateResponse {

	private boolean success;
	private String message;
	private MemberVo profile;

	public ProfileUpdateResponse(boolean success, String message, MemberVo profile) {
		this.success = success;
		this.message = message;
		this.profile = profile;
	}
}

