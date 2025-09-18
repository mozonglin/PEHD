package com.example.pehd.dto;

import java.time.LocalDateTime;

/**
 * 班级早操考勤记录DTO
 */
public class ClassAttendanceRecordDto {
    
    private String studentId;
    private String studentName;
    private String className;
    private LocalDateTime checkInTime;
    private String checkInLocation;
    private LocalDateTime checkOutTime;
    private String checkOutLocation;
    private Boolean isCheckedOut;
    private Integer pointsEarned;
    private String checkedByName;
    private String checkedOutByName;
    
    // 构造函数
    public ClassAttendanceRecordDto() {}
    
    public ClassAttendanceRecordDto(String studentId, String studentName, String className, 
                                  LocalDateTime checkInTime, String checkInLocation,
                                  LocalDateTime checkOutTime, String checkOutLocation,
                                  Boolean isCheckedOut, Integer pointsEarned,
                                  String checkedByName, String checkedOutByName) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.className = className;
        this.checkInTime = checkInTime;
        this.checkInLocation = checkInLocation;
        this.checkOutTime = checkOutTime;
        this.checkOutLocation = checkOutLocation;
        this.isCheckedOut = isCheckedOut;
        this.pointsEarned = pointsEarned;
        this.checkedByName = checkedByName;
        this.checkedOutByName = checkedOutByName;
    }
    
    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    
    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }
    
    public String getCheckInLocation() { return checkInLocation; }
    public void setCheckInLocation(String checkInLocation) { this.checkInLocation = checkInLocation; }
    
    public LocalDateTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalDateTime checkOutTime) { this.checkOutTime = checkOutTime; }
    
    public String getCheckOutLocation() { return checkOutLocation; }
    public void setCheckOutLocation(String checkOutLocation) { this.checkOutLocation = checkOutLocation; }
    
    public Boolean getIsCheckedOut() { return isCheckedOut; }
    public void setIsCheckedOut(Boolean isCheckedOut) { this.isCheckedOut = isCheckedOut; }
    
    public Integer getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(Integer pointsEarned) { this.pointsEarned = pointsEarned; }
    
    public String getCheckedByName() { return checkedByName; }
    public void setCheckedByName(String checkedByName) { this.checkedByName = checkedByName; }
    
    public String getCheckedOutByName() { return checkedOutByName; }
    public void setCheckedOutByName(String checkedOutByName) { this.checkedOutByName = checkedOutByName; }
}
