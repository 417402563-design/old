package com.antifraud.interceptor;

import com.antifraud.common.BusinessException;
import com.antifraud.common.Constants;
import com.antifraud.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 放行OPTIONS预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader(Constants.TOKEN_HEADER);
        if (token == null || !token.startsWith(Constants.TOKEN_PREFIX)) {
            throw new BusinessException(401, "未登录或Token已过期");
        }

        token = token.substring(Constants.TOKEN_PREFIX.length());
        if (!jwtUtil.validateToken(token) || jwtUtil.isTokenExpired(token)) {
            throw new BusinessException(401, "Token无效或已过期");
        }

        // 将用户信息放入request属性
        Long userId = jwtUtil.getUserId(token);
        String role = jwtUtil.getRole(token);
        request.setAttribute("userId", userId);
        request.setAttribute("role", role);
        return true;
    }
}
