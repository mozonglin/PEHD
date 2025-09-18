package com.example.pehd.service;

import com.example.pehd.dto.WebSocketMessage;
import com.example.pehd.dto.ActivityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class WebSocketPushService {

    @Autowired
    private WebSocketHandler webSocketHandler;

    /**
     * 推送积分更新消息
     */
    @Async
    public void pushPointsUpdate(String userId, int pointsEarned, int totalPoints, 
                                 int peActivityPoints, int morningExercisePoints, 
                                 String activityName, String earnedReason, 
                                 String calculationRule, int participationDuration) {
        
        WebSocketMessage message = new WebSocketMessage(
            "POINTS_UPDATE",
            Map.of(
                "userId", userId,
                "pointsEarned", pointsEarned,
                "totalPoints", totalPoints,
                "peActivityPoints", peActivityPoints,
                "morningExercisePoints", morningExercisePoints,
                "activityName", activityName,
                "earnedReason", earnedReason,
                "calculationRule", calculationRule,
                "participationDuration", participationDuration,
                "timestamp", LocalDateTime.now().toString()
            )
        );
        
        try {
            webSocketHandler.sendMessageToUser(userId, message);
            System.out.println("积分更新推送成功: userId=" + userId + ", pointsEarned=" + pointsEarned);
        } catch (Exception e) {
            System.err.println("积分更新推送失败: " + e.getMessage());
            throw e; // 重新抛出异常以触发重试
        }
    }

    /**
     * 推送活动状态更新消息
     */
    @Async
    public void pushActivityUpdate(String userId, List<ActivityDto> updatedActivities) {
        
        WebSocketMessage message = new WebSocketMessage(
            "ACTIVITY_UPDATE",
            Map.of(
                "userId", userId,
                "updatedActivities", updatedActivities,
                "timestamp", LocalDateTime.now().toString()
            )
        );
        
        try {
            webSocketHandler.sendMessageToUser(userId, message);
            System.out.println("活动状态更新推送成功: userId=" + userId);
        } catch (Exception e) {
            System.err.println("活动状态更新推送失败: " + e.getMessage());
            throw e; // 重新抛出异常以触发重试
        }
    }

    /**
     * 推送早操更新消息
     */
    @Async
    public void pushMorningExerciseUpdate(String userId, String exerciseId, 
                                          String exerciseName, String status, 
                                          int pointsEarned) {
        
        WebSocketMessage message = new WebSocketMessage(
            "MORNING_EXERCISE_UPDATE",
            Map.of(
                "userId", userId,
                "exerciseId", exerciseId,
                "exerciseName", exerciseName,
                "status", status,
                "pointsEarned", pointsEarned,
                "timestamp", LocalDateTime.now().toString()
            )
        );
        
        try {
            webSocketHandler.sendMessageToUser(userId, message);
            System.out.println("早操更新推送成功: userId=" + userId + ", exerciseId=" + exerciseId);
        } catch (Exception e) {
            System.err.println("早操更新推送失败: " + e.getMessage());
            throw e; // 重新抛出异常以触发重试
        }
    }

    /**
     * 推送签到成功消息给活动创建者
     */
    @Async
    public void pushCheckInNotification(String organizerId, String studentName, String activityName) {
        
        WebSocketMessage message = new WebSocketMessage(
            "CHECK_IN_NOTIFICATION",
            Map.of(
                "type", "CHECK_IN",
                "studentName", studentName,
                "activityName", activityName,
                "message", studentName + " 已完成 " + activityName + " 签到",
                "timestamp", LocalDateTime.now().toString()
            )
        );
        
        try {
            webSocketHandler.sendMessageToUser(organizerId, message);
            System.out.println("签到通知推送成功: organizerId=" + organizerId + ", student=" + studentName);
        } catch (Exception e) {
            System.err.println("签到通知推送失败: " + e.getMessage());
        }
    }

    /**
     * 推送签退成功消息给活动创建者
     */
    @Async
    public void pushCheckOutNotification(String organizerId, String studentName, String activityName, int pointsEarned) {
        
        WebSocketMessage message = new WebSocketMessage(
            "CHECK_OUT_NOTIFICATION",
            Map.of(
                "type", "CHECK_OUT",
                "studentName", studentName,
                "activityName", activityName,
                "pointsEarned", pointsEarned,
                "message", studentName + " 已完成 " + activityName + " 签退，获得 " + pointsEarned + " 积分",
                "timestamp", LocalDateTime.now().toString()
            )
        );
        
        try {
            webSocketHandler.sendMessageToUser(organizerId, message);
            System.out.println("签退通知推送成功: organizerId=" + organizerId + ", student=" + studentName);
        } catch (Exception e) {
            System.err.println("签退通知推送失败: " + e.getMessage());
        }
    }

    /**
     * 广播系统消息
     */
    @Async
    public void broadcastSystemMessage(String message, String messageType) {
        
        WebSocketMessage wsMessage = new WebSocketMessage(
            "SYSTEM_MESSAGE",
            Map.of(
                "messageType", messageType,
                "message", message,
                "timestamp", LocalDateTime.now().toString()
            )
        );
        
        try {
            webSocketHandler.broadcastMessage(wsMessage);
            System.out.println("系统消息广播成功: " + message);
        } catch (Exception e) {
            System.err.println("系统消息广播失败: " + e.getMessage());
        }
    }

    /**
     * 检查用户是否在线
     */
    public boolean isUserOnline(String userId) {
        return webSocketHandler.isUserOnline(userId);
    }

    /**
     * 获取在线用户数量
     */
    public int getOnlineUserCount() {
        return webSocketHandler.getOnlineUserCount();
    }
} 