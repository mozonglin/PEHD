package com.example.pehd.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

/**
 * 阳光跑上传请求DTO
 */
public class SunshineRunUploadRequest {
    
    @NotNull(message = "开始时间不能为空")
    private Long startTime;
    
    @NotNull(message = "结束时间不能为空")
    private Long endTime;
    
    @NotNull(message = "总距离不能为空")
    @Positive(message = "总距离必须大于0")
    private Double totalDistance;
    
    @NotNull(message = "总时长不能为空")
    @Positive(message = "总时长必须大于0")
    private Long totalDuration;
    
    @NotNull(message = "平均配速不能为空")
    private Double avgPace;
    
    @NotNull(message = "卡路里不能为空")
    private Integer calories;
    
    @NotNull(message = "完成打卡点数量不能为空")
    private Integer checkPointsCount;
    
    @NotNull(message = "总打卡点数量不能为空")
    private Integer totalCheckPoints;
    
    private List<PathPoint> pathPoints;
    
    // Constructors
    public SunshineRunUploadRequest() {}
    
    // Getters and Setters
    public Long getStartTime() { return startTime; }
    public void setStartTime(Long startTime) { this.startTime = startTime; }
    
    public Long getEndTime() { return endTime; }
    public void setEndTime(Long endTime) { this.endTime = endTime; }
    
    public Double getTotalDistance() { return totalDistance; }
    public void setTotalDistance(Double totalDistance) { this.totalDistance = totalDistance; }
    
    public Long getTotalDuration() { return totalDuration; }
    public void setTotalDuration(Long totalDuration) { this.totalDuration = totalDuration; }
    
    public Double getAvgPace() { return avgPace; }
    public void setAvgPace(Double avgPace) { this.avgPace = avgPace; }
    
    public Integer getCalories() { return calories; }
    public void setCalories(Integer calories) { this.calories = calories; }
    
    public Integer getCheckPointsCount() { return checkPointsCount; }
    public void setCheckPointsCount(Integer checkPointsCount) { this.checkPointsCount = checkPointsCount; }
    
    public Integer getTotalCheckPoints() { return totalCheckPoints; }
    public void setTotalCheckPoints(Integer totalCheckPoints) { this.totalCheckPoints = totalCheckPoints; }
    
    public List<PathPoint> getPathPoints() { return pathPoints; }
    public void setPathPoints(List<PathPoint> pathPoints) { this.pathPoints = pathPoints; }
    
    /**
     * 路径点内部类
     */
    public static class PathPoint {
        private Double latitude;
        private Double longitude;
        
        public PathPoint() {}
        
        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
    }
}

