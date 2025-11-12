package com.example.pehd.dto;

/**
 * 阳光跑记录DTO
 */
public class SunshineRunRecordDto {
    
    private String id;
    private String userName;
    private String studentId;
    private Long startTime;
    private Long endTime;
    private Double totalDistance;
    private Long totalDuration;
    private Double avgPace;
    private Integer calories;
    private Integer checkPointsCount;
    private Integer totalCheckPoints;
    private Long createdAt;
    
    // Constructors
    public SunshineRunRecordDto() {}
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
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
    
    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
}

