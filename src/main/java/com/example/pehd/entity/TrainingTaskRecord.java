package com.example.pehd.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "training_task_records")
public class TrainingTaskRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id", nullable = false) private Long taskId;
    @Column(name = "user_id", nullable = false, length = 50) private String userId;
    @Column(name = "student_id", nullable = false, length = 30) private String studentId;
    @Column(name = "student_name", nullable = false, length = 50) private String studentName;
    @Column(name = "class_name", length = 100) private String className;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private RecordStatus status = RecordStatus.pending;
    @Column(name = "completion_data", columnDefinition = "TEXT") private String completionData;
    @Column(name = "completion_rate", precision = 5, scale = 2) private BigDecimal completionRate = BigDecimal.ZERO;
    @Column(columnDefinition = "TEXT") private String feedback;
    @Column(precision = 5, scale = 1) private BigDecimal score;
    @Column(name = "started_at") private LocalDateTime startedAt;
    @Column(name = "completed_at") private LocalDateTime completedAt;
    @Column(name = "created_at", updatable = false) private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", insertable = false, updatable = false)
    private TrainingTask task;

    @PrePersist protected void onCreate() { this.createdAt = LocalDateTime.now(); this.updatedAt = LocalDateTime.now(); }
    @PreUpdate protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public enum RecordStatus { pending, in_progress, completed, overdue }

    public TrainingTaskRecord() {}

    public Long getId() { return id; }
    public Long getTaskId() { return taskId; }
    public String getUserId() { return userId; }
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getClassName() { return className; }
    public RecordStatus getStatus() { return status; }
    public void setStatus(RecordStatus status) { this.status = status; }
    public String getCompletionData() { return completionData; }
    public void setCompletionData(String v) { this.completionData = v; }
    public BigDecimal getCompletionRate() { return completionRate; }
    public void setCompletionRate(BigDecimal v) { this.completionRate = v; }
    public String getFeedback() { return feedback; }
    public BigDecimal getScore() { return score; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime v) { this.startedAt = v; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime v) { this.completedAt = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public TrainingTask getTask() { return task; }
}
