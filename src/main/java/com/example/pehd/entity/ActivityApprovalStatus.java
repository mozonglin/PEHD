package com.example.pehd.entity;

/**
 * 活动审核状态枚举
 */
public enum ActivityApprovalStatus {
    PENDING("PENDING", "审核中"),
    APPROVED("APPROVED", "已审核通过"),
    REJECTED("REJECTED", "已拒绝"),
    DRAFT("DRAFT", "草稿");

    private final String code;
    private final String description;

    ActivityApprovalStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
} 