package com.example.pehd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UpdatePhoneRequest {
    
    @NotBlank(message = "新手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String newPhoneNumber;
    
    @NotBlank(message = "验证码不能为空")
    private String verificationCode;
    
    // Constructors
    public UpdatePhoneRequest() {}
    
    public UpdatePhoneRequest(String newPhoneNumber, String verificationCode) {
        this.newPhoneNumber = newPhoneNumber;
        this.verificationCode = verificationCode;
    }
    
    // Getters and Setters
    public String getNewPhoneNumber() { return newPhoneNumber; }
    public void setNewPhoneNumber(String newPhoneNumber) { this.newPhoneNumber = newPhoneNumber; }
    
    public String getVerificationCode() { return verificationCode; }
    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }
} 