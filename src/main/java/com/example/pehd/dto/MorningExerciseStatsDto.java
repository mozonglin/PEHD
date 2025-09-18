package com.example.pehd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class MorningExerciseStatsDto {
    private String exerciseId;
    private String exerciseName;
    private Integer totalParticipants;
    private Integer checkedInCount;
    private Integer checkedOutCount;
    private Double completionRate;
    private Double onTimeRate;
    private Integer totalPointsAwarded;
    private Integer averageParticipationTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime statsGeneratedAt;
    
    // 默认构造函数
    public MorningExerciseStatsDto() {}
    
    // 构造函数
    public MorningExerciseStatsDto(String exerciseId, String exerciseName, Integer totalParticipants,
                                 Integer checkedInCount, Integer checkedOutCount, Double completionRate,
                                 Double onTimeRate, Integer totalPointsAwarded, Integer averageParticipationTime,
                                 LocalDateTime statsGeneratedAt) {
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.totalParticipants = totalParticipants;
        this.checkedInCount = checkedInCount;
        this.checkedOutCount = checkedOutCount;
        this.completionRate = completionRate;
        this.onTimeRate = onTimeRate;
        this.totalPointsAwarded = totalPointsAwarded;
        this.averageParticipationTime = averageParticipationTime;
        this.statsGeneratedAt = statsGeneratedAt;
    }
    
    // Getters and Setters
    public String getExerciseId() {
        return exerciseId;
    }
    
    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }
    
    public String getExerciseName() {
        return exerciseName;
    }
    
    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }
    
    public Integer getTotalParticipants() {
        return totalParticipants;
    }
    
    public void setTotalParticipants(Integer totalParticipants) {
        this.totalParticipants = totalParticipants;
    }
    
    public Integer getCheckedInCount() {
        return checkedInCount;
    }
    
    public void setCheckedInCount(Integer checkedInCount) {
        this.checkedInCount = checkedInCount;
    }
    
    public Integer getCheckedOutCount() {
        return checkedOutCount;
    }
    
    public void setCheckedOutCount(Integer checkedOutCount) {
        this.checkedOutCount = checkedOutCount;
    }
    
    public Double getCompletionRate() {
        return completionRate;
    }
    
    public void setCompletionRate(Double completionRate) {
        this.completionRate = completionRate;
    }
    
    public Double getOnTimeRate() {
        return onTimeRate;
    }
    
    public void setOnTimeRate(Double onTimeRate) {
        this.onTimeRate = onTimeRate;
    }
    
    public Integer getTotalPointsAwarded() {
        return totalPointsAwarded;
    }
    
    public void setTotalPointsAwarded(Integer totalPointsAwarded) {
        this.totalPointsAwarded = totalPointsAwarded;
    }
    
    public Integer getAverageParticipationTime() {
        return averageParticipationTime;
    }
    
    public void setAverageParticipationTime(Integer averageParticipationTime) {
        this.averageParticipationTime = averageParticipationTime;
    }
    
    public LocalDateTime getStatsGeneratedAt() {
        return statsGeneratedAt;
    }
    
    public void setStatsGeneratedAt(LocalDateTime statsGeneratedAt) {
        this.statsGeneratedAt = statsGeneratedAt;
    }
}
