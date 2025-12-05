package com.example.pehd.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 操场坐标实体
 */
@Entity
@Table(name = "playground_coordinates")
public class PlaygroundCoordinate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "school", nullable = false, length = 100)
    private String school;

    @Column(name = "playground_name", nullable = false, length = 100)
    private String playgroundName;

    @Column(name = "longitude1", nullable = false)
    private Double longitude1;

    @Column(name = "latitude1", nullable = false)
    private Double latitude1;

    @Column(name = "longitude2", nullable = false)
    private Double longitude2;

    @Column(name = "latitude2", nullable = false)
    private Double latitude2;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Constructors
    public PlaygroundCoordinate() {}

    public PlaygroundCoordinate(String school, String playgroundName, Double longitude1, Double latitude1, Double longitude2, Double latitude2) {
        this.school = school;
        this.playgroundName = playgroundName;
        this.longitude1 = longitude1;
        this.latitude1 = latitude1;
        this.longitude2 = longitude2;
        this.latitude2 = latitude2;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }

    public String getPlaygroundName() { return playgroundName; }
    public void setPlaygroundName(String playgroundName) { this.playgroundName = playgroundName; }

    public Double getLongitude1() { return longitude1; }
    public void setLongitude1(Double longitude1) { this.longitude1 = longitude1; }

    public Double getLatitude1() { return latitude1; }
    public void setLatitude1(Double latitude1) { this.latitude1 = latitude1; }

    public Double getLongitude2() { return longitude2; }
    public void setLongitude2(Double longitude2) { this.longitude2 = longitude2; }

    public Double getLatitude2() { return latitude2; }
    public void setLatitude2(Double latitude2) { this.latitude2 = latitude2; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}