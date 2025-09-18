package com.example.pehd.repository;

import com.example.pehd.entity.WebSocketConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WebSocketConnectionRepository extends JpaRepository<WebSocketConnection, String> {
    
    /**
     * 根据用户ID查找活跃连接
     */
    List<WebSocketConnection> findByUserIdAndIsActiveTrue(String userId);
    
    /**
     * 根据连接ID查找连接
     */
    WebSocketConnection findByConnectionId(String connectionId);
    
    /**
     * 查找所有活跃连接
     */
    List<WebSocketConnection> findByIsActiveTrue();
    
    /**
     * 清理过期连接（超过指定时间未心跳的连接）
     * @return 受影响的记录数
     */
    @Modifying
    @Query("UPDATE WebSocketConnection w SET w.isActive = false WHERE w.lastPingAt < :cutoffTime AND w.isActive = true")
    int deactivateExpiredConnections(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    /**
     * 删除非活跃连接
     * @return 删除的记录数
     */
    @Modifying
    @Query("DELETE FROM WebSocketConnection w WHERE w.isActive = false")
    int deleteByIsActiveFalse();
    
    /**
     * 统计活跃连接数量
     */
    long countByIsActiveTrue();
} 