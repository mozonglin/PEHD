package com.example.pehd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class MorningExerciseCheckoutResponse {
    private Integer pointsEarned;
    private Integer totalPoints;
    private String calculationRule;
    private String earnedReason;
    private Integer participationDuration;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime calculatedAt;
    
    // 默认构造函数
    public MorningExerciseCheckoutResponse() {}
    
    // 构造函数
    public MorningExerciseCheckoutResponse(Integer pointsEarned, Integer totalPoints, String calculationRule,
                                         String earnedReason, Integer participationDuration,
                                         LocalDateTime calculatedAt) {
        this.pointsEarned = pointsEarned;
        this.totalPoints = totalPoints;
        this.calculationRule = calculationRule;
        this.earnedReason = earnedReason;
        this.participationDuration = participationDuration;
        this.calculatedAt = calculatedAt;
    }
    
    // Getters and Setters
    public Integer getPointsEarned() {
        return pointsEarned;
    }
    
    public void setPointsEarned(Integer pointsEarned) {
        this.pointsEarned = pointsEarned;
    }
    
    public Integer getTotalPoints() {
        return totalPoints;
    }
    
    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }
    
    public String getCalculationRule() {
        return calculationRule;
    }
    
    public void setCalculationRule(String calculationRule) {
        this.calculationRule = calculationRule;
    }
    
    public String getEarnedReason() {
        return earnedReason;
    }
    
    public void setEarnedReason(String earnedReason) {
        this.earnedReason = earnedReason;
    }
    
    public Integer getParticipationDuration() {
        return participationDuration;
    }
    
    public void setParticipationDuration(Integer participationDuration) {
        this.participationDuration = participationDuration;
    }
    
    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }
    
    public void setCalculatedAt(LocalDateTime calculatedAt) {
        this.calculatedAt = calculatedAt;
    }
}
