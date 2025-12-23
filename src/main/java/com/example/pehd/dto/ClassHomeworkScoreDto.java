package com.example.pehd.dto;

import com.example.pehd.entity.ExerciseType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ClassHomeworkScoreDto {
    
    private String id;
    private String studentId;
    private String studentName;
    private String className;
    private ExerciseType exerciseType;
    private Integer count;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    // Constructors
    public ClassHomeworkScoreDto() {}
    
    public ClassHomeworkScoreDto(String id, String studentId, String studentName, String className,
                                  ExerciseType exerciseType, Integer count, LocalDateTime timestamp) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.className = className;
        this.exerciseType = exerciseType;
        this.count = count;
        this.timestamp = timestamp;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    
    public ExerciseType getExerciseType() { return exerciseType; }
    public void setExerciseType(ExerciseType exerciseType) { this.exerciseType = exerciseType; }
    
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}

