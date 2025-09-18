package com.example.pehd.dto;

import jakarta.validation.constraints.NotBlank;

public class CheckinCodeRequest {
    
    @NotBlank(message = "活动ID不能为空")
    private String activityId;
    
    @NotBlank(message = "活动名称不能为空")
    private String activityName;
    
    @NotBlank(message = "学生姓名不能为空")
    private String studentName;
    
    private Long timestamp;
    
    // Constructors
    public CheckinCodeRequest() {}
    
    public CheckinCodeRequest(String activityId, String activityName, String studentName, Long timestamp) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.studentName = studentName;
        this.timestamp = timestamp;
    }
    
    // Getters and Setters
    public String getActivityId() { return activityId; }
    public void setActivityId(String activityId) { this.activityId = activityId; }
    
    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
} 