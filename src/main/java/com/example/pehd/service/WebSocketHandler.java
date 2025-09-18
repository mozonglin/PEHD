package com.example.pehd.service;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.pehd.entity.WebSocketConnection;
import com.example.pehd.repository.WebSocketConnectionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.example.pehd.dto.WebSocketMessage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private WebSocketConnectionRepository connectionRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    // 存储活跃连接：sessionId -> WebSocketConnection
    private final Map<String, WebSocketConnection> activeConnections = new ConcurrentHashMap<>();
    
    // 存储会话映射：userId -> sessionId
    private final Map<String, String> userSessionMap = new ConcurrentHashMap<>();
    
    // 存储会话：sessionId -> WebSocketSession
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        String connectionId = UUID.randomUUID().toString();
        
        if (userId != null) {
            // 创建WebSocket连接记录
            WebSocketConnection connection = new WebSocketConnection(userId, connectionId);
            connection.setConnectedAt(LocalDateTime.now());
            connection.setLastPingAt(LocalDateTime.now());
            connection.setIsActive(true);
            
            // 保存到数据库
            connectionRepository.save(connection);
            
            // 存储到内存映射
            activeConnections.put(session.getId(), connection);
            userSessionMap.put(userId, session.getId());
            sessionMap.put(session.getId(), session);
            
            // 发送连接成功消息
            WebSocketMessage successMessage = new WebSocketMessage(
                "CONNECTION_SUCCESS",
                Map.of(
                    "userId", userId,
                    "connectionId", connectionId,
                    "timestamp", LocalDateTime.now().toString()
                )
            );
            
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(successMessage)));
            
            System.out.println("WebSocket连接建立成功: userId=" + userId + ", sessionId=" + session.getId());
        } else {
            // 没有用户ID，关闭连接
            session.close(CloseStatus.POLICY_VIOLATION);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            JsonNode jsonNode = objectMapper.readTree(message.getPayload());
            String messageType = jsonNode.get("type").asText();
            
            WebSocketConnection connection = activeConnections.get(session.getId());
            if (connection != null) {
                // 处理心跳消息
                if ("PING".equals(messageType)) {
                    connection.setLastPingAt(LocalDateTime.now());
                    connectionRepository.save(connection);
                    
                    // 发送PONG响应
                    WebSocketMessage pongMessage = new WebSocketMessage(
                        "PONG",
                        Map.of("timestamp", System.currentTimeMillis())
                    );
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(pongMessage)));
                }
                // 可以在这里处理其他类型的消息
            }
        } catch (Exception e) {
            System.err.println("WebSocket消息处理异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        WebSocketConnection connection = activeConnections.remove(session.getId());
        sessionMap.remove(session.getId());
        
        if (connection != null) {
            // 更新数据库连接状态
            connection.setIsActive(false);
            connectionRepository.save(connection);
            
            // 移除用户会话映射
            userSessionMap.remove(connection.getUserId());
            
            System.out.println("WebSocket连接关闭: userId=" + connection.getUserId() + ", sessionId=" + session.getId());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("WebSocket传输错误: " + exception.getMessage());
        exception.printStackTrace();
        
        // 清理连接
        afterConnectionClosed(session, CloseStatus.SERVER_ERROR);
    }
    
    /**
     * 向指定用户发送消息
     */
    public void sendMessageToUser(String userId, WebSocketMessage message) {
        String sessionId = userSessionMap.get(userId);
        if (sessionId != null) {
            WebSocketSession session = sessionMap.get(sessionId);
            if (session != null && session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
                } catch (Exception e) {
                    System.err.println("发送WebSocket消息失败: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 广播消息给所有连接的用户
     */
    public void broadcastMessage(WebSocketMessage message) {
        sessionMap.values().forEach(session -> {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
                } catch (Exception e) {
                    System.err.println("广播WebSocket消息失败: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * 获取在线用户数量
     */
    public int getOnlineUserCount() {
        return activeConnections.size();
    }
    
    /**
     * 检查用户是否在线
     */
    public boolean isUserOnline(String userId) {
        return userSessionMap.containsKey(userId);
    }
} 