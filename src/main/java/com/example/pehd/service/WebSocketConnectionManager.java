package com.example.pehd.service;

import com.example.pehd.repository.WebSocketConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class WebSocketConnectionManager {

    @Autowired
    private WebSocketConnectionRepository connectionRepository;

    /**
     * 定时清理过期的WebSocket连接
     * 每分钟执行一次，清理超过5分钟没有心跳的连接
     */
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    @Transactional
    public void cleanupExpiredConnections() {
        try {
            // 计算过期时间点（5分钟前）
            LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(5);
            
            // 标记过期连接为非活跃状态
            int updatedCount = connectionRepository.deactivateExpiredConnections(cutoffTime);
            
            // 删除非活跃连接（可选，或者保留用于统计）
            // connectionRepository.deleteByIsActiveFalse();
            
            if (updatedCount > 0) {
                System.out.println("WebSocket连接清理完成: " + LocalDateTime.now() + 
                                 ", 清理了 " + updatedCount + " 个过期连接");
            } else {
                System.out.println("WebSocket连接清理完成: " + LocalDateTime.now() + 
                                 ", 无过期连接需要清理");
            }
        } catch (Exception e) {
            System.err.println("WebSocket连接清理失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取活跃连接统计信息
     */
    public ConnectionStats getConnectionStats() {
        long activeConnections = connectionRepository.countByIsActiveTrue();
        return new ConnectionStats(activeConnections);
    }

    /**
     * 连接统计信息类
     */
    public static class ConnectionStats {
        private final long activeConnections;
        private final LocalDateTime timestamp;

        public ConnectionStats(long activeConnections) {
            this.activeConnections = activeConnections;
            this.timestamp = LocalDateTime.now();
        }

        public long getActiveConnections() {
            return activeConnections;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
} 