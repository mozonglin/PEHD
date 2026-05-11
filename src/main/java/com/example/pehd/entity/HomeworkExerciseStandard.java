package com.example.pehd.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "homework_exercise_standards")
public class HomeworkExerciseStandard {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "school", length = 100, nullable = false)
    private String school;

    @Column(name = "exercise_type", length = 20, nullable = false)
    private String exerciseType;

    @Column(name = "male_standard", nullable = false)
    private Integer maleStandard = 0;

    @Column(name = "female_standard", nullable = false)
    private Integer femaleStandard = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public HomeworkExerciseStandard() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }

    public String getExerciseType() { return exerciseType; }
    public void setExerciseType(String exerciseType) { this.exerciseType = exerciseType; }

    public Integer getMaleStandard() { return maleStandard; }
    public void setMaleStandard(Integer maleStandard) { this.maleStandard = maleStandard; }

    public Integer getFemaleStandard() { return femaleStandard; }
    public void setFemaleStandard(Integer femaleStandard) { this.femaleStandard = femaleStandard; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
