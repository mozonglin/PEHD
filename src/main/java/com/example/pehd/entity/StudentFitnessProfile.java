package com.example.pehd.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_fitness_profiles")
public class StudentFitnessProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 50, unique = true)
    private String userId;

    @Column(name = "student_id", nullable = false, length = 30, unique = true)
    private String studentId;

    @Column(name = "real_name", nullable = false, length = 50)
    private String realName;

    @Column(name = "class_name", length = 100)
    private String className;

    @Column(name = "department_name", length = 100)
    private String departmentName;

    @Column(name = "vital_capacity") private Integer vitalCapacity;
    @Column(name = "sit_and_reach", precision = 5, scale = 1) private BigDecimal sitAndReach;
    @Column(name = "standing_long_jump", precision = 5, scale = 1) private BigDecimal standingLongJump;
    @Column(name = "sit_ups") private Integer sitUps;
    @Column(name = "sprint_50m", precision = 5, scale = 2) private BigDecimal sprint50m;
    @Column(name = "long_run") private Integer longRun;
    @Column(name = "pull_ups") private Integer pullUps;
    @Column(precision = 4, scale = 1) private BigDecimal bmi;
    @Column(precision = 5, scale = 1) private BigDecimal height;
    @Column(precision = 5, scale = 1) private BigDecimal weight;
    @Column(name = "tice_total_score", precision = 5, scale = 1) private BigDecimal ticeTotalScore;
    @Column(name = "tice_grade", length = 20) private String ticeGrade;
    @Column(name = "tice_updated_at") private LocalDateTime ticeUpdatedAt;

    @Column(name = "run_total_count") private Integer runTotalCount = 0;
    @Column(name = "run_total_distance") private Double runTotalDistance = 0.0;
    @Column(name = "run_avg_pace") private Double runAvgPace;
    @Column(name = "run_best_pace") private Double runBestPace;
    @Column(name = "run_updated_at") private LocalDateTime runUpdatedAt;

    @Column(name = "homework_total_count") private Integer homeworkTotalCount = 0;
    @Column(name = "homework_squat_best") private Integer homeworkSquatBest;
    @Column(name = "homework_situp_best") private Integer homeworkSitupBest;
    @Column(name = "homework_pushup_best") private Integer homeworkPushupBest;
    @Column(name = "homework_pullup_best") private Integer homeworkPullupBest;
    @Column(name = "homework_jumprope_best") private Integer homeworkJumpropeBest;
    @Column(name = "homework_updated_at") private LocalDateTime homeworkUpdatedAt;

    @Column(name = "pe_total_points") private Integer peTotalPoints = 0;
    @Column(name = "pe_activity_points") private Integer peActivityPoints = 0;
    @Column(name = "pe_morning_points") private Integer peMorningPoints = 0;
    @Column(name = "pe_points_updated_at") private LocalDateTime pePointsUpdatedAt;

    @Column(name = "ai_fitness_level", length = 20) private String aiFitnessLevel;
    @Column(name = "ai_strengths", columnDefinition = "TEXT") private String aiStrengths;
    @Column(name = "ai_weaknesses", columnDefinition = "TEXT") private String aiWeaknesses;
    @Column(name = "ai_recommendation", columnDefinition = "TEXT") private String aiRecommendation;
    @Column(name = "ai_analyzed_at") private LocalDateTime aiAnalyzedAt;

    @Column(name = "created_at", updatable = false) private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); this.updatedAt = LocalDateTime.now(); }
    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public StudentFitnessProfile() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public Integer getVitalCapacity() { return vitalCapacity; }
    public void setVitalCapacity(Integer v) { this.vitalCapacity = v; }
    public BigDecimal getSitAndReach() { return sitAndReach; }
    public void setSitAndReach(BigDecimal v) { this.sitAndReach = v; }
    public BigDecimal getStandingLongJump() { return standingLongJump; }
    public void setStandingLongJump(BigDecimal v) { this.standingLongJump = v; }
    public Integer getSitUps() { return sitUps; }
    public void setSitUps(Integer v) { this.sitUps = v; }
    public BigDecimal getSprint50m() { return sprint50m; }
    public void setSprint50m(BigDecimal v) { this.sprint50m = v; }
    public Integer getLongRun() { return longRun; }
    public void setLongRun(Integer v) { this.longRun = v; }
    public Integer getPullUps() { return pullUps; }
    public void setPullUps(Integer v) { this.pullUps = v; }
    public BigDecimal getBmi() { return bmi; }
    public void setBmi(BigDecimal v) { this.bmi = v; }
    public BigDecimal getHeight() { return height; }
    public void setHeight(BigDecimal v) { this.height = v; }
    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal v) { this.weight = v; }
    public BigDecimal getTiceTotalScore() { return ticeTotalScore; }
    public void setTiceTotalScore(BigDecimal v) { this.ticeTotalScore = v; }
    public String getTiceGrade() { return ticeGrade; }
    public void setTiceGrade(String v) { this.ticeGrade = v; }
    public LocalDateTime getTiceUpdatedAt() { return ticeUpdatedAt; }
    public void setTiceUpdatedAt(LocalDateTime v) { this.ticeUpdatedAt = v; }
    public Integer getRunTotalCount() { return runTotalCount; }
    public void setRunTotalCount(Integer v) { this.runTotalCount = v; }
    public Double getRunTotalDistance() { return runTotalDistance; }
    public void setRunTotalDistance(Double v) { this.runTotalDistance = v; }
    public Double getRunAvgPace() { return runAvgPace; }
    public void setRunAvgPace(Double v) { this.runAvgPace = v; }
    public Double getRunBestPace() { return runBestPace; }
    public void setRunBestPace(Double v) { this.runBestPace = v; }
    public LocalDateTime getRunUpdatedAt() { return runUpdatedAt; }
    public void setRunUpdatedAt(LocalDateTime v) { this.runUpdatedAt = v; }
    public Integer getHomeworkTotalCount() { return homeworkTotalCount; }
    public void setHomeworkTotalCount(Integer v) { this.homeworkTotalCount = v; }
    public Integer getHomeworkSquatBest() { return homeworkSquatBest; }
    public void setHomeworkSquatBest(Integer v) { this.homeworkSquatBest = v; }
    public Integer getHomeworkSitupBest() { return homeworkSitupBest; }
    public void setHomeworkSitupBest(Integer v) { this.homeworkSitupBest = v; }
    public Integer getHomeworkPushupBest() { return homeworkPushupBest; }
    public void setHomeworkPushupBest(Integer v) { this.homeworkPushupBest = v; }
    public Integer getHomeworkPullupBest() { return homeworkPullupBest; }
    public void setHomeworkPullupBest(Integer v) { this.homeworkPullupBest = v; }
    public Integer getHomeworkJumpropeBest() { return homeworkJumpropeBest; }
    public void setHomeworkJumpropeBest(Integer v) { this.homeworkJumpropeBest = v; }
    public LocalDateTime getHomeworkUpdatedAt() { return homeworkUpdatedAt; }
    public void setHomeworkUpdatedAt(LocalDateTime v) { this.homeworkUpdatedAt = v; }
    public Integer getPeTotalPoints() { return peTotalPoints; }
    public void setPeTotalPoints(Integer v) { this.peTotalPoints = v; }
    public Integer getPeActivityPoints() { return peActivityPoints; }
    public void setPeActivityPoints(Integer v) { this.peActivityPoints = v; }
    public Integer getPeMorningPoints() { return peMorningPoints; }
    public void setPeMorningPoints(Integer v) { this.peMorningPoints = v; }
    public LocalDateTime getPePointsUpdatedAt() { return pePointsUpdatedAt; }
    public void setPePointsUpdatedAt(LocalDateTime v) { this.pePointsUpdatedAt = v; }
    public String getAiFitnessLevel() { return aiFitnessLevel; }
    public void setAiFitnessLevel(String v) { this.aiFitnessLevel = v; }
    public String getAiStrengths() { return aiStrengths; }
    public void setAiStrengths(String v) { this.aiStrengths = v; }
    public String getAiWeaknesses() { return aiWeaknesses; }
    public void setAiWeaknesses(String v) { this.aiWeaknesses = v; }
    public String getAiRecommendation() { return aiRecommendation; }
    public void setAiRecommendation(String v) { this.aiRecommendation = v; }
    public LocalDateTime getAiAnalyzedAt() { return aiAnalyzedAt; }
    public void setAiAnalyzedAt(LocalDateTime v) { this.aiAnalyzedAt = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
