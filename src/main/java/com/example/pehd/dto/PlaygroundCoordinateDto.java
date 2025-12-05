package com.example.pehd.dto;

/**
 * 操场坐标响应DTO
 */
public class PlaygroundCoordinateDto {

    private String school;
    private String playgroundName;
    private Double longitude1;
    private Double latitude1;
    private Double longitude2;
    private Double latitude2;

    public PlaygroundCoordinateDto() {}

    public PlaygroundCoordinateDto(String school, String playgroundName, Double longitude1, Double latitude1, Double longitude2, Double latitude2) {
        this.school = school;
        this.playgroundName = playgroundName;
        this.longitude1 = longitude1;
        this.latitude1 = latitude1;
        this.longitude2 = longitude2;
        this.latitude2 = latitude2;
    }

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
}