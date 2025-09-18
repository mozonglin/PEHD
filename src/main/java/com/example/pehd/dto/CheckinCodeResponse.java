package com.example.pehd.dto;

public class CheckinCodeResponse {
    
    private String uniqueCode;
    private String activityId;
    private String activityName;
    private String studentId;
    private String studentName;
    private Long timestamp;
    private Long expiresAt;
    private Boolean isValid;
    
    // Constructors
    public CheckinCodeResponse() {}
    
    public CheckinCodeResponse(String uniqueCode, String activityId, String activityName, 
                              String studentId, String studentName, Long timestamp, Long expiresAt, Boolean isValid) {
        this.uniqueCode = uniqueCode;
        this.activityId = activityId;
        this.activityName = activityName;
        this.studentId = studentId;
        this.studentName = studentName;
        this.timestamp = timestamp;
        this.expiresAt = expiresAt;
        this.isValid = isValid;
    }
    
    // Getters and Setters
    public String getUniqueCode() { return uniqueCode; }
    public void setUniqueCode(String uniqueCode) { this.uniqueCode = uniqueCode; }
    
    public String getActivityId() { return activityId; }
    public void setActivityId(String activityId) { this.activityId = activityId; }
    
    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
    
    public Long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Long expiresAt) { this.expiresAt = expiresAt; }
    
    public Boolean getIsValid() { return isValid; }
    public void setIsValid(Boolean isValid) { this.isValid = isValid; }
} 