package com.example.pehd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class MorningExerciseAttendanceDto {
    private String id;
    private String exerciseId;
    private String studentId;
    private String studentName;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkInTime;
    
    private String checkInLocation;
    private String checkedBy;
    private String checkedByName;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkOutTime;
    
    private String checkOutLocation;
    private String checkedOutBy;
    private String checkedOutByName;
    private Boolean isCheckedOut;
    private Integer pointsEarned;
    private String calculationRule;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime earnedAt;
    
    // 默认构造函数
    public MorningExerciseAttendanceDto() {}
    
    // 构造函数
    public MorningExerciseAttendanceDto(String id, String exerciseId, String studentId, String studentName,
                                      LocalDateTime checkInTime, String checkInLocation, String checkedBy,
                                      String checkedByName, LocalDateTime checkOutTime, String checkOutLocation,
                                      String checkedOutBy, String checkedOutByName, Boolean isCheckedOut,
                                      Integer pointsEarned, String calculationRule, LocalDateTime earnedAt) {
        this.id = id;
        this.exerciseId = exerciseId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.checkInTime = checkInTime;
        this.checkInLocation = checkInLocation;
        this.checkedBy = checkedBy;
        this.checkedByName = checkedByName;
        this.checkOutTime = checkOutTime;
        this.checkOutLocation = checkOutLocation;
        this.checkedOutBy = checkedOutBy;
        this.checkedOutByName = checkedOutByName;
        this.isCheckedOut = isCheckedOut;
        this.pointsEarned = pointsEarned;
        this.calculationRule = calculationRule;
        this.earnedAt = earnedAt;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getExerciseId() {
        return exerciseId;
    }
    
    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
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
    
    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }
    
    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }
    
    public String getCheckInLocation() {
        return checkInLocation;
    }
    
    public void setCheckInLocation(String checkInLocation) {
        this.checkInLocation = checkInLocation;
    }
    
    public String getCheckedBy() {
        return checkedBy;
    }
    
    public void setCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
    }
    
    public String getCheckedByName() {
        return checkedByName;
    }
    
    public void setCheckedByName(String checkedByName) {
        this.checkedByName = checkedByName;
    }
    
    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }
    
    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }
    
    public String getCheckOutLocation() {
        return checkOutLocation;
    }
    
    public void setCheckOutLocation(String checkOutLocation) {
        this.checkOutLocation = checkOutLocation;
    }
    
    public String getCheckedOutBy() {
        return checkedOutBy;
    }
    
    public void setCheckedOutBy(String checkedOutBy) {
        this.checkedOutBy = checkedOutBy;
    }
    
    public String getCheckedOutByName() {
        return checkedOutByName;
    }
    
    public void setCheckedOutByName(String checkedOutByName) {
        this.checkedOutByName = checkedOutByName;
    }
    
    public Boolean getIsCheckedOut() {
        return isCheckedOut;
    }
    
    public void setIsCheckedOut(Boolean isCheckedOut) {
        this.isCheckedOut = isCheckedOut;
    }
    
    public Integer getPointsEarned() {
        return pointsEarned;
    }
    
    public void setPointsEarned(Integer pointsEarned) {
        this.pointsEarned = pointsEarned;
    }
    
    public String getCalculationRule() {
        return calculationRule;
    }
    
    public void setCalculationRule(String calculationRule) {
        this.calculationRule = calculationRule;
    }
    
    public LocalDateTime getEarnedAt() {
        return earnedAt;
    }
    
    public void setEarnedAt(LocalDateTime earnedAt) {
        this.earnedAt = earnedAt;
    }
}
