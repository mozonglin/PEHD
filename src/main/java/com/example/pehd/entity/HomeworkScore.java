package com.example.pehd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

@Entity
@Table(name = "homework_scores", indexes = {
    @Index(name = "idx_student_id", columnList = "student_id"),
    @Index(name = "idx_exercise_type", columnList = "exercise_type"),
    @Index(name = "idx_timestamp", columnList = "timestamp"),
    @Index(name = "idx_student_exercise", columnList = "student_id, exercise_type")
})
public class HomeworkScore {
    
    @Id
    @Column(length = 36)
    private String id;
    
    @NotBlank(message = "学号不能为空")
    @Column(name = "student_id", nullable = false, length = 20)
    private String studentId;
    
    @NotNull(message = "项目类型不能为空")
    @Enumerated(EnumType.STRING)
    @Column(name = "exercise_type", nullable = false, length = 20)
    private ExerciseType exerciseType;
    
    @NotNull(message = "次数不能为空")
    @Min(value = 0, message = "次数不能为负数")
    @Column(name = "count", nullable = false)
    private Integer count;
    
    @NotNull(message = "提交时间不能为空")
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.id == null) {
            this.id = java.util.UUID.randomUUID().toString();
        }
    }
    
    // Constructors
    public HomeworkScore() {}
    
    public HomeworkScore(String studentId, ExerciseType exerciseType, Integer count, LocalDateTime timestamp) {
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
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

