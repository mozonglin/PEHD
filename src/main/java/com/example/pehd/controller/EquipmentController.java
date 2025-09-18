package com.example.pehd.controller;

import com.example.pehd.dto.*;
import com.example.pehd.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * 器材借阅控制器
 * 为学生端提供器材查看和借阅功能
 */
@RestController
@RequestMapping("/v1/equipment")
@CrossOrigin(origins = "*")
public class EquipmentController {
    
    @Autowired
    private EquipmentService equipmentService;
    
    /**
     * 获取器材分类列表
     * GET /v1/equipment/categories
     */
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<EquipmentCategoryDto>>> getCategories() {
        try {
            List<EquipmentCategoryDto> categories = equipmentService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse<>(true, "获取分类列表成功", categories));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "获取分类列表失败: " + e.getMessage(), null));
        }
    }
    
    /**
     * 根据分类获取器材列表
     * GET /v1/equipment/items?categoryId={categoryId}&page={page}&size={size}
     */
    @GetMapping("/items")
    public ResponseEntity<ApiResponse<Page<EquipmentItemDto>>> getEquipmentsByCategory(
            @RequestParam(required = false) String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        try {
            Sort sort = Sort.by(sortDir.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<EquipmentItemDto> equipments;
            if (categoryId != null && !categoryId.isEmpty()) {
                equipments = equipmentService.getEquipmentsByCategory(categoryId, pageable);
            } else {
                equipments = equipmentService.searchEquipments(null, null, pageable);
            }
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取器材列表成功", equipments));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "获取器材列表失败: " + e.getMessage(), null));
        }
    }
    
    /**
     * 搜索器材
     * GET /v1/equipment/search?keyword={keyword}&categoryId={categoryId}&page={page}&size={size}
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<EquipmentItemDto>>> searchEquipments(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        try {
            Sort sort = Sort.by(sortDir.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<EquipmentItemDto> equipments = equipmentService.searchEquipments(categoryId, keyword, pageable);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "搜索器材成功", equipments));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "搜索器材失败: " + e.getMessage(), null));
        }
    }
    
    /**
     * 获取器材详情
     * GET /v1/equipment/items/{equipmentId}
     */
    @GetMapping("/items/{equipmentId}")
    public ResponseEntity<ApiResponse<EquipmentItemDto>> getEquipmentDetail(@PathVariable String equipmentId) {
        try {
            Optional<EquipmentItemDto> equipment = equipmentService.getEquipmentById(equipmentId);
            
            if (equipment.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(true, "获取器材详情成功", equipment.get()));
            } else {
                return ResponseEntity.ok(new ApiResponse<>(false, "器材不存在", null));
            }
        } catch (Exception e) {
            // 记录完整的错误信息
            e.printStackTrace();
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("doesn't exist")) {
                errorMsg = "器材管理数据库表未初始化，请先执行数据库初始化脚本";
            }
            return ResponseEntity.ok(new ApiResponse<>(false, "获取器材详情失败: " + errorMsg, null));
        }
    }
    
    /**
     * 获取热门器材排行
     * GET /v1/equipment/popular?page={page}&size={size}
     */
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<Page<EquipmentItemDto>>> getPopularEquipments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<EquipmentItemDto> popularEquipments = equipmentService.getPopularEquipments(pageable);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取热门器材成功", popularEquipments));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "获取热门器材失败: " + e.getMessage(), null));
        }
    }
    
    /**
     * 检查器材可用性
     * GET /v1/equipment/items/{equipmentId}/availability?quantity={quantity}
     */
    @GetMapping("/items/{equipmentId}/availability")
    public ResponseEntity<ApiResponse<Boolean>> checkEquipmentAvailability(
            @PathVariable String equipmentId,
            @RequestParam Integer quantity) {
        
        try {
            boolean available = equipmentService.isEquipmentAvailable(equipmentId, quantity);
            String message = available ? "器材可借用" : "器材库存不足";
            
            return ResponseEntity.ok(new ApiResponse<>(true, message, available));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "检查器材可用性失败: " + e.getMessage(), false));
        }
    }
    
    /**
     * 提交借用申请
     * POST /v1/equipment/applications
     */
    @PostMapping("/applications")
    public ResponseEntity<ApiResponse<String>> createApplication(
            @Valid @RequestBody CreateEquipmentApplicationRequest request,
            Authentication authentication) {
        
        try {
            String borrowerId = authentication.getName(); // 从JWT获取用户ID
            String applicationId = equipmentService.createApplication(request, borrowerId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "借用申请提交成功", applicationId));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "提交借用申请失败: " + e.getMessage(), null));
        }
    }
    
    /**
     * 获取我的借用申请列表
     * GET /v1/equipment/my-applications?page={page}&size={size}
     */
    @GetMapping("/my-applications")
    public ResponseEntity<ApiResponse<Page<EquipmentApplicationDto>>> getMyApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        
        try {
            String borrowerId = authentication.getName(); // 从JWT获取用户ID
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            
            Page<EquipmentApplicationDto> applications = equipmentService.getUserApplications(borrowerId, pageable);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取申请列表成功", applications));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "获取申请列表失败: " + e.getMessage(), null));
        }
    }
    
    /**
     * 获取申请详情
     * GET /v1/equipment/applications/{applicationId}
     */
    @GetMapping("/applications/{applicationId}")
    public ResponseEntity<ApiResponse<EquipmentApplicationDto>> getApplicationDetail(
            @PathVariable String applicationId,
            Authentication authentication) {
        
        try {
            Optional<EquipmentApplicationDto> application = equipmentService.getApplicationById(applicationId);
            
            if (application.isPresent()) {
                // 检查是否是申请人本人
                String borrowerId = authentication.getName();
                if (!application.get().getBorrowerId().equals(borrowerId)) {
                    return ResponseEntity.ok(new ApiResponse<>(false, "无权查看此申请", null));
                }
                
                return ResponseEntity.ok(new ApiResponse<>(true, "获取申请详情成功", application.get()));
            } else {
                return ResponseEntity.ok(new ApiResponse<>(false, "申请不存在", null));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "获取申请详情失败: " + e.getMessage(), null));
        }
    }
    
    /**
     * 获取超期提醒（我的超期申请）
     * GET /v1/equipment/my-overdue
     */
    @GetMapping("/my-overdue")
    public ResponseEntity<ApiResponse<List<EquipmentApplicationDto>>> getMyOverdueApplications(
            Authentication authentication) {
        
        try {
            List<EquipmentApplicationDto> overdueApps = equipmentService.getOverdueApplications();
            String borrowerId = authentication.getName();
            
            // 过滤出当前用户的超期申请
            List<EquipmentApplicationDto> myOverdueApps = overdueApps.stream()
                .filter(app -> app.getBorrowerId().equals(borrowerId))
                .toList();
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取超期提醒成功", myOverdueApps));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "获取超期提醒失败: " + e.getMessage(), null));
        }
    }
    
    /**
     * 健康检查接口
     * GET /v1/equipment/health
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(new ApiResponse<>(true, "器材借阅服务运行正常", "OK"));
    }
}
