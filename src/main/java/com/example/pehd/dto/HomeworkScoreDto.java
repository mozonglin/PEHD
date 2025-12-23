package com.example.pehd.dto;

import com.example.pehd.entity.ExerciseType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class HomeworkScoreDto {
    
    private String id;
    private String studentId;
    private ExerciseType exerciseType;
    private Integer count;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    // Constructors
    public HomeworkScoreDto() {}
    
    public HomeworkScoreDto(String id, String studentId, ExerciseType exerciseType, Integer count, LocalDateTime timestamp) {
        this.id = id;
        this.studentId = studentId;
        this.exerciseType = exerciseType;
        this.count = count;
        this.timestamp = timestamp;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public ExerciseType getExerciseType() { return exerciseType; }
    public void setExerciseType(ExerciseType exerciseType) { this.exerciseType = exerciseType; }
    
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}

