package com.jbro.mypage.model.vo;

public class ProfileUpdateResponse {

	private boolean success;
	private String message;
	private MemberVo profile;

	public ProfileUpdateResponse() {
	}

	public ProfileUpdateResponse(boolean success, String message, MemberVo profile) {
		this.success = success;
		this.message = message;
		this.profile = profile;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MemberVo getProfile() {
		return profile;
	}

	public void setProfile(MemberVo profile) {
		this.profile = profile;
	}
}

