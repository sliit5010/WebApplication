package com.weddingplanner.admin.config;

import com.weddingplanner.admin.model.Admin;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Admin user = (Admin) session.getAttribute("user");

        String uri = request.getRequestURI();

        if (user == null && !uri.equals("/login")) {
            response.sendRedirect("/login");
            return false;
        }

        if (uri.startsWith("/admins") || uri.startsWith("/income")) {
            if (user == null || !"SuperAdmin".equals(user.getRole())) {
                response.sendRedirect("/dashboard?error=unauthorized");
                return false;
            }
        }

        return true;
    }
}
