package com.example.pehd.controller;

import com.example.pehd.dto.*;
import com.example.pehd.entity.AttendanceRecord;
import com.example.pehd.entity.User;
import com.example.pehd.repository.AttendanceRecordRepository;
import com.example.pehd.service.ActivityService;
import com.example.pehd.service.CheckinService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/activities")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ActivityController {
    
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private CheckinService checkinService;
    
    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;
    
    /**
     * 获取活动列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ActivityDto>>> getActivities(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status) {
        
        try {
            String userId = getUserId(authentication);
            List<ActivityDto> activities = activityService.getActivities(userId, page, limit, category, status);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", activities));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 获取单个活动详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ActivityDto>> getActivity(
            @PathVariable String id,
            Authentication authentication) {
        
        try {
            String userId = getUserId(authentication);
            ActivityDto activity = activityService.getActivityById(id, userId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", activity));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 创建活动
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ActivityDto>> createActivity(
            @Valid @RequestBody CreateActivityRequest request,
            Authentication authentication) {
        
        try {
            String userId = getUserId(authentication);
            ActivityDto activity = activityService.createActivity(request, userId);
            
            String message = request.getIsDraft() != null && request.getIsDraft() 
                ? "活动草稿保存成功" : "活动创建成功，等待审核";
            
            return ResponseEntity.ok(new ApiResponse<>(true, message, activity));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 更新活动（仅限草稿或被拒绝的活动）
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ActivityDto>> updateActivity(
            @PathVariable String id,
            @Valid @RequestBody CreateActivityRequest request,
            Authentication authentication) {
        
        try {
            String userId = getUserId(authentication);
            ActivityDto activity = activityService.updateActivity(id, request, userId);
            
            String message = request.getIsDraft() != null && request.getIsDraft() 
                ? "活动草稿更新成功" : "活动更新成功，已提交审核";
            
            return ResponseEntity.ok(new ApiResponse<>(true, message, activity));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 报名活动
     */
    @PostMapping("/{id}/register")
    public ResponseEntity<ApiResponse<Void>> registerActivity(
            @PathVariable String id,
            Authentication authentication) {
        
        try {
            String userId = getUserId(authentication);
            activityService.registerActivity(id, userId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "报名成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 取消报名
     */
    @DeleteMapping("/{id}/register")
    public ResponseEntity<ApiResponse<Void>> cancelRegistration(
            @PathVariable String id,
            Authentication authentication) {
        
        try {
            String userId = getUserId(authentication);
            activityService.cancelRegistration(id, userId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "取消报名成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 获取我的活动
     */
    @GetMapping("/my-activities")
    public ResponseEntity<ApiResponse<List<ActivityDto>>> getMyActivities(
            Authentication authentication,
            @RequestParam(defaultValue = "registered") String type) {
        
        try {
            String userId = getUserId(authentication);
            List<ActivityDto> activities = activityService.getMyActivities(userId, type);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", activities));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 获取活动参与者列表
     */
    @GetMapping("/{id}/participants")
    public ResponseEntity<ApiResponse<List<UserDto>>> getActivityParticipants(
            @PathVariable String id,
            Authentication authentication) {
        
        try {
            String userId = getUserId(authentication);
            List<UserDto> participants = activityService.getActivityParticipants(id, userId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", participants));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 获取活动签到记录
     */
    @GetMapping("/{id}/attendance")
    public ResponseEntity<ApiResponse<List<AttendanceRecordDto>>> getActivityAttendance(
            @PathVariable String id,
            Authentication authentication) {
        
        try {
            String userId = getUserId(authentication);
            // 验证权限（只有活动创建者可以查看）
            activityService.getActivityById(id, userId); // 这里会进行权限验证
            
            List<AttendanceRecord> records = attendanceRecordRepository.findByActivityId(id);
            List<AttendanceRecordDto> attendanceRecords = records.stream()
                    .map(this::convertToAttendanceRecordDto)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", attendanceRecords));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 移除活动参与者
     */
    @DeleteMapping("/{id}/participants/{userId}")
    public ResponseEntity<ApiResponse<Void>> removeParticipant(
            @PathVariable String id,
            @PathVariable String userId,
            Authentication authentication) {
        
        try {
            String currentUserId = getUserId(authentication);
            activityService.removeParticipant(id, userId, currentUserId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "参与者移除成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 请求活动签到码
     */
    @PostMapping("/request-checkin-code")
    public ResponseEntity<ApiResponse<CheckinCodeResponse>> requestCheckinCode(
            @Valid @RequestBody CheckinCodeRequest request,
            Authentication authentication) {
        
        try {
            String userId = getUserId(authentication);
            CheckinCodeResponse response = checkinService.requestCheckinCode(request, userId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 验证签到码并完成签到
     */
    @PostMapping("/validate-checkin-code")
    public ResponseEntity<ApiResponse<Void>> validateCheckinCode(
            @Valid @RequestBody ValidateCodeRequest request,
            Authentication authentication) {
        
        try {
            String userId = getUserId(authentication);
            String message = checkinService.validateCheckinCode(request, userId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 请求活动签退码
     */
    @PostMapping("/request-checkout-code")
    public ResponseEntity<ApiResponse<CheckinCodeResponse>> requestCheckoutCode(
            @Valid @RequestBody CheckinCodeRequest request,
            Authentication authentication) {
        
        try {
            String userId = getUserId(authentication);
            CheckinCodeResponse response = checkinService.requestCheckoutCode(request, userId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 验证签退码并完成签退
     */
    @PostMapping("/validate-checkout-code")
    public ResponseEntity<ApiResponse<CheckoutResponse>> validateCheckoutCode(
            @Valid @RequestBody ValidateCodeRequest request,
            Authentication authentication) {
        
        try {
            String userId = getUserId(authentication);
            CheckoutResponse response = checkinService.validateCheckoutCode(request, userId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, response.getMessage(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 从Authentication获取用户ID
     */
    private String getUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
    
    /**
     * 转换AttendanceRecord为DTO
     */
    private AttendanceRecordDto convertToAttendanceRecordDto(AttendanceRecord record) {
        AttendanceRecordDto dto = new AttendanceRecordDto();
        dto.setId(record.getId());
        dto.setActivityId(record.getActivityId());
        dto.setUserId(record.getUserId());
        dto.setUserName(record.getUserName());
        dto.setStudentId(record.getStudentId());
        dto.setCheckInTime(record.getCheckInTime());
        dto.setCheckInLocation(record.getCheckInLocation());
        return dto;
    }
} 