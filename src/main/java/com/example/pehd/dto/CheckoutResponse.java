package com.example.pehd.dto;

public class CheckoutResponse {
    
    private String message;
    private Integer pointsEarned;
    private Integer participationDuration;
    private String calculationRule;
    private Integer totalPoints;
    private Integer activityPoints;
    private Integer morningExercisePoints;
    
    // Constructors
    public CheckoutResponse() {}
    
    public CheckoutResponse(String message, Integer pointsEarned, Integer participationDuration, 
                          String calculationRule, Integer totalPoints, Integer activityPoints, 
                          Integer morningExercisePoints) {
        this.message = message;
        this.pointsEarned = pointsEarned;
        this.participationDuration = participationDuration;
        this.calculationRule = calculationRule;
        this.totalPoints = totalPoints;
        this.activityPoints = activityPoints;
        this.morningExercisePoints = morningExercisePoints;
    }
    
    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Integer getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(Integer pointsEarned) { this.pointsEarned = pointsEarned; }
    
    public Integer getParticipationDuration() { return participationDuration; }
    public void setParticipationDuration(Integer participationDuration) { this.participationDuration = participationDuration; }
    
    public String getCalculationRule() { return calculationRule; }
    public void setCalculationRule(String calculationRule) { this.calculationRule = calculationRule; }
    
    public Integer getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }
    
    public Integer getActivityPoints() { return activityPoints; }
    public void setActivityPoints(Integer activityPoints) { this.activityPoints = activityPoints; }
    
    public Integer getMorningExercisePoints() { return morningExercisePoints; }
    public void setMorningExercisePoints(Integer morningExercisePoints) { this.morningExercisePoints = morningExercisePoints; }
}