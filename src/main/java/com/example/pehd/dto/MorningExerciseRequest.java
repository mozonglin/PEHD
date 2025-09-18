package com.example.pehd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MorningExerciseRequest {
    
    @NotBlank(message = "早操ID不能为空")
    private String exerciseId;
    
    @NotBlank(message = "早操名称不能为空")
    private String exerciseName;
    
    @NotBlank(message = "学生姓名不能为空")
    private String studentName;
    
    @NotNull(message = "时间戳不能为空")
    private Long timestamp;
    
    // 默认构造函数
    public MorningExerciseRequest() {}
    
    // 构造函数
    public MorningExerciseRequest(String exerciseId, String exerciseName, String studentName, Long timestamp) {
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.studentName = studentName;
        this.timestamp = timestamp;
    }
    
    // Getters and Setters
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
}


