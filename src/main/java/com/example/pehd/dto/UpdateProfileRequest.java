package com.example.pehd.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateProfileRequest {
    
    @NotBlank(message = "姓名不能为空")
    private String name;
    
    private String school;
    private String college;
    private String avatar;
    
    // Constructors
    public UpdateProfileRequest() {}
    
    public UpdateProfileRequest(String name, String school, String college, String avatar) {
        this.name = name;
        this.school = school;
        this.college = college;
        this.avatar = avatar;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }
    
    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }
    
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
} 