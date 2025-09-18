package com.example.pehd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "websocket_connections")
public class WebSocketConnection {
    
    @Id
    @Column(length = 36)
    private String id;
    
    @NotBlank(message = "用户ID不能为空")
    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;
    
    @NotBlank(message = "连接ID不能为空")
    @Column(name = "connection_id", nullable = false, length = 100)
    private String connectionId;
    
    @Column(name = "connected_at", nullable = false)
    private LocalDateTime connectedAt;
    
    @Column(name = "last_ping_at", nullable = false)
    private LocalDateTime lastPingAt;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @PrePersist
    protected void onCreate() {
        this.connectedAt = LocalDateTime.now();
        this.lastPingAt = LocalDateTime.now();
        if (this.id == null) {
            this.id = java.util.UUID.randomUUID().toString();
        }
    }
    
    // Constructors
    public WebSocketConnection() {}
    
    public WebSocketConnection(String userId, String connectionId) {
        this.userId = userId;
        this.connectionId = connectionId;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getConnectionId() { return connectionId; }
    public void setConnectionId(String connectionId) { this.connectionId = connectionId; }
    
    public LocalDateTime getConnectedAt() { return connectedAt; }
    public void setConnectedAt(LocalDateTime connectedAt) { this.connectedAt = connectedAt; }
    
    public LocalDateTime getLastPingAt() { return lastPingAt; }
    public void setLastPingAt(LocalDateTime lastPingAt) { this.lastPingAt = lastPingAt; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}