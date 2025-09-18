package com.example.pehd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 器材分类DTO
 */
public class EquipmentCategoryDto {
    
    private String id;
    private String name;
    private String description;
    private String icon;
    private Integer sort;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // 统计信息
    private Integer equipmentCount;      // 该分类下器材种类数量
    private Integer totalQuantity;       // 该分类下器材总数量
    private Integer availableQuantity;   // 该分类下可用数量
    private Integer borrowedQuantity;    // 该分类下借出数量
    
    // 构造函数
    public EquipmentCategoryDto() {}
    
    public EquipmentCategoryDto(String id, String name, String description, String icon, Integer sort) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.sort = sort;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public Integer getSort() {
        return sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Integer getEquipmentCount() {
        return equipmentCount;
    }
    
    public void setEquipmentCount(Integer equipmentCount) {
        this.equipmentCount = equipmentCount;
    }
    
    public Integer getTotalQuantity() {
        return totalQuantity;
    }
    
    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
    
    public Integer getAvailableQuantity() {
        return availableQuantity;
    }
    
    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
    
    public Integer getBorrowedQuantity() {
        return borrowedQuantity;
    }
    
    public void setBorrowedQuantity(Integer borrowedQuantity) {
        this.borrowedQuantity = borrowedQuantity;
    }
}
