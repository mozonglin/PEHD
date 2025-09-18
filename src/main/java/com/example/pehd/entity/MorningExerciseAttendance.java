package com.example.pehd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "morning_exercise_attendance")
public class MorningExerciseAttendance {
    
    @Id
    @Column(length = 36)
    private String id;
    
    @NotBlank(message = "早操ID不能为空")
    @Column(name = "exercise_id", nullable = false, length = 36)
    private String exerciseId;
    
    @NotBlank(message = "学号不能为空")
    @Column(name = "student_id", nullable = false, length = 20)
    private String studentId;
    
    @NotBlank(message = "学生姓名不能为空")
    @Column(name = "student_name", nullable = false, length = 50)
    private String studentName;
    
    @Column(name = "check_in_time", nullable = false)
    private LocalDateTime checkInTime;
    
    @Column(name = "check_in_location", length = 200)
    private String checkInLocation = "";
    
    @NotBlank(message = "签到员ID不能为空")
    @Column(name = "checked_by", nullable = false, length = 36)
    private String checkedBy;
    
    @NotBlank(message = "签到员姓名不能为空")
    @Column(name = "checked_by_name", nullable = false, length = 50)
    private String checkedByName;
    
    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;
    
    @Column(name = "check_out_location", length = 200)
    private String checkOutLocation = "";
    
    @Column(name = "checked_out_by", length = 36)
    private String checkedOutBy;
    
    @Column(name = "checked_out_by_name", length = 50)
    private String checkedOutByName = "";
    
    @Column(name = "is_checked_out", nullable = false)
    private Boolean isCheckedOut = false;
    
    @Column(name = "points_earned", nullable = false)
    private Integer pointsEarned = 0;
    
    @Column(name = "qr_code_data", columnDefinition = "TEXT")
    private String qrCodeData;
    
    @Column(name = "is_valid", nullable = false)
    private Boolean isValid = true;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.id == null) {
            this.id = java.util.UUID.randomUUID().toString();
        }
        if (this.checkInTime == null) {
            this.checkInTime = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public MorningExerciseAttendance() {}
    
    public MorningExerciseAttendance(String exerciseId, String studentId, String studentName, 
                                   String checkInLocation, String checkedBy, String checkedByName, 
                                   String qrCodeData) {
        this.exerciseId = exerciseId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.checkInLocation = checkInLocation;
        this.checkedBy = checkedBy;
        this.checkedByName = checkedByName;
        this.qrCodeData = qrCodeData;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getExerciseId() { return exerciseId; }
    public void setExerciseId(String exerciseId) { this.exerciseId = exerciseId; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }
    
    public String getCheckInLocation() { return checkInLocation; }
    public void setCheckInLocation(String checkInLocation) { this.checkInLocation = checkInLocation; }
    
    public String getCheckedBy() { return checkedBy; }
    public void setCheckedBy(String checkedBy) { this.checkedBy = checkedBy; }
    
    public String getCheckedByName() { return checkedByName; }
    public void setCheckedByName(String checkedByName) { this.checkedByName = checkedByName; }
    
    public LocalDateTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalDateTime checkOutTime) { this.checkOutTime = checkOutTime; }
    
    public String getCheckOutLocation() { return checkOutLocation; }
    public void setCheckOutLocation(String checkOutLocation) { this.checkOutLocation = checkOutLocation; }
    
    public String getCheckedOutBy() { return checkedOutBy; }
    public void setCheckedOutBy(String checkedOutBy) { this.checkedOutBy = checkedOutBy; }
    
    public String getCheckedOutByName() { return checkedOutByName; }
    public void setCheckedOutByName(String checkedOutByName) { this.checkedOutByName = checkedOutByName; }
    
    public Boolean getIsCheckedOut() { return isCheckedOut; }
    public void setIsCheckedOut(Boolean isCheckedOut) { this.isCheckedOut = isCheckedOut; }
    
    public Integer getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(Integer pointsEarned) { this.pointsEarned = pointsEarned; }
    
    public String getQrCodeData() { return qrCodeData; }
    public void setQrCodeData(String qrCodeData) { this.qrCodeData = qrCodeData; }
    
    public Boolean getIsValid() { return isValid; }
    public void setIsValid(Boolean isValid) { this.isValid = isValid; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}