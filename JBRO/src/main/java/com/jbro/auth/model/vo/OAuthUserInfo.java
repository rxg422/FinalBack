package com.jbro.auth.model.vo;

public class OAuthUserInfo {

	private final String provider;
	private final String providerId;
	private final String email;
	private final String nickname;
	private final String profile;

	public OAuthUserInfo(String provider, String providerId, String email, String nickname, String profile) {
		this.provider = provider;
		this.providerId = providerId;
		this.email = email;
		this.nickname = nickname;
		this.profile = profile;
	}

	public String getProvider() {
		return provider;
	}

	public String getProviderId() {
		return providerId;
	}

	public String getEmail() {
		return email;
	}

	public String getNickname() {
		return nickname;
	}

	public String getProfile() {
		return profile;
	}
}
