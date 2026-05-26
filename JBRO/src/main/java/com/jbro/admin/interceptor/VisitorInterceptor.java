package com.jbro.admin.interceptor;

import com.jbro.admin.model.service.AdminDashboardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class VisitorInterceptor implements HandlerInterceptor {

    private final AdminDashboardService adminDashboardService;

    public VisitorInterceptor(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String identifier;
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof Long memberId) {
            identifier = "user:" + memberId;
        } else {
            identifier = "ip:" + request.getRemoteAddr();
        }

        adminDashboardService.recordVisit(identifier);
        return true;
    }
}