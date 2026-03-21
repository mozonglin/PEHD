package com.example.pehd.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "training_tasks")
public class TrainingTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "teacher_id", nullable = false, length = 50) private String teacherId;
    @Column(name = "teacher_name", nullable = false, length = 50) private String teacherName;
    @Column(nullable = false, length = 200) private String title;
    @Column(columnDefinition = "TEXT") private String description;
    @Enumerated(EnumType.STRING) @Column(name = "training_type", nullable = false) private TrainingType trainingType;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Difficulty difficulty;
    @Column(name = "target_classes", nullable = false, columnDefinition = "TEXT") private String targetClasses;
    @Column(name = "course_id") private Long courseId;
    @Column(name = "start_date", nullable = false) private LocalDate startDate;
    @Column(name = "end_date", nullable = false) private LocalDate endDate;
    @Column(columnDefinition = "TEXT") private String requirements;
    @Column(name = "ai_generated") private Boolean aiGenerated = false;
    @Column(name = "ai_reference", columnDefinition = "TEXT") private String aiReference;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private TaskStatus status = TaskStatus.active;
    @Column(name = "total_students") private Integer totalStudents = 0;
    @Column(name = "completed_students") private Integer completedStudents = 0;
    @Column(name = "created_at", updatable = false) private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;

    @PrePersist protected void onCreate() { this.createdAt = LocalDateTime.now(); this.updatedAt = LocalDateTime.now(); }
    @PreUpdate protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public enum TrainingType { endurance, strength, flexibility, skill, comprehensive }
    public enum Difficulty { easy, medium, hard }
    public enum TaskStatus { draft, active, completed, cancelled }

    public TrainingTask() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTeacherId() { return teacherId; }
    public String getTeacherName() { return teacherName; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public TrainingType getTrainingType() { return trainingType; }
    public Difficulty getDifficulty() { return difficulty; }
    public String getTargetClasses() { return targetClasses; }
    public Long getCourseId() { return courseId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getRequirements() { return requirements; }
    public Boolean getAiGenerated() { return aiGenerated; }
    public String getAiReference() { return aiReference; }
    public TaskStatus getStatus() { return status; }
    public Integer getTotalStudents() { return totalStudents; }
    public Integer getCompletedStudents() { return completedStudents; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
