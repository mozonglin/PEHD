package com.example.pehd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class LoginRequest {
    
    @NotBlank(message = "姓名不能为空")
    private String name;
    
    @NotBlank(message = "学号不能为空")
    private String studentId;
    
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phoneNumber;
    
    @NotBlank(message = "验证码不能为空")
    private String verificationCode;
    
    private String school;
    
    // Constructors
    public LoginRequest() {}
    
    public LoginRequest(String name, String studentId, String phoneNumber, String verificationCode) {
        this.name = name;
        this.studentId = studentId;
        this.phoneNumber = phoneNumber;
        this.verificationCode = verificationCode;
    }
    
    public LoginRequest(String name, String studentId, String phoneNumber, String verificationCode, String school) {
        this.name = name;
        this.studentId = studentId;
        this.phoneNumber = phoneNumber;
        this.verificationCode = verificationCode;
        this.school = school;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getVerificationCode() { return verificationCode; }
    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }
    
    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }
} 