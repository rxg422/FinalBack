package com.jbro.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;  
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;  
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

import com.jbro.auth.model.dao.AuthDAO;  
import com.jbro.auth.model.service.JwtTokenProvider; 
import com.jbro.auth.model.service.LoginMemberProvider;  

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
	private final JwtTokenProvider jwtTokenProvider;
    private final LoginMemberProvider loginMemberProvider;
    private final AuthDAO authDAO;
    
    public AuthController(
        JwtTokenProvider jwtTokenProvider,
        LoginMemberProvider loginMemberProvider,
        AuthDAO authDAO
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginMemberProvider = loginMemberProvider;
        this.authDAO = authDAO;
    }
	
    // ========== 로그아웃 기능 ==========
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        // 방법1: 세션 제거
        session.invalidate();
        
        // 방법2: Spring Security 로그아웃
        SecurityContextHolder.clearContext();
        
        return ResponseEntity.ok("로그아웃 성공!");
    }
    
    // ========== 리프레시 토큰으로 새 액세스 토큰 받기 ==========
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            String newAccessToken = jwtTokenProvider.refreshAccessToken(request.getRefreshToken());
            return ResponseEntity.ok(new TokenResponse(newAccessToken, "토큰 갱신 성공!"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("토큰 갱신 실패: " + e.getMessage());
        }
    }
    
    // ========== 로그인 상태 확인 API ==========
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            // ✅ HttpSession 대신 JWT에서 추출
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            
            if (principal == null || principal.equals("anonymousUser")) {
                return ResponseEntity.status(401).body("로그인되지 않았습니다!");
            }
            
            Long memberId = (Long) principal;
            
            return ResponseEntity.ok(new LoginCheckResponse(
                memberId,
                "로그인 상태입니다!",
                true
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(401).body("로그인 확인 실패: " + e.getMessage());
        }
    }
    
    // ========== 로그인 확인 응답 DTO ==========
    public static class LoginCheckResponse {
        private Long memberId;
        private String message;
        private boolean isLoggedIn;
        
        public LoginCheckResponse(Long memberId, String message, boolean isLoggedIn) {
            this.memberId = memberId;
            this.message = message;
            this.isLoggedIn = isLoggedIn;
        }
        
        public Long getMemberId() {
            return memberId;
        }
        
        public String getMessage() {
            return message;
        }
        
        public boolean isLoggedIn() {
            return isLoggedIn;
        }
    }
    
    // ========== RefreshTokenRequest DTO ==========
    public static class RefreshTokenRequest {
        private String refreshToken;	
        
        public String getRefreshToken() {
            return refreshToken;
        }
        
        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }
    
    // ========== TokenResponse DTO ==========
    public static class TokenResponse {
        private String accessToken;
        private String message;
        
        public TokenResponse(String accessToken, String message) {
            this.accessToken = accessToken;
            this.message = message;
        }
        
        public String getAccessToken() {
            return accessToken;
        }
        
        public String getMessage() {
            return message;
        }
    }
}