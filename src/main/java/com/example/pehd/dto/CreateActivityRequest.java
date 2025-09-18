package com.example.pehd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

public class CreateActivityRequest {
    
    @NotBlank(message = "活动标题不能为空")
    private String title;
    
    private String description;
    
    @NotBlank(message = "活动地点不能为空")
    private String location;
    
    @NotNull(message = "最大参与人数不能为空")
    @Min(value = 1, message = "最大参与人数必须大于0")
    private Integer maxParticipants;
    
    @NotNull(message = "报名开始时间不能为空")
    private LocalDateTime registrationStartTime;
    
    @NotNull(message = "报名结束时间不能为空")
    private LocalDateTime registrationEndTime;
    
    @NotNull(message = "活动开始时间不能为空")
    private LocalDateTime activityStartTime;
    
    @NotNull(message = "活动结束时间不能为空")
    private LocalDateTime activityEndTime;
    
    private String category;
    
    @Min(value = 0, message = "积分不能为负数")
    private Integer points = 0;
    
    private String imageUrl;
    
    // 是否保存为草稿
    private Boolean isDraft = false;
    
    // Constructors
    public CreateActivityRequest() {}
    
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }
    
    public LocalDateTime getRegistrationStartTime() { return registrationStartTime; }
    public void setRegistrationStartTime(LocalDateTime registrationStartTime) { this.registrationStartTime = registrationStartTime; }
    
    public LocalDateTime getRegistrationEndTime() { return registrationEndTime; }
    public void setRegistrationEndTime(LocalDateTime registrationEndTime) { this.registrationEndTime = registrationEndTime; }
    
    public LocalDateTime getActivityStartTime() { return activityStartTime; }
    public void setActivityStartTime(LocalDateTime activityStartTime) { this.activityStartTime = activityStartTime; }
    
    public LocalDateTime getActivityEndTime() { return activityEndTime; }
    public void setActivityEndTime(LocalDateTime activityEndTime) { this.activityEndTime = activityEndTime; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public Boolean getIsDraft() { return isDraft; }
    public void setIsDraft(Boolean isDraft) { this.isDraft = isDraft; }
} 