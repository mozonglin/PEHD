package com.example.pehd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 器材借用申请实体类
 */
@Entity
@Table(name = "equipment_applications")
public class EquipmentApplication {
    
    @Id
    @Column(length = 36)
    private String id;
    
    @NotBlank(message = "器材ID不能为空")
    @Column(name = "equipment_id", nullable = false, length = 36)
    private String equipmentId;
    
    @NotBlank(message = "借用人ID不能为空")
    @Column(name = "borrower_id", nullable = false, length = 36)
    private String borrowerId;
    
    @NotNull(message = "借用数量不能为空")
    @Min(value = 1, message = "借用数量必须大于0")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @NotBlank(message = "借用目的不能为空")
    @Size(max = 200, message = "借用目的长度不能超过200个字符")
    @Column(name = "purpose", nullable = false, length = 200)
    private String purpose;
    
    @NotNull(message = "借用开始时间不能为空")
    @Column(name = "borrow_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime borrowDate;
    
    @NotNull(message = "预期归还时间不能为空")
    @Column(name = "expected_return_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedReturnDate;
    
    @Column(name = "actual_return_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualReturnDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status = ApplicationStatus.pending;
    
    @Size(max = 200, message = "备注长度不能超过200个字符")
    @Column(name = "remark", length = 200)
    private String remark;
    
    @Column(name = "approved_by", length = 36)
    private String approvedBy;
    
    @Column(name = "approved_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;
    
    @Column(name = "actual_quantity")
    private Integer actualQuantity;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "return_condition")
    private ReturnCondition returnCondition;
    
    @Column(name = "returned_by", length = 36)
    private String returnedBy;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // 关联器材
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", insertable = false, updatable = false)
    private EquipmentItem equipment;
    
    // 申请状态枚举
    public enum ApplicationStatus {
        pending("pending"),
        approved("approved"),
        rejected("rejected"),
        returned("returned");
        
        private final String value;
        
        ApplicationStatus(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
    
    // 归还状态枚举
    public enum ReturnCondition {
        good("good"),
        damaged("damaged"),
        lost("lost");
        
        private final String value;
        
        ReturnCondition(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
    
    // 构造函数
    public EquipmentApplication() {}
    
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
    
    public String getEquipmentId() {
        return equipmentId;
    }
    
    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }
    
    public String getBorrowerId() {
        return borrowerId;
    }
    
    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public String getPurpose() {
        return purpose;
    }
    
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    
    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }
    
    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }
    
    public LocalDateTime getExpectedReturnDate() {
        return expectedReturnDate;
    }
    
    public void setExpectedReturnDate(LocalDateTime expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }
    
    public LocalDateTime getActualReturnDate() {
        return actualReturnDate;
    }
    
    public void setActualReturnDate(LocalDateTime actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }
    
    public ApplicationStatus getStatus() {
        return status;
    }
    
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public String getApprovedBy() {
        return approvedBy;
    }
    
    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
    
    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }
    
    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }
    
    public Integer getActualQuantity() {
        return actualQuantity;
    }
    
    public void setActualQuantity(Integer actualQuantity) {
        this.actualQuantity = actualQuantity;
    }
    
    public ReturnCondition getReturnCondition() {
        return returnCondition;
    }
    
    public void setReturnCondition(ReturnCondition returnCondition) {
        this.returnCondition = returnCondition;
    }
    
    public String getReturnedBy() {
        return returnedBy;
    }
    
    public void setReturnedBy(String returnedBy) {
        this.returnedBy = returnedBy;
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
    
    public EquipmentItem getEquipment() {
        return equipment;
    }
    
    public void setEquipment(EquipmentItem equipment) {
        this.equipment = equipment;
    }
}
