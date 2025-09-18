package com.example.pehd.dto;

/**
 * 学生个人达标情况DTO
 */
public class StudentComplianceDto {
    
    private String studentId;
    private String studentName;
    private String className;
    private Integer totalPoints;
    private Integer peActivityPoints;
    private Integer morningExercisePoints;
    
    // 管理员设置的目标
    private Integer weeklyTarget;
    private Integer monthlyTarget;
    private Integer totalTarget;
    
    // 达标率（百分比）
    private Double weeklyComplianceRate;
    private Double monthlyComplianceRate;
    private Double totalComplianceRate;
    
    // 平均积分
    private Double averageWeeklyPoints;
    private Double averageMonthlyPoints;
    
    // 构造函数
    public StudentComplianceDto() {}
    
    public StudentComplianceDto(String studentId, String studentName, String className,
                              Integer totalPoints, Integer peActivityPoints, Integer morningExercisePoints,
                              Integer weeklyTarget, Integer monthlyTarget, Integer totalTarget,
                              Double averageWeeklyPoints, Double averageMonthlyPoints) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.className = className;
        this.totalPoints = totalPoints;
        this.peActivityPoints = peActivityPoints;
        this.morningExercisePoints = morningExercisePoints;
        this.weeklyTarget = weeklyTarget;
        this.monthlyTarget = monthlyTarget;
        this.totalTarget = totalTarget;
        this.averageWeeklyPoints = averageWeeklyPoints;
        this.averageMonthlyPoints = averageMonthlyPoints;
        
        // 计算达标率（平均积分 / 目标积分 * 100）
        this.weeklyComplianceRate = (weeklyTarget > 0) ? (averageWeeklyPoints / weeklyTarget * 100.0) : 0.0;
        this.monthlyComplianceRate = (monthlyTarget > 0) ? (averageMonthlyPoints / monthlyTarget * 100.0) : 0.0;
        this.totalComplianceRate = (totalTarget > 0) ? (totalPoints.doubleValue() / totalTarget * 100.0) : 0.0;
        
        // 限制达标率最大为100%
        this.weeklyComplianceRate = Math.min(this.weeklyComplianceRate, 100.0);
        this.monthlyComplianceRate = Math.min(this.monthlyComplianceRate, 100.0);
        this.totalComplianceRate = Math.min(this.totalComplianceRate, 100.0);
    }
    
    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    
    public Integer getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }
    
    public Integer getPeActivityPoints() { return peActivityPoints; }
    public void setPeActivityPoints(Integer peActivityPoints) { this.peActivityPoints = peActivityPoints; }
    
    public Integer getMorningExercisePoints() { return morningExercisePoints; }
    public void setMorningExercisePoints(Integer morningExercisePoints) { this.morningExercisePoints = morningExercisePoints; }
    
    public Integer getWeeklyTarget() { return weeklyTarget; }
    public void setWeeklyTarget(Integer weeklyTarget) { this.weeklyTarget = weeklyTarget; }
    
    public Integer getMonthlyTarget() { return monthlyTarget; }
    public void setMonthlyTarget(Integer monthlyTarget) { this.monthlyTarget = monthlyTarget; }
    
    public Integer getTotalTarget() { return totalTarget; }
    public void setTotalTarget(Integer totalTarget) { this.totalTarget = totalTarget; }
    
    public Double getWeeklyComplianceRate() { return weeklyComplianceRate; }
    public void setWeeklyComplianceRate(Double weeklyComplianceRate) { this.weeklyComplianceRate = weeklyComplianceRate; }
    
    public Double getMonthlyComplianceRate() { return monthlyComplianceRate; }
    public void setMonthlyComplianceRate(Double monthlyComplianceRate) { this.monthlyComplianceRate = monthlyComplianceRate; }
    
    public Double getTotalComplianceRate() { return totalComplianceRate; }
    public void setTotalComplianceRate(Double totalComplianceRate) { this.totalComplianceRate = totalComplianceRate; }
    
    public Double getAverageWeeklyPoints() { return averageWeeklyPoints; }
    public void setAverageWeeklyPoints(Double averageWeeklyPoints) { this.averageWeeklyPoints = averageWeeklyPoints; }
    
    public Double getAverageMonthlyPoints() { return averageMonthlyPoints; }
    public void setAverageMonthlyPoints(Double averageMonthlyPoints) { this.averageMonthlyPoints = averageMonthlyPoints; }
}
