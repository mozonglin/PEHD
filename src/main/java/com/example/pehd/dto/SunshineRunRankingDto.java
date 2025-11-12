package com.example.pehd.dto;

/**
 * 阳光跑排名DTO
 */
public class SunshineRunRankingDto {
    
    private String userName;
    private String studentId;
    private Double totalDistance;
    private Integer totalRuns;
    private Long totalDuration;
    private Integer rank;
    
    // Constructors
    public SunshineRunRankingDto() {}
    
    public SunshineRunRankingDto(String userName, String studentId, Double totalDistance, 
                                Integer totalRuns, Long totalDuration, Integer rank) {
        this.userName = userName;
        this.studentId = studentId;
        this.totalDistance = totalDistance;
        this.totalRuns = totalRuns;
        this.totalDuration = totalDuration;
        this.rank = rank;
    }
    
    // Getters and Setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public Double getTotalDistance() { return totalDistance; }
    public void setTotalDistance(Double totalDistance) { this.totalDistance = totalDistance; }
    
    public Integer getTotalRuns() { return totalRuns; }
    public void setTotalRuns(Integer totalRuns) { this.totalRuns = totalRuns; }
    
    public Long getTotalDuration() { return totalDuration; }
    public void setTotalDuration(Long totalDuration) { this.totalDuration = totalDuration; }
    
    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }
}

