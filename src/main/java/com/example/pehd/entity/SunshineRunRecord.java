package com.example.pehd.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 阳光跑记录实体
 */
@Entity
@Table(name = "sunshine_run_records")
public class SunshineRunRecord {
    
    @Id
    @Column(length = 36)
    private String id;
    
    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;
    
    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;
    
    @Column(name = "student_id", nullable = false, length = 20)
    private String studentId;
    
    @Column(name = "class_name", length = 100)
    private String className;
    
    @Column(name = "start_time", nullable = false)
    private Long startTime;
    
    @Column(name = "end_time", nullable = false)
    private Long endTime;
    
    @Column(name = "total_distance", nullable = false)
    private Double totalDistance;
    
    @Column(name = "total_duration", nullable = false)
    private Long totalDuration;
    
    @Column(name = "avg_pace", nullable = false)
    private Double avgPace;
    
    @Column(name = "calories", nullable = false)
    private Integer calories;
    
    @Column(name = "check_points_count", nullable = false)
    private Integer checkPointsCount;
    
    @Column(name = "total_check_points", nullable = false)
    private Integer totalCheckPoints;
    
    @Column(name = "path_points", columnDefinition = "TEXT")
    private String pathPoints;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.id == null) {
            this.id = java.util.UUID.randomUUID().toString();
        }
    }
    
    // Constructors
    public SunshineRunRecord() {}
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    
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
    
    public String getPathPoints() { return pathPoints; }
    public void setPathPoints(String pathPoints) { this.pathPoints = pathPoints; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

