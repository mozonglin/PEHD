package com.example.pehd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 器材库存实体类
 */
@Entity
@Table(name = "equipment_items")
public class EquipmentItem {
    
    @Id
    @Column(length = 36)
    private String id;
    
    @NotBlank(message = "分类ID不能为空")
    @Column(name = "category_id", nullable = false, length = 36)
    private String categoryId;
    
    @NotBlank(message = "器材名称不能为空")
    @Size(max = 50, message = "器材名称长度不能超过50个字符")
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    
    @Size(max = 30, message = "型号长度不能超过30个字符")
    @Column(name = "model", length = 30)
    private String model;
    
    @Size(max = 200, message = "规格描述长度不能超过200个字符")
    @Column(name = "specification", length = 200)
    private String specification;
    
    @NotNull(message = "总数量不能为空")
    @Min(value = 0, message = "总数量不能为负数")
    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;
    
    @NotNull(message = "可用数量不能为空")
    @Min(value = 0, message = "可用数量不能为负数")
    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;
    
    @Column(name = "borrowed_quantity", nullable = false)
    private Integer borrowedQuantity = 0;
    
    @Column(name = "damaged_quantity", nullable = false)
    private Integer damagedQuantity = 0;
    
    @DecimalMin(value = "0.0", message = "单价不能为负数")
    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(name = "purchase_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;
    
    @Column(name = "warranty_period")
    private Integer warrantyPeriod;
    
    @Size(max = 50, message = "存放位置长度不能超过50个字符")
    @Column(name = "storage_location", length = 50)
    private String storageLocation;

    @Column(name = "school", length = 100)
    private String school;
    
    @Column(name = "created_by", length = 36)
    private String createdBy;
    
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // 关联分类
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private EquipmentCategory category;
    
    // 构造函数
    public EquipmentItem() {}
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getSpecification() {
        return specification;
    }
    
    public void setSpecification(String specification) {
        this.specification = specification;
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
    
    public Integer getDamagedQuantity() {
        return damagedQuantity;
    }
    
    public void setDamagedQuantity(Integer damagedQuantity) {
        this.damagedQuantity = damagedQuantity;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }
    
    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
    
    public Integer getWarrantyPeriod() {
        return warrantyPeriod;
    }
    
    public void setWarrantyPeriod(Integer warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }
    
    public String getStorageLocation() {
        return storageLocation;
    }
    
    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public Boolean getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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
    
    public EquipmentCategory getCategory() {
        return category;
    }
    
    public void setCategory(EquipmentCategory category) {
        this.category = category;
    }
}
