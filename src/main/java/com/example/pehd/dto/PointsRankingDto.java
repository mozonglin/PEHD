package com.example.pehd.dto;

/**
 * 积分排名DTO
 */
public class PointsRankingDto {
    
    private Integer rank;
    private String studentId;
    private String studentName;
    private String className;
    private Integer totalPoints;
    private Integer peActivityPoints;
    private Integer morningExercisePoints;
    
    // 构造函数
    public PointsRankingDto() {}
    
    public PointsRankingDto(Integer rank, String studentId, String studentName, String className,
                           Integer totalPoints, Integer peActivityPoints, Integer morningExercisePoints) {
        this.rank = rank;
        this.studentId = studentId;
        this.studentName = studentName;
        this.className = className;
        this.totalPoints = totalPoints;
        this.peActivityPoints = peActivityPoints;
        this.morningExercisePoints = morningExercisePoints;
    }
    
    // Getters and Setters
    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }
    
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
}
