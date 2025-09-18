package com.example.pehd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_points_summary")
public class UserPointsSummary {
    
    @Id
    @NotBlank(message = "用户ID不能为空")
    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;
    
    @Column(name = "total_points", nullable = false)
    private Integer totalPoints = 0;
    
    @Column(name = "pe_activity_points", nullable = false)
    private Integer peActivityPoints = 0;
    
    @Column(name = "morning_exercise_points", nullable = false)
    private Integer morningExercisePoints = 0;
    
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;
    
    @PrePersist
    protected void onCreate() {
        this.lastUpdated = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }
    
    // Constructors
    public UserPointsSummary() {}
    
    public UserPointsSummary(String userId, Integer totalPoints, Integer peActivityPoints, 
                           Integer morningExercisePoints) {
        this.userId = userId;
        this.totalPoints = totalPoints;
        this.peActivityPoints = peActivityPoints;
        this.morningExercisePoints = morningExercisePoints;
    }
    
    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public Integer getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }
    
    public Integer getPeActivityPoints() { return peActivityPoints; }
    public void setPeActivityPoints(Integer peActivityPoints) { this.peActivityPoints = peActivityPoints; }
    
    public Integer getMorningExercisePoints() { return morningExercisePoints; }
    public void setMorningExercisePoints(Integer morningExercisePoints) { this.morningExercisePoints = morningExercisePoints; }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}