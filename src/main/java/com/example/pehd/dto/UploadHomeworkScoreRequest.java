package com.example.pehd.dto;

import com.example.pehd.entity.ExerciseType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class UploadHomeworkScoreRequest {
    
    @NotBlank(message = "学号不能为空")
    private String studentId;
    
    @NotNull(message = "项目类型不能为空")
    private ExerciseType exerciseType;
    
    @NotNull(message = "次数不能为空")
    @Min(value = 0, message = "次数不能为负数")
    private Integer count;
    
    // timestamp字段可选，如果不传则后端使用当前时间
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    // Constructors
    public UploadHomeworkScoreRequest() {}
    
    public UploadHomeworkScoreRequest(String studentId, ExerciseType exerciseType, Integer count, LocalDateTime timestamp) {
        this.studentId = studentId;
        this.exerciseType = exerciseType;
        this.count = count;
        this.timestamp = timestamp;
    }
    
    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public ExerciseType getExerciseType() { return exerciseType; }
    public void setExerciseType(ExerciseType exerciseType) { this.exerciseType = exerciseType; }
    
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}

