package com.example.pehd.service;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.pehd.service.JwtService;

import java.util.Map;
import java.net.URI;
import java.util.Arrays;

@Component
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        
        // 从查询参数中获取token
        URI uri = request.getURI();
        String query = uri.getQuery();
        String token = null;
        
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("token=")) {
                    token = param.substring(6); // 去掉"token="
                    break;
                }
            }
        }
        
        // 如果没有token，也可以从Header中获取
        if (token == null) {
            String authHeader = request.getHeaders().getFirst("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }
        
        if (token != null) {
            try {
                // 从token中提取用户名（学号）
                String username = jwtService.extractUsername(token);
                if (username != null) {
                    // 这里简化处理，直接使用用户名作为userId
                    // 实际应用中可能需要通过用户名查询用户ID
                    attributes.put("userId", username);
                    attributes.put("token", token);
                    return true;
                } else {
                    System.err.println("WebSocket认证失败: Token无效");
                    return false;
                }
            } catch (Exception e) {
                System.err.println("WebSocket认证异常: " + e.getMessage());
                return false;
            }
        } else {
            System.err.println("WebSocket认证失败: 缺少Token");
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // 握手完成后的处理，通常不需要特殊处理
        if (exception != null) {
            System.err.println("WebSocket握手异常: " + exception.getMessage());
        }
    }
} 