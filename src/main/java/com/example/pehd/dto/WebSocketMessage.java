package com.example.pehd.dto;

import java.util.List;

public class WebSocketMessage {
    
    private String type;
    private Object data;
    
    // Constructors
    public WebSocketMessage() {}
    
    public WebSocketMessage(String type, Object data) {
        this.type = type;
        this.data = data;
    }
    
    // Getters and Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
    
    // 内部类用于活动更新消息
    public static class ActivityUpdateData {
        private String userId;
        private List<ActivityDto> updatedActivities;
        
        public ActivityUpdateData() {}
        
        public ActivityUpdateData(String userId, List<ActivityDto> updatedActivities) {
            this.userId = userId;
            this.updatedActivities = updatedActivities;
        }
        
        // Getters and Setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        
        public List<ActivityDto> getUpdatedActivities() { return updatedActivities; }
        public void setUpdatedActivities(List<ActivityDto> updatedActivities) { this.updatedActivities = updatedActivities; }
    }
    
    // 创建活动更新消息的工厂方法
    public static WebSocketMessage createActivityUpdateMessage(String userId, List<ActivityDto> activities) {
        ActivityUpdateData data = new ActivityUpdateData(userId, activities);
        return new WebSocketMessage("ACTIVITY_UPDATE", data);
    }
    
    // 积分更新消息数据类
    public static class PointsUpdateData {
        private String userId;
        private int pointsEarned;
        private int totalPoints;
        private int peActivityPoints;
        private int morningExercisePoints;
        private String activityName;
        private String earnedReason;
        private String calculationRule;
        private int participationDuration;
        private String timestamp;
        
        public PointsUpdateData() {}
        
        public PointsUpdateData(String userId, int pointsEarned, int totalPoints, 
                               int peActivityPoints, int morningExercisePoints,
                               String activityName, String earnedReason, 
                               String calculationRule, int participationDuration, 
                               String timestamp) {
            this.userId = userId;
            this.pointsEarned = pointsEarned;
            this.totalPoints = totalPoints;
            this.peActivityPoints = peActivityPoints;
            this.morningExercisePoints = morningExercisePoints;
            this.activityName = activityName;
            this.earnedReason = earnedReason;
            this.calculationRule = calculationRule;
            this.participationDuration = participationDuration;
            this.timestamp = timestamp;
        }
        
        // Getters and Setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        
        public int getPointsEarned() { return pointsEarned; }
        public void setPointsEarned(int pointsEarned) { this.pointsEarned = pointsEarned; }
        
        public int getTotalPoints() { return totalPoints; }
        public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }
        
        public int getPeActivityPoints() { return peActivityPoints; }
        public void setPeActivityPoints(int peActivityPoints) { this.peActivityPoints = peActivityPoints; }
        
        public int getMorningExercisePoints() { return morningExercisePoints; }
        public void setMorningExercisePoints(int morningExercisePoints) { this.morningExercisePoints = morningExercisePoints; }
        
        public String getActivityName() { return activityName; }
        public void setActivityName(String activityName) { this.activityName = activityName; }
        
        public String getEarnedReason() { return earnedReason; }
        public void setEarnedReason(String earnedReason) { this.earnedReason = earnedReason; }
        
        public String getCalculationRule() { return calculationRule; }
        public void setCalculationRule(String calculationRule) { this.calculationRule = calculationRule; }
        
        public int getParticipationDuration() { return participationDuration; }
        public void setParticipationDuration(int participationDuration) { this.participationDuration = participationDuration; }
        
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    }
    
    // 创建积分更新消息的工厂方法
    public static WebSocketMessage createPointsUpdateMessage(String userId, int pointsEarned, int totalPoints, 
                                                           int peActivityPoints, int morningExercisePoints,
                                                           String activityName, String earnedReason, 
                                                           String calculationRule, int participationDuration, 
                                                           String timestamp) {
        PointsUpdateData data = new PointsUpdateData(userId, pointsEarned, totalPoints, 
                                                   peActivityPoints, morningExercisePoints,
                                                   activityName, earnedReason, 
                                                   calculationRule, participationDuration, 
                                                   timestamp);
        return new WebSocketMessage("POINTS_UPDATE", data);
    }
} 