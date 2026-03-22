package com.example.pehd.dto;

public class HomeworkScoreStatsDto {
    
    private Integer totalSubmissions;
    private ExerciseStatsDto squat;
    private ExerciseStatsDto sitUp;
    private ExerciseStatsDto pushUp;
    private ExerciseStatsDto pullUp;
    private ExerciseStatsDto jumpRope;
    private ExerciseStatsDto jumpingJack;
    private ExerciseStatsDto highKnees;
    
    // Constructors
    public HomeworkScoreStatsDto() {}
    
    public HomeworkScoreStatsDto(Integer totalSubmissions, ExerciseStatsDto squat, ExerciseStatsDto sitUp,
                                  ExerciseStatsDto pushUp, ExerciseStatsDto pullUp, ExerciseStatsDto jumpRope,
                                  ExerciseStatsDto jumpingJack, ExerciseStatsDto highKnees) {
        this.totalSubmissions = totalSubmissions;
        this.squat = squat;
        this.sitUp = sitUp;
        this.pushUp = pushUp;
        this.pullUp = pullUp;
        this.jumpRope = jumpRope;
        this.jumpingJack = jumpingJack;
        this.highKnees = highKnees;
    }
    
    // Getters and Setters
    public Integer getTotalSubmissions() { return totalSubmissions; }
    public void setTotalSubmissions(Integer totalSubmissions) { this.totalSubmissions = totalSubmissions; }
    
    public ExerciseStatsDto getSquat() { return squat; }
    public void setSquat(ExerciseStatsDto squat) { this.squat = squat; }
    
    public ExerciseStatsDto getSitUp() { return sitUp; }
    public void setSitUp(ExerciseStatsDto sitUp) { this.sitUp = sitUp; }
    
    public ExerciseStatsDto getPushUp() { return pushUp; }
    public void setPushUp(ExerciseStatsDto pushUp) { this.pushUp = pushUp; }
    
    public ExerciseStatsDto getPullUp() { return pullUp; }
    public void setPullUp(ExerciseStatsDto pullUp) { this.pullUp = pullUp; }
    
    public ExerciseStatsDto getJumpRope() { return jumpRope; }
    public void setJumpRope(ExerciseStatsDto jumpRope) { this.jumpRope = jumpRope; }
    
    public ExerciseStatsDto getJumpingJack() { return jumpingJack; }
    public void setJumpingJack(ExerciseStatsDto jumpingJack) { this.jumpingJack = jumpingJack; }
    
    public ExerciseStatsDto getHighKnees() { return highKnees; }
    public void setHighKnees(ExerciseStatsDto highKnees) { this.highKnees = highKnees; }
}

