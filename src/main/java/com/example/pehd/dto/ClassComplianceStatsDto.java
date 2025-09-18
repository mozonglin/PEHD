package com.example.pehd.dto;

/**
 * 班级达标率统计DTO
 */
public class ClassComplianceStatsDto {
    
    private String className;
    private Integer totalStudents;
    private Integer weeklyCompliantStudents;
    private Integer monthlyCompliantStudents;
    private Integer totalCompliantStudents;
    private Double weeklyComplianceRate;
    private Double monthlyComplianceRate;
    private Double totalComplianceRate;
    
    // 管理员设置的目标
    private Integer weeklyTarget;
    private Integer monthlyTarget;
    private Integer totalTarget;
    
    // 构造函数
    public ClassComplianceStatsDto() {}
    
    public ClassComplianceStatsDto(String className, Integer totalStudents,
                                 Integer weeklyCompliantStudents, Integer monthlyCompliantStudents,
                                 Integer totalCompliantStudents, Integer weeklyTarget,
                                 Integer monthlyTarget, Integer totalTarget) {
        this.className = className;
        this.totalStudents = totalStudents;
        this.weeklyCompliantStudents = weeklyCompliantStudents;
        this.monthlyCompliantStudents = monthlyCompliantStudents;
        this.totalCompliantStudents = totalCompliantStudents;
        this.weeklyTarget = weeklyTarget;
        this.monthlyTarget = monthlyTarget;
        this.totalTarget = totalTarget;
        
        // 计算达标率
        if (totalStudents > 0) {
            this.weeklyComplianceRate = (weeklyCompliantStudents * 100.0) / totalStudents;
            this.monthlyComplianceRate = (monthlyCompliantStudents * 100.0) / totalStudents;
            this.totalComplianceRate = (totalCompliantStudents * 100.0) / totalStudents;
        } else {
            this.weeklyComplianceRate = 0.0;
            this.monthlyComplianceRate = 0.0;
            this.totalComplianceRate = 0.0;
        }
    }
    
    // Getters and Setters
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    
    public Integer getTotalStudents() { return totalStudents; }
    public void setTotalStudents(Integer totalStudents) { this.totalStudents = totalStudents; }
    
    public Integer getWeeklyCompliantStudents() { return weeklyCompliantStudents; }
    public void setWeeklyCompliantStudents(Integer weeklyCompliantStudents) { this.weeklyCompliantStudents = weeklyCompliantStudents; }
    
    public Integer getMonthlyCompliantStudents() { return monthlyCompliantStudents; }
    public void setMonthlyCompliantStudents(Integer monthlyCompliantStudents) { this.monthlyCompliantStudents = monthlyCompliantStudents; }
    
    public Integer getTotalCompliantStudents() { return totalCompliantStudents; }
    public void setTotalCompliantStudents(Integer totalCompliantStudents) { this.totalCompliantStudents = totalCompliantStudents; }
    
    public Double getWeeklyComplianceRate() { return weeklyComplianceRate; }
    public void setWeeklyComplianceRate(Double weeklyComplianceRate) { this.weeklyComplianceRate = weeklyComplianceRate; }
    
    public Double getMonthlyComplianceRate() { return monthlyComplianceRate; }
    public void setMonthlyComplianceRate(Double monthlyComplianceRate) { this.monthlyComplianceRate = monthlyComplianceRate; }
    
    public Double getTotalComplianceRate() { return totalComplianceRate; }
    public void setTotalComplianceRate(Double totalComplianceRate) { this.totalComplianceRate = totalComplianceRate; }
    
    public Integer getWeeklyTarget() { return weeklyTarget; }
    public void setWeeklyTarget(Integer weeklyTarget) { this.weeklyTarget = weeklyTarget; }
    
    public Integer getMonthlyTarget() { return monthlyTarget; }
    public void setMonthlyTarget(Integer monthlyTarget) { this.monthlyTarget = monthlyTarget; }
    
    public Integer getTotalTarget() { return totalTarget; }
    public void setTotalTarget(Integer totalTarget) { this.totalTarget = totalTarget; }
}
