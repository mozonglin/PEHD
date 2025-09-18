package com.example.pehd.entity;

/**
 * 用户角色枚举
 */
public enum UserRole {
    STUDENT("学生"),
    CHECKER("签到员"),
    SUB_CHECKER("二级管理员"),
    ADMIN("管理员");
    
    private final String description;
    
    UserRole(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}