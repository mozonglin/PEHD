package com.example.pehd.dto;

/**
 * 阳光跑统计DTO
 */
public class SunshineRunStatsDto {
    
    private Integer totalRuns;
    private Double totalDistance;
    private Long totalDuration;
    private Integer totalCalories;
    
    // Constructors
    public SunshineRunStatsDto() {}
    
    public SunshineRunStatsDto(Integer totalRuns, Double totalDistance, Long totalDuration, Integer totalCalories) {
        this.totalRuns = totalRuns;
        this.totalDistance = totalDistance;
        this.totalDuration = totalDuration;
        this.totalCalories = totalCalories;
    }
    
    // Getters and Setters
    public Integer getTotalRuns() { return totalRuns; }
    public void setTotalRuns(Integer totalRuns) { this.totalRuns = totalRuns; }
    
    public Double getTotalDistance() { return totalDistance; }
    public void setTotalDistance(Double totalDistance) { this.totalDistance = totalDistance; }
    
    public Long getTotalDuration() { return totalDuration; }
    public void setTotalDuration(Long totalDuration) { this.totalDuration = totalDuration; }
    
    public Integer getTotalCalories() { return totalCalories; }
    public void setTotalCalories(Integer totalCalories) { this.totalCalories = totalCalories; }
}

