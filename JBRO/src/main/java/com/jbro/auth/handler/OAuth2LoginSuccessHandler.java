package com.jbro.auth.handler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.jbro.auth.model.service.JwtTokenProvider;
import com.jbro.auth.model.service.OAuthLoginService;
import com.jbro.auth.model.service.OAuthUserInfoFactory;
import com.jbro.auth.model.vo.OAuthUserInfo;
import com.jbro.mypage.model.vo.MemberVo;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
	private static final Logger logger = LoggerFactory.getLogger(OAuth2LoginSuccessHandler.class);

	private final OAuthUserInfoFactory userInfoFactory;
	private final OAuthLoginService oauthLoginService;
	private final JwtTokenProvider jwtTokenProvider;
	private final String frontendCallbackUrl;

	public OAuth2LoginSuccessHandler(
		OAuthUserInfoFactory userInfoFactory,
		OAuthLoginService oauthLoginService,
		JwtTokenProvider jwtTokenProvider,
		@Value("${app.frontend.oauth-callback-url:http://localhost:3000/oauth2/callback}") String frontendCallbackUrl
	) {
		this.userInfoFactory = userInfoFactory;
		this.oauthLoginService = oauthLoginService;
		this.jwtTokenProvider = jwtTokenProvider;
		this.frontendCallbackUrl = frontendCallbackUrl;
	}

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication
	) throws IOException, ServletException {
		try {
	        logger.info("===== OAuth2 Login Success Handler Start =====");

	        // ========== 입력값 검증 ==========
	        if (authentication == null) {
	            throw new IllegalArgumentException("인증 정보가 없습니다!");
	        }

	        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
	        System.out.println(oauthToken);
	        
	        
	        OAuth2User oauth2User = oauthToken.getPrincipal();
	        String provider = oauthToken.getAuthorizedClientRegistrationId();

	        logger.info("Provider: {}", provider);
	        logger.info("OAuth2User Principal Name: {}", oauth2User.getName());

	        if (oauth2User == null) {
	            throw new IllegalArgumentException("OAuth2 사용자 정보가 없습니다!");
	        }

	        if (provider == null || provider.isEmpty()) {
	            throw new IllegalArgumentException("소셜 로그인 제공자 정보가 없습니다!");
	        }

	        // ========== 사용자 정보 추출 및 검증 ==========
	        logger.info("Extracting user info from provider: {}", provider);
	        OAuthUserInfo userInfo = userInfoFactory.create(provider, oauth2User);
	        logger.info("User info extracted successfully");
	        logger.info("UserInfo - Provider: {}, ProviderId: {}, Email: {}", userInfo.getProvider(), userInfo.getProviderId(), userInfo.getEmail());

	        if (userInfo == null) {
	            throw new RuntimeException("사용자 정보 변환에 실패했습니다!");
	        }

	        // ========== 사용자 생성 또는 조회 ==========
	        logger.info("Finding or creating member...");
	        MemberVo member = oauthLoginService.findOrCreateMember(userInfo);
	        logger.info("Member found/created successfully");

	        if (member == null) {
	            throw new RuntimeException("사용자 정보를 찾을 수 없습니다!");
	        }

	        logger.info("Member ID: {}", member.getId());

	        if (member.getId() == null || member.getId() <= 0) {
	            throw new RuntimeException("유효한 사용자 ID가 없습니다!");
	        }

	        // ========== 토큰 생성 ==========
	        logger.info("Creating tokens...");
	        String accessToken = jwtTokenProvider.createAccessToken(member);
	        String refreshToken = jwtTokenProvider.createRefreshToken(member);
	        logger.info("Tokens created successfully");

	        if (accessToken == null || accessToken.isEmpty()) {
	            throw new RuntimeException("생성된 토큰이 없습니다!");
	        }

	        if (refreshToken == null || refreshToken.isEmpty()) {
	            throw new RuntimeException("생성된 리프레시 토큰이 없습니다!");
	        }

	        // ========== 세션에 사용자 ID 저장 ==========
	        HttpSession session = request.getSession();
	        session.setAttribute("memberId", member.getId());
	        logger.info("Session attribute set");

	        // ========== 프론트엔드로 성공 응답 ==========
	        String encodedToken = URLEncoder.encode(accessToken, StandardCharsets.UTF_8);
	        String encodedRefreshToken = URLEncoder.encode(refreshToken, StandardCharsets.UTF_8);
	        String redirectUrl = frontendCallbackUrl + "?accessToken=" + encodedToken + "&refreshToken=" + encodedRefreshToken + "&success=true";
	        logger.info("Redirecting to: {}", frontendCallbackUrl);
	        response.sendRedirect(redirectUrl);
	        logger.info("===== OAuth2 Login Success Handler Complete =====");

	    } catch (Exception e) {
	        // ========== 예상 밖의 에러 처리 ==========
	        logger.error("===== OAuth2 Login Failed =====", e);
	        logger.error("Exception type: {}", e.getClass().getName());
	        logger.error("Exception message: {}", e.getMessage());

	        String errorMessage = "알 수 없는 오류가 발생했습니다!";
	        String encodedError = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
	        try {
	            response.sendRedirect(frontendCallbackUrl + "?error=" + encodedError);
	        } catch (Exception redirectException) {
	            logger.error("Failed to redirect to error page", redirectException);
	        }
	    }
	}
}