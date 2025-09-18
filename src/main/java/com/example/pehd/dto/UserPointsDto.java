package com.example.pehd.dto;

/**
 * 用户积分DTO
 */
public class UserPointsDto {
    
    private Integer points;              // 总积分
    private Integer peActivityPoints;    // 活动积分  
    private Integer morningExercisePoints; // 早操积分
    private Integer integrityScore;      // 诚信度
    
    // Constructors
    public UserPointsDto() {}
    
    public UserPointsDto(Integer points, Integer peActivityPoints, Integer morningExercisePoints, Integer integrityScore) {
        this.points = points;
        this.peActivityPoints = peActivityPoints;
        this.morningExercisePoints = morningExercisePoints;
        this.integrityScore = integrityScore;
    }
    
    // Getters and Setters
    public Integer getPoints() {
        return points;
    }
    
    public void setPoints(Integer points) {
        this.points = points;
    }
    
    public Integer getPeActivityPoints() {
        return peActivityPoints;
    }
    
    public void setPeActivityPoints(Integer peActivityPoints) {
        this.peActivityPoints = peActivityPoints;
    }
    
    public Integer getMorningExercisePoints() {
        return morningExercisePoints;
    }
    
    public void setMorningExercisePoints(Integer morningExercisePoints) {
        this.morningExercisePoints = morningExercisePoints;
    }
    
    public Integer getIntegrityScore() {
        return integrityScore;
    }
    
    public void setIntegrityScore(Integer integrityScore) {
        this.integrityScore = integrityScore;
    }
}