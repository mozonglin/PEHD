package com.example.pehd.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "homework_assignments", indexes = {
    @Index(name = "idx_assignment_temp_class", columnList = "temp_class_id"),
    @Index(name = "idx_assignment_teacher", columnList = "teacher_id"),
    @Index(name = "idx_assignment_status", columnList = "status")
})
public class HomeworkAssignment {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "teacher_id", length = 36, nullable = false)
    private String teacherId;

    @Column(name = "temp_class_id", length = 36, nullable = false)
    private String tempClassId;

    @Column(name = "school", length = 100)
    private String school;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "exercise_type", length = 20)
    private ExerciseType exerciseType;

    @Column(name = "required_count")
    private Integer requiredCount;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.id == null) {
            this.id = java.util.UUID.randomUUID().toString();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public HomeworkAssignment() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }

    public String getTempClassId() { return tempClassId; }
    public void setTempClassId(String tempClassId) { this.tempClassId = tempClassId; }

    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public ExerciseType getExerciseType() { return exerciseType; }
    public void setExerciseType(ExerciseType exerciseType) { this.exerciseType = exerciseType; }

    public Integer getRequiredCount() { return requiredCount; }
    public void setRequiredCount(Integer requiredCount) { this.requiredCount = requiredCount; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
