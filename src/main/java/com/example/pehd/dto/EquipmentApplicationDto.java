package com.example.pehd.dto;

import com.example.pehd.entity.EquipmentApplication;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 器材借用申请DTO
 */
public class EquipmentApplicationDto {
    
    private String id;
    private String equipmentId;
    private String equipmentName;
    private String equipmentModel;
    private String categoryName;
    private String borrowerId;
    private String borrowerName;
    private String borrowerStudentId;
    private String borrowerPhone;
    private Integer quantity;
    private String purpose;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime borrowDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedReturnDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualReturnDate;
    
    private EquipmentApplication.ApplicationStatus status;
    private String remark;
    private String approvedBy;
    private String approverName;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;
    
    private Integer actualQuantity;
    private EquipmentApplication.ReturnCondition returnCondition;
    private String returnedBy;
    private String returnerName;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // 构造函数
    public EquipmentApplicationDto() {}
    
    // 状态文本映射
    public String getStatusText() {
        if (status == null) return null;
        switch (status) {
            case pending: return "待审批";
            case approved: return "已批准";
            case rejected: return "已拒绝";
            case returned: return "已归还";
            default: return status.getValue();
        }
    }
    
    // 归还状态文本映射
    public String getReturnConditionText() {
        if (returnCondition == null) return null;
        switch (returnCondition) {
            case good: return "完好";
            case damaged: return "损坏";
            case lost: return "丢失";
            default: return returnCondition.getValue();
        }
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
    
    public String getEquipmentName() {
        return equipmentName;
    }
    
    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }
    
    public String getEquipmentModel() {
        return equipmentModel;
    }
    
    public void setEquipmentModel(String equipmentModel) {
        this.equipmentModel = equipmentModel;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getBorrowerId() {
        return borrowerId;
    }
    
    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }
    
    public String getBorrowerName() {
        return borrowerName;
    }
    
    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }
    
    public String getBorrowerStudentId() {
        return borrowerStudentId;
    }
    
    public void setBorrowerStudentId(String borrowerStudentId) {
        this.borrowerStudentId = borrowerStudentId;
    }
    
    public String getBorrowerPhone() {
        return borrowerPhone;
    }
    
    public void setBorrowerPhone(String borrowerPhone) {
        this.borrowerPhone = borrowerPhone;
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
    
    public EquipmentApplication.ApplicationStatus getStatus() {
        return status;
    }
    
    public void setStatus(EquipmentApplication.ApplicationStatus status) {
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
    
    public String getApproverName() {
        return approverName;
    }
    
    public void setApproverName(String approverName) {
        this.approverName = approverName;
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
    
    public EquipmentApplication.ReturnCondition getReturnCondition() {
        return returnCondition;
    }
    
    public void setReturnCondition(EquipmentApplication.ReturnCondition returnCondition) {
        this.returnCondition = returnCondition;
    }
    
    public String getReturnedBy() {
        return returnedBy;
    }
    
    public void setReturnedBy(String returnedBy) {
        this.returnedBy = returnedBy;
    }
    
    public String getReturnerName() {
        return returnerName;
    }
    
    public void setReturnerName(String returnerName) {
        this.returnerName = returnerName;
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
}
