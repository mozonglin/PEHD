package com.example.pehd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExerciseStatsDto {
    
    private Integer count;
    private Integer bestCount;
    private Integer lastCount;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastTime;
    
    // Constructors
    public ExerciseStatsDto() {}
    
    public ExerciseStatsDto(Integer count, Integer bestCount, Integer lastCount, LocalDateTime lastTime) {
        this.count = count;
        this.bestCount = bestCount;
        this.lastCount = lastCount;
        this.lastTime = lastTime;
    }
    
    // Getters and Setters
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
    
    public Integer getBestCount() { return bestCount; }
    public void setBestCount(Integer bestCount) { this.bestCount = bestCount; }
    
    public Integer getLastCount() { return lastCount; }
    public void setLastCount(Integer lastCount) { this.lastCount = lastCount; }
    
    public LocalDateTime getLastTime() { return lastTime; }
    public void setLastTime(LocalDateTime lastTime) { this.lastTime = lastTime; }
}

