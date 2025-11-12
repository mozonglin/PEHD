package com.example.pehd.controller;

import com.example.pehd.dto.*;
import com.example.pehd.entity.User;
import com.example.pehd.repository.UserRepository;
import com.example.pehd.service.SunshineRunService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 阳光跑控制器
 */
@RestController
@RequestMapping("/sunshine-run")
@CrossOrigin(origins = "*")
public class SunshineRunController {
    
    private static final Logger logger = LoggerFactory.getLogger(SunshineRunController.class);
    
    @Autowired
    private SunshineRunService sunshineRunService;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 1. 上传阳光跑记录
     * POST /sunshine-run/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<SunshineRunRecordDto>> uploadRecord(
            @Valid @RequestBody SunshineRunUploadRequest request,
            Authentication authentication) {
        try {
            String userId = getUserId(authentication);
            SunshineRunRecordDto record = sunshineRunService.uploadRecord(userId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "上传成功", record));
        } catch (Exception e) {
            logger.error("上传阳光跑记录失败", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 2. 获取个人阳光跑记录列表
     * GET /sunshine-run/my-records
     */
    @GetMapping("/my-records")
    public ResponseEntity<ApiResponse<List<SunshineRunRecordDto>>> getMyRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        try {
            String userId = getUserId(authentication);
            List<SunshineRunRecordDto> records = sunshineRunService.getMyRecords(userId, page, size);
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", records));
        } catch (Exception e) {
            logger.error("获取个人阳光跑记录失败", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 3. 获取个人阳光跑统计信息
     * GET /sunshine-run/my-stats
     */
    @GetMapping("/my-stats")
    public ResponseEntity<ApiResponse<SunshineRunStatsDto>> getMyStats(Authentication authentication) {
        try {
            String userId = getUserId(authentication);
            SunshineRunStatsDto stats = sunshineRunService.getMyStats(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", stats));
        } catch (Exception e) {
            logger.error("获取个人阳光跑统计失败", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 4. 获取班级阳光跑记录（班级管理员）
     * GET /sunshine-run/class-records
     */
    @GetMapping("/class-records")
    public ResponseEntity<ApiResponse<List<SunshineRunRecordDto>>> getClassRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        try {
            String userId = getUserId(authentication);
            User currentUser = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            
            // 验证是否为班级管理员
            if (!isChecker(currentUser)) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "权限不足，仅班级管理员可以查看", null));
            }
            
            String className = currentUser.getClassName();
            if (className == null || className.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "用户班级信息缺失", null));
            }
            
            List<SunshineRunRecordDto> records = sunshineRunService.getClassRecords(className, page, size);
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", records));
        } catch (Exception e) {
            logger.error("获取班级阳光跑记录失败", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 5. 获取单条阳光跑记录详情
     * GET /sunshine-run/records/{id}
     */
    @GetMapping("/records/{id}")
    public ResponseEntity<ApiResponse<SunshineRunRecordDto>> getRecordById(
            @PathVariable String id,
            Authentication authentication) {
        try {
            String userId = getUserId(authentication);
            SunshineRunRecordDto record = sunshineRunService.getRecordById(id, userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", record));
        } catch (Exception e) {
            logger.error("获取阳光跑记录详情失败", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 6. 删除阳光跑记录
     * DELETE /sunshine-run/records/{id}
     */
    @DeleteMapping("/records/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRecord(
            @PathVariable String id,
            Authentication authentication) {
        try {
            String userId = getUserId(authentication);
            sunshineRunService.deleteRecord(id, userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "删除成功", null));
        } catch (Exception e) {
            logger.error("删除阳光跑记录失败", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 7. 获取班级跑步排行榜（班级管理员）
     * GET /sunshine-run/class-ranking
     */
    @GetMapping("/class-ranking")
    public ResponseEntity<ApiResponse<List<SunshineRunRankingDto>>> getClassRanking(
            Authentication authentication) {
        try {
            String userId = getUserId(authentication);
            User currentUser = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            
            // 验证是否为班级管理员
            if (!isChecker(currentUser)) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "权限不足，仅班级管理员可以查看", null));
            }
            
            String className = currentUser.getClassName();
            if (className == null || className.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "用户班级信息缺失", null));
            }
            
            List<SunshineRunRankingDto> rankings = sunshineRunService.getClassRanking(className);
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", rankings));
        } catch (Exception e) {
            logger.error("获取班级排行榜失败", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 从认证信息中获取用户ID
     */
    private String getUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("用户未登录");
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).getId();
        } else if (principal instanceof String) {
            return (String) principal;
        } else {
            throw new RuntimeException("无效的用户认证信息");
        }
    }
    
    /**
     * 检查用户是否为签到员
     */
    private boolean isChecker(User user) {
        return user.getRole().name().equals("CHECKER") || 
               user.getRole().name().equals("SUB_CHECKER");
    }
}

