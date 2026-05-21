package com.jbro.auth.model.service;

import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.jbro.auth.model.vo.OAuthUserInfo;

@Component
public class OAuthUserInfoFactory {

	public OAuthUserInfo create(String provider, OAuth2User oauth2User) {
		// ========== 입력값 검증 ==========
        if (provider == null || provider.isEmpty()) {
            throw new IllegalArgumentException("소셜 로그인 제공자가 없습니다!");
        }
        
        if (oauth2User == null) {
            throw new IllegalArgumentException("OAuth2 사용자 정보가 없습니다!");
        }
        
        Map<String, Object> attributes = oauth2User.getAttributes();
        
        if (attributes == null || attributes.isEmpty()) {
            throw new IllegalArgumentException("사용자 속성 정보가 없습니다!");
        }

        //  어디서 로그인 했는지에 따라 다른 처리
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
        
        // ========== providerId 검증 ==========
        if (providerId == null || providerId.isEmpty() || providerId.equals("null")) {
            throw new IllegalArgumentException("카카오 사용자 ID가 없습니다!");
        }
        
        Map<String, Object> account = (Map<String, Object>) attributes.getOrDefault("kakao_account", Map.of());
        Map<String, Object> profile = (Map<String, Object>) account.getOrDefault("profile", Map.of());

        // ========== 이메일 (선택사항) ==========
        String email = toStringOrNull(account.get("email"));
        if (email == null || email.isEmpty()) {
            email = "kakao_" + providerId + "@jbro.local";
        }

        return new OAuthUserInfo(
            "kakao",
            providerId,
            email,
            toStringOrNull(profile.get("nickname")),
            toStringOrNull(profile.get("profile_image_url"))
        );
    }

    @SuppressWarnings("unchecked")
    private OAuthUserInfo createNaverUserInfo(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.getOrDefault("response", Map.of());
        
        // ========== response 검증 ==========
        if (response.isEmpty()) {
            throw new IllegalArgumentException("네이버 응답 데이터가 비어있습니다!");
        }

        String providerId = toStringOrNull(response.get("id"));
        
        // ========== providerId 검증 ==========
        if (providerId == null || providerId.isEmpty()) {
            throw new IllegalArgumentException("네이버 사용자 ID가 없습니다!");
        }

        // ========== 이메일 검증 ==========
        String email = toStringOrNull(response.get("email"));
        if (email == null || email.isEmpty()) {
            email = "naver_" + providerId + "@jbro.local";
        }

        return new OAuthUserInfo(
            "naver",
            providerId,
            email,
            toStringOrNull(response.get("nickname")),
            toStringOrNull(response.get("profile_image"))
        );
    }

    private OAuthUserInfo createGoogleUserInfo(Map<String, Object> attributes) {
        String providerId = toStringOrNull(attributes.get("sub"));

        // ========== providerId 검증 ==========
        if (providerId == null || providerId.isEmpty()) {
            throw new IllegalArgumentException("구글 사용자 ID(sub)가 없습니다!");
        }

        // ========== 이메일 검증 ==========
        String email = toStringOrNull(attributes.get("email"));
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("구글에서 제공한 이메일이 없습니다!");
        }

        String name = toStringOrNull(attributes.get("name"));
        String picture = toStringOrNull(attributes.get("picture"));

        return new OAuthUserInfo(
            "google",
            providerId,
            email,
            name,
            picture
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