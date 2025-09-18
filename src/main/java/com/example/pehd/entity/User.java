package com.example.pehd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users1")
public class User implements UserDetails {
    
    @Id
    @Column(length = 36)
    private String id;
    
    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    
    @NotBlank(message = "学号不能为空")
    @Size(max = 20, message = "学号长度不能超过20个字符")
    @Column(name = "student_id", nullable = false, unique = true, length = 20)
    private String studentId;
    
    @Size(max = 100, message = "学校名称长度不能超过100个字符")
    @Column(name = "school", length = 100)
    private String school;
    
    @Size(max = 100, message = "学院名称长度不能超过100个字符")
    @Column(name = "college", length = 100)
    private String college;
    
    @Size(max = 100, message = "班级名称长度不能超过100个字符")
    @Column(name = "class_name", length = 100)
    private String className;
    
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Column(name = "phone_number", nullable = false, unique = true, length = 20)
    private String phoneNumber;
    
    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;
    
    @Column(name = "points", nullable = false)
    private Integer points = 0;
    
    @Column(name = "pe_activity_points", nullable = false)
    private Integer peActivityPoints = 0;
    
    @Column(name = "morning_exercise_points", nullable = false)
    private Integer morningExercisePoints = 0;
    
    @Column(name = "study_hours", nullable = false)
    private Integer studyHours = 0;
    
    @Column(name = "integrity_score", nullable = false)
    private Integer integrityScore = 100;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role = UserRole.STUDENT;
    
    @Column(name = "is_logged_in", nullable = false)
    private Boolean isLoggedIn = false;
    
    @Column(name = "points_last_updated", nullable = false)
    private LocalDateTime pointsLastUpdated;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.pointsLastUpdated = LocalDateTime.now();
        if (this.id == null) {
            this.id = java.util.UUID.randomUUID().toString();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public User() {}
    
    public User(String name, String studentId, String phoneNumber, String school, String college, String className) {
        this.name = name;
        this.studentId = studentId;
        this.phoneNumber = phoneNumber;
        this.school = school;
        this.college = college;
        this.className = className;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }
    
    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }
    
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
    
    public Integer getPeActivityPoints() { return peActivityPoints; }
    public void setPeActivityPoints(Integer peActivityPoints) { this.peActivityPoints = peActivityPoints; }
    
    public Integer getMorningExercisePoints() { return morningExercisePoints; }
    public void setMorningExercisePoints(Integer morningExercisePoints) { this.morningExercisePoints = morningExercisePoints; }
    
    public Integer getStudyHours() { return studyHours; }
    public void setStudyHours(Integer studyHours) { this.studyHours = studyHours; }
    
    public Integer getIntegrityScore() { return integrityScore; }
    public void setIntegrityScore(Integer integrityScore) { this.integrityScore = integrityScore; }
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    
    public Boolean getIsLoggedIn() { return isLoggedIn; }
    public void setIsLoggedIn(Boolean isLoggedIn) { this.isLoggedIn = isLoggedIn; }
    
    public LocalDateTime getPointsLastUpdated() { return pointsLastUpdated; }
    public void setPointsLastUpdated(LocalDateTime pointsLastUpdated) { this.pointsLastUpdated = pointsLastUpdated; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // UserDetails implementation
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
    
    @Override
    @JsonIgnore
    public String getPassword() {
        return null; // 不使用密码登录
    }
    
    @Override
    @JsonIgnore
    public String getUsername() {
        return this.studentId;
    }
    
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
} 