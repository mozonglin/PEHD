package com.example.pehd.dto;

import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 创建器材借用申请请求DTO
 */
public class CreateEquipmentApplicationRequest {
    
    @NotBlank(message = "器材ID不能为空")
    private String equipmentId;
    
    @NotNull(message = "借用数量不能为空")
    @Min(value = 1, message = "借用数量必须大于0")
    @Max(value = 100, message = "借用数量不能超过100")
    private Integer quantity;
    
    @NotBlank(message = "借用目的不能为空")
    @Size(max = 200, message = "借用目的长度不能超过200个字符")
    private String purpose;
    
    @NotNull(message = "借用开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime borrowDate;
    
    @NotNull(message = "预期归还时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedReturnDate;
    
    @Size(max = 200, message = "备注长度不能超过200个字符")
    private String remark;
    
    // 构造函数
    public CreateEquipmentApplicationRequest() {}
    
    public CreateEquipmentApplicationRequest(String equipmentId, Integer quantity, String purpose, 
                                           LocalDateTime borrowDate, LocalDateTime expectedReturnDate) {
        this.equipmentId = equipmentId;
        this.quantity = quantity;
        this.purpose = purpose;
        this.borrowDate = borrowDate;
        this.expectedReturnDate = expectedReturnDate;
    }
    
    // 自定义验证方法
    @AssertTrue(message = "预期归还时间必须在借用开始时间之后")
    public boolean isValidDateRange() {
        if (borrowDate == null || expectedReturnDate == null) {
            return true; // 让@NotNull注解处理null值验证
        }
        return expectedReturnDate.isAfter(borrowDate);
    }
    
    @AssertTrue(message = "借用开始时间不能早于当前时间")
    public boolean isValidBorrowDate() {
        if (borrowDate == null) {
            return true; // 让@NotNull注解处理null值验证
        }
        return borrowDate.isAfter(LocalDateTime.now().minusMinutes(5)); // 允许5分钟的时间误差
    }
    
    // Getters and Setters
    public String getEquipmentId() {
        return equipmentId;
    }
    
    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
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
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
