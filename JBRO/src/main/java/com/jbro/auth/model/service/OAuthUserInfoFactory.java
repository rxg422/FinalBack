package com.jbro.auth.model.service;

import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.jbro.auth.model.vo.OAuthUserInfo;

@Component
public class OAuthUserInfoFactory {

	public OAuthUserInfo create(String provider, OAuth2User oauth2User) {
		Map<String, Object> attributes = oauth2User.getAttributes();

		return switch (provider) {
			case "kakao" -> createKakaoUserInfo(attributes);
			case "naver" -> createNaverUserInfo(attributes);
			case "google" -> createGoogleUserInfo(attributes);
			default -> throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다: " + provider);
		};
	}

	@SuppressWarnings("unchecked")
	private OAuthUserInfo createKakaoUserInfo(Map<String, Object> attributes) {
		String providerId = String.valueOf(attributes.get("id"));
		Map<String, Object> account = (Map<String, Object>) attributes.getOrDefault("kakao_account", Map.of());
		Map<String, Object> profile = (Map<String, Object>) account.getOrDefault("profile", Map.of());

		return new OAuthUserInfo(
			"kakao",
			providerId,
			toStringOrNull(account.get("email")),
			toStringOrNull(profile.get("nickname")),
			toStringOrNull(profile.get("profile_image_url"))
		);
	}

	@SuppressWarnings("unchecked")
	private OAuthUserInfo createNaverUserInfo(Map<String, Object> attributes) {
		Map<String, Object> response = (Map<String, Object>) attributes.getOrDefault("response", Map.of());

		return new OAuthUserInfo(
			"naver",
			toStringOrNull(response.get("id")),
			toStringOrNull(response.get("email")),
			toStringOrNull(response.get("nickname")),
			toStringOrNull(response.get("profile_image"))
		);
	}

	private OAuthUserInfo createGoogleUserInfo(Map<String, Object> attributes) {
		return new OAuthUserInfo(
			"google",
			toStringOrNull(attributes.get("sub")),
			toStringOrNull(attributes.get("email")),
			toStringOrNull(attributes.get("name")),
			toStringOrNull(attributes.get("picture"))
		);
	}

	private String toStringOrNull(Object value) {
		if (value == null) {
			return null;
		}

		String text = String.valueOf(value).trim();
		return text.isEmpty() ? null : text;
	}
}
