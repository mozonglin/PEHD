package com.example.pehd.service;

import com.example.pehd.dto.*;
import com.example.pehd.entity.*;
import com.example.pehd.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 器材服务类
 */
@Service
@Transactional
public class EquipmentService {
    
    @Autowired
    private EquipmentCategoryRepository categoryRepository;
    
    @Autowired
    private EquipmentItemRepository itemRepository;
    
    @Autowired
    private EquipmentApplicationRepository applicationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 获取所有器材分类（包含统计信息）
     */
    @Transactional(readOnly = true)
    public List<EquipmentCategoryDto> getAllCategories() {
        List<Object[]> statistics = categoryRepository.findCategoryStatistics();
        
        return statistics.stream().map(stat -> {
            EquipmentCategoryDto dto = new EquipmentCategoryDto();
            dto.setId((String) stat[0]);
            dto.setName((String) stat[1]);
            dto.setDescription((String) stat[2]);
            dto.setIcon((String) stat[3]);
            dto.setSort((Integer) stat[4]);
            dto.setCreatedAt((LocalDateTime) stat[5]);
            dto.setUpdatedAt((LocalDateTime) stat[6]);
            dto.setEquipmentCount(((Number) stat[7]).intValue());
            dto.setTotalQuantity(((Number) stat[8]).intValue());
            dto.setAvailableQuantity(((Number) stat[9]).intValue());
            dto.setBorrowedQuantity(((Number) stat[10]).intValue());
            return dto;
        }).collect(Collectors.toList());
    }
    
    private String getUserSchool(String studentId) {
        if (studentId == null) return null;
        return userRepository.findByStudentId(studentId).map(u -> u.getSchool()).orElse(null);
    }

