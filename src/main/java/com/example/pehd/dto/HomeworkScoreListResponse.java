package com.example.pehd.dto;

import java.util.List;

public class HomeworkScoreListResponse {
    
    private Long total;
    private List<HomeworkScoreDto> scores;
    
    // Constructors
    public HomeworkScoreListResponse() {}
    
    public HomeworkScoreListResponse(Long total, List<HomeworkScoreDto> scores) {
        this.total = total;
        this.scores = scores;
    }
    
    // Getters and Setters
    public Long getTotal() { return total; }
    public void setTotal(Long total) { this.total = total; }
    
    public List<HomeworkScoreDto> getScores() { return scores; }
    public void setScores(List<HomeworkScoreDto> scores) { this.scores = scores; }
}

