package com.property.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.property.util.JwtUtil;
import com.property.util.UserContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Allow preflight CORS requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("access_token");

        if (token == null || token.isEmpty()) {
            sendErrorResponse(response, 400, "Authentication token is required");
            return false;
        }

        try {
            Claims claims = jwtUtil.parseToken(token);
            Long id = ((Number) claims.get("id")).longValue();
            String name = (String) claims.get("name");
            Integer role = (Integer) claims.get("role");
            String email = (String) claims.get("email");
            String phone = (String) claims.get("phone");

            UserContext.UserClaims userClaims = new UserContext.UserClaims(id, name, role, email, phone);
            UserContext.setClaims(userClaims);
            return true;
        } catch (Exception e) {
            sendErrorResponse(response, 401, "Authentication token is invalid");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear();
    }

    private void sendErrorResponse(HttpServletResponse response, int responseCode, String message) throws Exception {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK); // 200 OK to match Node.js contract

        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("responseCode", responseCode);
        errorMap.put("responseMessage", message);
        errorMap.put("responseData", new HashMap<>());

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorMap));
    }
}