    /**
     * 根据分类ID获取器材列表（按学校隔离）
     */
    @Transactional(readOnly = true)
    public Page<EquipmentItemDto> getEquipmentsByCategory(String categoryId, Pageable pageable, String userId) {
        String school = getUserSchool(userId);
        Page<EquipmentItem> items;
        if (school != null) {
            items = itemRepository.findBySchoolAndCategoryIdAndIsDeletedFalse(school, categoryId, pageable);
        } else {
            items = itemRepository.findByCategoryIdAndIsDeletedFalse(categoryId, pageable);
        }
        List<EquipmentItemDto> dtos = items.getContent().stream()
            .map(this::convertToEquipmentItemDto)
            .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, items.getTotalElements());
    }
    
    /**
     * 搜索器材（按学校隔离）
     */
    @Transactional(readOnly = true)
    public Page<EquipmentItemDto> searchEquipments(String categoryId, String keyword, Pageable pageable, String userId) {
        String school = getUserSchool(userId);
        Page<EquipmentItem> items;
        if (school != null) {
            if (categoryId != null && !categoryId.isEmpty()) {
                if (keyword != null && !keyword.isEmpty()) {
                    items = itemRepository.findBySchoolAndCategoryIdAndNameContainingAndIsDeletedFalse(school, categoryId, keyword, pageable);
                } else {
                    items = itemRepository.findBySchoolAndCategoryIdAndIsDeletedFalse(school, categoryId, pageable);
                }
            } else {
                if (keyword != null && !keyword.isEmpty()) {
                    items = itemRepository.findBySchoolAndNameContainingAndIsDeletedFalse(school, keyword, pageable);
                } else {
                    items = itemRepository.findBySchoolAndIsDeletedFalse(school, pageable);
                }
            }
        } else {
            if (categoryId != null && !categoryId.isEmpty()) {
                if (keyword != null && !keyword.isEmpty()) {
                    items = itemRepository.findByCategoryIdAndNameContainingAndIsDeletedFalse(categoryId, keyword, pageable);
                } else {
                    items = itemRepository.findByCategoryIdAndIsDeletedFalse(categoryId, pageable);
                }
            } else {
                if (keyword != null && !keyword.isEmpty()) {
                    items = itemRepository.findByNameContainingAndIsDeletedFalse(keyword, pageable);
                } else {
                    items = itemRepository.findByIsDeletedFalse(pageable);
                }
            }
        }
        List<EquipmentItemDto> dtos = items.getContent().stream()
            .map(this::convertToEquipmentItemDto)
            .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, items.getTotalElements());
    }
    
    /**
     * 根据ID获取器材详情
     */
    @Transactional(readOnly = true)
    public Optional<EquipmentItemDto> getEquipmentById(String equipmentId) {
        // 使用简单方式，直接从实体获取数据
        Optional<EquipmentItem> equipmentOpt = itemRepository.findByIdAndIsDeletedFalse(equipmentId);
        
        if (equipmentOpt.isPresent()) {
            EquipmentItem equipment = equipmentOpt.get();
            EquipmentItemDto dto = convertToEquipmentItemDto(equipment);
            
            // 获取分类名称
            Optional<EquipmentCategory> categoryOpt = categoryRepository.findById(equipment.getCategoryId());
            if (categoryOpt.isPresent()) {
                dto.setCategoryName(categoryOpt.get().getName());
            }
            
            // 获取借用统计
            Long borrowCount = applicationRepository.countByEquipmentIdAndStatusApproved(equipmentId);
            dto.setBorrowCount(borrowCount.intValue());
            
            return Optional.of(dto);
        }
        
        return Optional.empty();
    }
    
    /**
     * 获取热门器材排行
     */
    @Transactional(readOnly = true)
    public Page<EquipmentItemDto> getPopularEquipments(Pageable pageable) {
        Page<Object[]> results = itemRepository.findPopularEquipment(pageable);
        
        List<EquipmentItemDto> dtos = results.getContent().stream().map(data -> {
            EquipmentItemDto dto = new EquipmentItemDto();
            dto.setId((String) data[0]);
            dto.setName((String) data[1]);
            dto.setModel((String) data[2]);
            dto.setCategoryName((String) data[3]);
            dto.setAvailableQuantity((Integer) data[4]);
            dto.setTotalQuantity((Integer) data[5]);
            dto.setBorrowCount(((Number) data[6]).intValue());
            dto.setTotalBorrowedQty(((Number) data[7]).intValue());
            return dto;
        }).collect(Collectors.toList());
        
        return new PageImpl<>(dtos, pageable, results.getTotalElements());
    }
    
    /**
     * 检查器材是否可借用
     */
    @Transactional(readOnly = true)
    public boolean isEquipmentAvailable(String equipmentId, Integer requestQuantity) {
        Optional<EquipmentItem> equipment = itemRepository.findByIdAndIsDeletedFalse(equipmentId);
        if (equipment.isEmpty()) {
            return false;
        }
        
        return equipment.get().getAvailableQuantity() >= requestQuantity;
    }
    
    /**
     * 提交借用申请
     */
    public String createApplication(CreateEquipmentApplicationRequest request, String borrowerId) {
        // 验证器材是否存在且可用
        Optional<EquipmentItem> equipment = itemRepository.findByIdAndIsDeletedFalse(request.getEquipmentId());
        if (equipment.isEmpty()) {
            throw new RuntimeException("器材不存在或已删除");
        }
        
        EquipmentItem item = equipment.get();
        if (item.getAvailableQuantity() < request.getQuantity()) {
            throw new RuntimeException("库存不足，当前可用数量：" + item.getAvailableQuantity());
        }
        
        // 检查用户是否已有该器材的未归还借用
        boolean hasUnreturned = applicationRepository.existsUnreturnedApplicationByBorrowerAndEquipment(
            borrowerId, request.getEquipmentId());
        if (hasUnreturned) {
            throw new RuntimeException("您还有该器材的未归还借用，请先归还后再申请");
        }
        
        // 验证用户是否存在

        
        // 创建申请
        EquipmentApplication application = new EquipmentApplication();
        application.setId(UUID.randomUUID().toString());
        application.setEquipmentId(request.getEquipmentId());
        application.setBorrowerId(borrowerId);
        application.setQuantity(request.getQuantity());
        application.setPurpose(request.getPurpose());
        application.setBorrowDate(request.getBorrowDate());
        application.setExpectedReturnDate(request.getExpectedReturnDate());
        application.setRemark(request.getRemark());
        application.setStatus(EquipmentApplication.ApplicationStatus.pending);
        
        EquipmentApplication saved = applicationRepository.save(application);
        return saved.getId();
    }
    
    /**
     * 获取用户的借用申请列表
     */
    @Transactional(readOnly = true)
    public Page<EquipmentApplicationDto> getUserApplications(String borrowerId, Pageable pageable) {
        Page<Object[]> results = applicationRepository.findApplicationDetailsByBorrowerId(borrowerId, pageable);
        
        List<EquipmentApplicationDto> dtos = results.getContent().stream().map(data -> {
            EquipmentApplicationDto dto = new EquipmentApplicationDto();
            dto.setId((String) data[0]);
            dto.setEquipmentId((String) data[1]);
            dto.setEquipmentName((String) data[2]);
            dto.setEquipmentModel((String) data[3]);
            dto.setCategoryName((String) data[4]);
            dto.setQuantity((Integer) data[5]);
            dto.setPurpose((String) data[6]);
            dto.setBorrowDate((LocalDateTime) data[7]);
            dto.setExpectedReturnDate((LocalDateTime) data[8]);
            dto.setActualReturnDate((LocalDateTime) data[9]);
            dto.setStatus((EquipmentApplication.ApplicationStatus) data[10]);
            dto.setRemark((String) data[11]);
            dto.setApprovedAt((LocalDateTime) data[12]);
            dto.setActualQuantity((Integer) data[13]);
            dto.setReturnCondition((EquipmentApplication.ReturnCondition) data[14]);
            dto.setCreatedAt((LocalDateTime) data[15]);
            dto.setUpdatedAt((LocalDateTime) data[16]);
            return dto;
        }).collect(Collectors.toList());
        
        return new PageImpl<>(dtos, pageable, results.getTotalElements());
    }
    
    /**
     * 根据ID获取申请详情
     */
    @Transactional(readOnly = true)
    public Optional<EquipmentApplicationDto> getApplicationById(String applicationId) {
        Optional<Object[]> result = applicationRepository.findApplicationDetailsById(applicationId);
        
        if (result.isPresent()) {
            Object[] data = result.get();
            EquipmentApplicationDto dto = new EquipmentApplicationDto();
            
            dto.setId((String) data[0]);
            dto.setEquipmentId((String) data[1]);
            dto.setEquipmentName((String) data[2]);
            dto.setEquipmentModel((String) data[3]);
            dto.setCategoryName((String) data[4]);
            dto.setBorrowerId((String) data[5]);
            dto.setBorrowerName((String) data[6]);
            dto.setBorrowerStudentId((String) data[7]);
            dto.setBorrowerPhone((String) data[8]);
            dto.setQuantity((Integer) data[9]);
            dto.setPurpose((String) data[10]);
            dto.setBorrowDate((LocalDateTime) data[11]);
            dto.setExpectedReturnDate((LocalDateTime) data[12]);
            dto.setActualReturnDate((LocalDateTime) data[13]);
            dto.setStatus((EquipmentApplication.ApplicationStatus) data[14]);
            dto.setRemark((String) data[15]);
            dto.setApprovedBy((String) data[16]);
            dto.setApproverName((String) data[17]);
            dto.setApprovedAt((LocalDateTime) data[18]);
            dto.setActualQuantity((Integer) data[19]);
            dto.setReturnCondition((EquipmentApplication.ReturnCondition) data[20]);
            dto.setReturnedBy((String) data[21]);
            dto.setReturnerName((String) data[22]);
            dto.setCreatedAt((LocalDateTime) data[23]);
            dto.setUpdatedAt((LocalDateTime) data[24]);
            
            return Optional.of(dto);
        }
        
        return Optional.empty();
    }
    
    /**
     * 获取超期未归还的申请
     */
    @Transactional(readOnly = true)
    public List<EquipmentApplicationDto> getOverdueApplications() {
        List<EquipmentApplication> overdueApps = applicationRepository.findOverdueApplications(LocalDateTime.now());
        
        return overdueApps.stream().map(app -> {
            Optional<EquipmentApplicationDto> dto = getApplicationById(app.getId());
            return dto.orElse(null);
        }).filter(dto -> dto != null).collect(Collectors.toList());
    }
    
    /**
     * 将EquipmentItem转换为EquipmentItemDto
     */
    private EquipmentItemDto convertToEquipmentItemDto(EquipmentItem item) {
        EquipmentItemDto dto = new EquipmentItemDto();
        dto.setId(item.getId());
        dto.setCategoryId(item.getCategoryId());
        dto.setName(item.getName());
        dto.setModel(item.getModel());
        dto.setSpecification(item.getSpecification());
        dto.setTotalQuantity(item.getTotalQuantity());
        dto.setAvailableQuantity(item.getAvailableQuantity());
        dto.setBorrowedQuantity(item.getBorrowedQuantity());
        dto.setDamagedQuantity(item.getDamagedQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setPurchaseDate(item.getPurchaseDate());
        dto.setWarrantyPeriod(item.getWarrantyPeriod());
        dto.setStorageLocation(item.getStorageLocation());
        dto.setCreatedBy(item.getCreatedBy());
        dto.setIsDeleted(item.getIsDeleted());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());
        
        // 设置分类名称
        if (item.getCategory() != null) {
            dto.setCategoryName(item.getCategory().getName());
        }
        
        return dto;
    }
}
