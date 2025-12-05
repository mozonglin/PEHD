package com.example.pehd.dto;

import java.util.List;

/**
 * 学校操场下拉列表响应DTO
 */
public class PlaygroundDropdownDto {

    private String school;
    private List<PlaygroundInfo> playgrounds;

    public PlaygroundDropdownDto() {}

    public PlaygroundDropdownDto(String school, List<PlaygroundInfo> playgrounds) {
        this.school = school;
        this.playgrounds = playgrounds;
    }

    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }

    public List<PlaygroundInfo> getPlaygrounds() { return playgrounds; }
    public void setPlaygrounds(List<PlaygroundInfo> playgrounds) { this.playgrounds = playgrounds; }

    /**
     * 操场信息内部类
     */
    public static class PlaygroundInfo {
        private String playgroundName;

        public PlaygroundInfo() {}

        public PlaygroundInfo(String playgroundName) {
            this.playgroundName = playgroundName;
        }

        public String getPlaygroundName() { return playgroundName; }
        public void setPlaygroundName(String playgroundName) { this.playgroundName = playgroundName; }
    }
}