package com.example.pehd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

public class PointExchangeRequest {
    
    @NotBlank(message = "兑换类型不能为空")
    private String type;
    
    @Min(value = 1, message = "兑换积分必须大于0")
    private Integer points;
    
    // Constructors
    public PointExchangeRequest() {}
    
    public PointExchangeRequest(String type, Integer points) {
        this.type = type;
        this.points = points;
    }
    
    // Getters and Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
} 