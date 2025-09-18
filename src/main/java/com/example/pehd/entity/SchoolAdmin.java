package com.example.pehd.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 学校管理员实体（映射users表）
 * 仅用于获取管理员设置的积分目标
 */
@Entity
@Table(name = "users")
public class SchoolAdmin {
    
    @Id
    @Column(length = 36)
    private String id;
    
    @Column(name = "username", length = 50)
    private String username;
    
    @Column(name = "real_name", length = 50)
    private String realName;
    
    @Column(name = "student_id", length = 20)
    private String studentId;
    
    @Column(name = "school", length = 100)
    private String school;
    
    @Column(name = "department_name", length = 100)
    private String departmentName;
    
    @Column(name = "user_type", length = 20)
    private String userType;
    
    @Column(name = "weekly_target")
    private Integer weeklyTarget;
    
    @Column(name = "monthly_target")
    private Integer monthlyTarget;
    
    @Column(name = "total_target")
    private Integer totalTarget;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 构造函数
    public SchoolAdmin() {}
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }
    
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
    
    public Integer getWeeklyTarget() { return weeklyTarget; }
    public void setWeeklyTarget(Integer weeklyTarget) { this.weeklyTarget = weeklyTarget; }
    
    public Integer getMonthlyTarget() { return monthlyTarget; }
    public void setMonthlyTarget(Integer monthlyTarget) { this.monthlyTarget = monthlyTarget; }
    
    public Integer getTotalTarget() { return totalTarget; }
    public void setTotalTarget(Integer totalTarget) { this.totalTarget = totalTarget; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
