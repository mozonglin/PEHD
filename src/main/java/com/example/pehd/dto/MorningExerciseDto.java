package com.example.pehd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MorningExerciseDto {
    private String id;
    private String title;
    private String description;
    private String location;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
    
    private String type;
    private Boolean isActive;
    private Integer totalParticipants;
    private Integer checkedInCount;
    private Integer checkedOutCount;
    private String createdBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    // 默认构造函数
    public MorningExerciseDto() {}
    
    // 构造函数
    public MorningExerciseDto(String id, String title, String description, String location,
                            LocalDate date, LocalDateTime startTime, LocalDateTime endTime,
                            String type, Boolean isActive, Integer totalParticipants,
                            Integer checkedInCount, Integer checkedOutCount, String createdBy,
                            LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.isActive = isActive;
        this.totalParticipants = totalParticipants;
        this.checkedInCount = checkedInCount;
        this.checkedOutCount = checkedOutCount;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Integer getTotalParticipants() {
        return totalParticipants;
    }
    
    public void setTotalParticipants(Integer totalParticipants) {
        this.totalParticipants = totalParticipants;
    }
    
    public Integer getCheckedInCount() {
        return checkedInCount;
    }
    
    public void setCheckedInCount(Integer checkedInCount) {
        this.checkedInCount = checkedInCount;
    }
    
    public Integer getCheckedOutCount() {
        return checkedOutCount;
    }
    
    public void setCheckedOutCount(Integer checkedOutCount) {
        this.checkedOutCount = checkedOutCount;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
