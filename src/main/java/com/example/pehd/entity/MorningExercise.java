package com.example.pehd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "morning_exercises")
public class MorningExercise {
    
    @Id
    @Column(length = 36)
    private String id;
    
    @NotBlank(message = "活动标题不能为空")
    @Column(name = "title", nullable = false, length = 200)
    private String title = "早操考勤";
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description = "每日早操考勤";
    
    @Column(name = "location", length = 200)
    private String location = "操场";
    
    @NotNull(message = "日期不能为空")
    @Column(name = "date", nullable = false)
    private LocalDate date;
    
    @NotNull(message = "开始时间不能为空")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @NotNull(message = "结束时间不能为空")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ExerciseType type = ExerciseType.MORNING_EXERCISE;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "total_participants", nullable = false)
    private Integer totalParticipants = 0;
    
    @Column(name = "checked_in_count", nullable = false)
    private Integer checkedInCount = 0;
    
    @Column(name = "checked_out_count", nullable = false)
    private Integer checkedOutCount = 0;
    
    @NotBlank(message = "创建者ID不能为空")
    @Column(name = "created_by", nullable = false, length = 36)
    private String createdBy;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.id == null) {
            this.id = java.util.UUID.randomUUID().toString();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public MorningExercise() {}
    
    public MorningExercise(String title, String description, String location, LocalDate date, 
                          LocalDateTime startTime, LocalDateTime endTime, String createdBy) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    
    public ExerciseType getType() { return type; }
    public void setType(ExerciseType type) { this.type = type; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public Integer getTotalParticipants() { return totalParticipants; }
    public void setTotalParticipants(Integer totalParticipants) { this.totalParticipants = totalParticipants; }
    
    public Integer getCheckedInCount() { return checkedInCount; }
    public void setCheckedInCount(Integer checkedInCount) { this.checkedInCount = checkedInCount; }
    
    public Integer getCheckedOutCount() { return checkedOutCount; }
    public void setCheckedOutCount(Integer checkedOutCount) { this.checkedOutCount = checkedOutCount; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public enum ExerciseType {
        MORNING_EXERCISE
    }
}