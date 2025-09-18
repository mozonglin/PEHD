package com.example.pehd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.example.pehd.entity.ActivityApprovalStatus;
import java.time.LocalDateTime;

public class ActivityDto {
    
    private String id;
    private String title;
    private String description;
    private String location;
    private Integer maxParticipants;
    private Integer currentParticipants;
    @JsonFormat(pattern = "MMM dd, yyyy h:mm:ss a", locale = "en_US")
    private LocalDateTime registrationStartTime;
    @JsonFormat(pattern = "MMM dd, yyyy h:mm:ss a", locale = "en_US")
    private LocalDateTime registrationEndTime;
    @JsonFormat(pattern = "MMM dd, yyyy h:mm:ss a", locale = "en_US")
    private LocalDateTime activityStartTime;
    @JsonFormat(pattern = "MMM dd, yyyy h:mm:ss a", locale = "en_US")
    private LocalDateTime activityEndTime;
    private String organizer;
    private String organizerId;
    private String category;
    private Integer points;
    private String imageUrl;
    private Boolean isRegistered = false;
    @JsonFormat(pattern = "MMM dd, yyyy h:mm:ss a", locale = "en_US")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "MMM dd, yyyy h:mm:ss a", locale = "en_US")
    private LocalDateTime updatedAt;
    
    // 审核状态相关字段
    private ActivityApprovalStatus approvalStatus;
    private String reviewedBy;
    @JsonFormat(pattern = "MMM dd, yyyy h:mm:ss a", locale = "en_US")
    private LocalDateTime reviewedAt;
    private String reviewComment;
    
    // Constructors
    public ActivityDto() {}
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }
    
    public Integer getCurrentParticipants() { return currentParticipants; }
    public void setCurrentParticipants(Integer currentParticipants) { this.currentParticipants = currentParticipants; }
    
    public LocalDateTime getRegistrationStartTime() { return registrationStartTime; }
    public void setRegistrationStartTime(LocalDateTime registrationStartTime) { this.registrationStartTime = registrationStartTime; }
    
    public LocalDateTime getRegistrationEndTime() { return registrationEndTime; }
    public void setRegistrationEndTime(LocalDateTime registrationEndTime) { this.registrationEndTime = registrationEndTime; }
    
    public LocalDateTime getActivityStartTime() { return activityStartTime; }
    public void setActivityStartTime(LocalDateTime activityStartTime) { this.activityStartTime = activityStartTime; }
    
    public LocalDateTime getActivityEndTime() { return activityEndTime; }
    public void setActivityEndTime(LocalDateTime activityEndTime) { this.activityEndTime = activityEndTime; }
    
    public String getOrganizer() { return organizer; }
    public void setOrganizer(String organizer) { this.organizer = organizer; }
    
    public String getOrganizerId() { return organizerId; }
    public void setOrganizerId(String organizerId) { this.organizerId = organizerId; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public Boolean getIsRegistered() { return isRegistered; }
    public void setIsRegistered(Boolean isRegistered) { this.isRegistered = isRegistered; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public ActivityApprovalStatus getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(ActivityApprovalStatus approvalStatus) { this.approvalStatus = approvalStatus; }
    
    public String getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; }
    
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
    
    public String getReviewComment() { return reviewComment; }
    public void setReviewComment(String reviewComment) { this.reviewComment = reviewComment; }
} 