package com.example.pehd.dto;

import com.example.pehd.entity.ExerciseType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class BestScoreDto {
    
    private String studentId;
    private ExerciseType exerciseType;
    private Integer bestCount;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    // Constructors
    public BestScoreDto() {}
    
    public BestScoreDto(String studentId, ExerciseType exerciseType, Integer bestCount, LocalDateTime timestamp) {
        this.studentId = studentId;
        this.exerciseType = exerciseType;
        this.bestCount = bestCount;
        this.timestamp = timestamp;
    }
    
    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public ExerciseType getExerciseType() { return exerciseType; }
    public void setExerciseType(ExerciseType exerciseType) { this.exerciseType = exerciseType; }
    
    public Integer getBestCount() { return bestCount; }
    public void setBestCount(Integer bestCount) { this.bestCount = bestCount; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}

