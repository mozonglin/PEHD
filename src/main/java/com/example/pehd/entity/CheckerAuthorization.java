package com.example.pehd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "checker_authorizations")
public class CheckerAuthorization {
    
    @Id
    @Column(length = 36)
    private String id;
    
    @NotBlank(message = "学号不能为空")
    @Column(name = "student_id", nullable = false, length = 20)
    private String studentId;
    
    @NotBlank(message = "学生姓名不能为空")
    @Column(name = "student_name", nullable = false, length = 50)
    private String studentName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private CheckerRole role;
    
    @NotBlank(message = "授权人ID不能为空")
    @Column(name = "authorized_by", nullable = false, length = 36)
    private String authorizedBy;
    
    @NotBlank(message = "授权人姓名不能为空")
    @Column(name = "authorized_by_name", nullable = false, length = 50)
    private String authorizedByName;
    
    @Column(name = "authorized_at", nullable = false)
    private LocalDateTime authorizedAt;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @NotBlank(message = "早操ID不能为空")
    @Column(name = "exercise_id", nullable = false, length = 36)
    private String exerciseId;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.authorizedAt = LocalDateTime.now();
        if (this.id == null) {
            this.id = java.util.UUID.randomUUID().toString();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public CheckerAuthorization() {}
    
    public CheckerAuthorization(String studentId, String studentName, CheckerRole role, 
                              String authorizedBy, String authorizedByName, String exerciseId) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.role = role;
        this.authorizedBy = authorizedBy;
        this.authorizedByName = authorizedByName;
        this.exerciseId = exerciseId;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public CheckerRole getRole() { return role; }
    public void setRole(CheckerRole role) { this.role = role; }
    
    public String getAuthorizedBy() { return authorizedBy; }
    public void setAuthorizedBy(String authorizedBy) { this.authorizedBy = authorizedBy; }
    
    public String getAuthorizedByName() { return authorizedByName; }
    public void setAuthorizedByName(String authorizedByName) { this.authorizedByName = authorizedByName; }
    
    public LocalDateTime getAuthorizedAt() { return authorizedAt; }
    public void setAuthorizedAt(LocalDateTime authorizedAt) { this.authorizedAt = authorizedAt; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public String getExerciseId() { return exerciseId; }
    public void setExerciseId(String exerciseId) { this.exerciseId = exerciseId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public enum CheckerRole {
        CHECKER, SUB_CHECKER
    }
}