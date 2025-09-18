package com.example.pehd.dto;

import jakarta.validation.constraints.NotBlank;

public class ValidateCodeRequest {
    
    @NotBlank(message = "签到码不能为空")
    private String uniqueCode;
    
    // Constructors
    public ValidateCodeRequest() {}
    
    public ValidateCodeRequest(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
    
    // Getters and Setters
    public String getUniqueCode() { return uniqueCode; }
    public void setUniqueCode(String uniqueCode) { this.uniqueCode = uniqueCode; }
} 