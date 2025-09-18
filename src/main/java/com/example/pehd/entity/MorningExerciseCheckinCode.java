package com.example.pehd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "morning_exercise_checkin_codes")
public class MorningExerciseCheckinCode {
    
    @Id
    @Column(length = 36)
    private String id;
    
    @NotBlank(message = "唯一码不能为空")
    @Column(name = "unique_code", nullable = false, unique = true, length = 100)
    private String uniqueCode;
    
    @NotBlank(message = "早操ID不能为空")
    @Column(name = "exercise_id", nullable = false, length = 36)
    private String exerciseId;
    
    @NotBlank(message = "早操名称不能为空")
    @Column(name = "exercise_name", nullable = false, length = 200)
    private String exerciseName;
    
    @NotBlank(message = "学号不能为空")
    @Column(name = "student_id", nullable = false, length = 20)
    private String studentId;
    
    @NotBlank(message = "学生姓名不能为空")
    @Column(name = "student_name", nullable = false, length = 50)
    private String studentName;
    
    @NotNull(message = "时间戳不能为空")
    @Column(name = "timestamp", nullable = false)
    private Long timestamp;
    
    @NotNull(message = "过期时间不能为空")
    @Column(name = "expires_at", nullable = false)
    private Long expiresAt;
    
    @Column(name = "is_valid", nullable = false)
    private Boolean isValid = true;
    
    @Column(name = "is_used", nullable = false)
    private Boolean isUsed = false;
    
    @Column(name = "used_at")
    private LocalDateTime usedAt;
    
    @Column(name = "used_by", length = 36)
    private String usedBy;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
    
    // 默认构造函数
    public MorningExerciseCheckinCode() {}
    
    // 构造函数
    public MorningExerciseCheckinCode(String id, String uniqueCode, String exerciseId, 
                                    String exerciseName, String studentId, String studentName, 
                                    Long timestamp, Long expiresAt) {
        this.id = id;
        this.uniqueCode = uniqueCode;
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.studentId = studentId;
        this.studentName = studentName;
        this.timestamp = timestamp;
        this.expiresAt = expiresAt;
        this.isValid = true;
        this.isUsed = false;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getUniqueCode() {
        return uniqueCode;
    }
    
    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
    
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
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
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
    
    public Long getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public Boolean getIsValid() {
        return isValid;
    }
    
    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }
    
    public Boolean getIsUsed() {
        return isUsed;
    }
    
    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }
    
    public LocalDateTime getUsedAt() {
        return usedAt;
    }
    
    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }
    
    public String getUsedBy() {
        return usedBy;
    }
    
    public void setUsedBy(String usedBy) {
        this.usedBy = usedBy;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * 检查签到码是否已过期
     */
    public boolean isExpired() {
        return System.currentTimeMillis() > this.expiresAt;
    }
    
    /**
     * 检查签到码是否可用
     */
    public boolean isAvailable() {
        return this.isValid && !this.isUsed && !isExpired();
    }
    
    /**
     * 标记为已使用
     */
    public void markAsUsed(String usedById) {
        this.isUsed = true;
        this.usedAt = LocalDateTime.now();
        this.usedBy = usedById;
    }
}


