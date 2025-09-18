package com.example.pehd.dto;

public class MorningExerciseCodeResponse {
    private String uniqueCode;
    private String exerciseId;
    private String exerciseName;
    private String studentId;
    private String studentName;
    private Long timestamp;
    private Long expiresAt;
    private Boolean isValid;
    
    // 默认构造函数
    public MorningExerciseCodeResponse() {}
    
    // 构造函数
    public MorningExerciseCodeResponse(String uniqueCode, String exerciseId, String exerciseName,
                                     String studentId, String studentName, Long timestamp,
                                     Long expiresAt, Boolean isValid) {
        this.uniqueCode = uniqueCode;
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.studentId = studentId;
        this.studentName = studentName;
        this.timestamp = timestamp;
        this.expiresAt = expiresAt;
        this.isValid = isValid;
    }
    
    // Getters and Setters
    public String getUniqueCode() {
        return uniqueCode;
    }
    
    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
    
    public String getExerciseId() {
        return exerciseId;
    }
    
    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }
    
    public String getExerciseName() {
        return exerciseName;
    }
    
    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    public Long getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public Boolean getIsValid() {
        return isValid;
    }
    
    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }
}


