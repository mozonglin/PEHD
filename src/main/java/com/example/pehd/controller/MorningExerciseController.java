package com.example.pehd.controller;

import com.example.pehd.dto.*;
import com.example.pehd.entity.CheckerAuthorization;
import com.example.pehd.entity.User;
import com.example.pehd.service.MorningExerciseService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/morning-exercise")
@CrossOrigin(origins = "*")
public class MorningExerciseController {
    
    private static final Logger logger = LoggerFactory.getLogger(MorningExerciseController.class);
    
    @Autowired
    private MorningExerciseService morningExerciseService;
    
    /**
     * 1. 获取当前早操考勤活动
     * GET /morning-exercise/current
     */
    @GetMapping("/current")
    public ResponseEntity<ApiResponse<MorningExerciseDto>> getCurrentMorningExercise(Authentication authentication) {
        try {
            // 验证用户身份
            String userId = getUserId(authentication);
            
            Optional<MorningExerciseDto> currentExercise = morningExerciseService.getCurrentMorningExercise();
            
            if (currentExercise.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", currentExercise.get()));
            } else {
                return ResponseEntity.ok(new ApiResponse<>(false, "当前没有进行中的早操考勤活动", null));
            }
            
        } catch (Exception e) {
            logger.error("获取当前早操考勤活动失败", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 2. 获取早操考勤活动列表
     * GET /morning-exercise/list
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<MorningExerciseDto>>> getMorningExerciseList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            Authentication authentication) {
        try {
            // 验证用户身份
            String userId = getUserId(authentication);
            
            List<MorningExerciseDto> exercises = morningExerciseService.getMorningExerciseList(page, limit);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", exercises));
            
        } catch (Exception e) {
            logger.error("获取早操考勤活动列表失败", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 3. 创建早操考勤活动
     * POST /morning-exercise/create
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<MorningExerciseDto>> createMorningExercise(
            @Valid @RequestBody CreateMorningExerciseRequest request,
            Authentication authentication) {
        try {
            // 验证用户身份
            String userId = getUserId(authentication);
            
            MorningExerciseDto exercise = morningExerciseService.createMorningExercise(request, userId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "创建成功", exercise));
            
        } catch (Exception e) {
            logger.error("创建早操考勤活动失败", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 4. 请求早操签到码
     * POST /morning-exercise/request-checkin-code
     */
    @PostMapping("/request-checkin-code")
    public ResponseEntity<ApiResponse<MorningExerciseCodeResponse>> requestCheckinCode(
            @Valid @RequestBody MorningExerciseRequest request,
            Authentication authentication) {
        try {
            // 验证用户身份
            String userId = getUserId(authentication);
            
            MorningExerciseCodeResponse response = morningExerciseService.requestCheckinCode(request, userId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", response));
            
        } catch (Exception e) {
            logger.error("请求早操签到码失败: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 5. 验证早操签到码
     * POST /morning-exercise/validate-checkin-code
     */
    @PostMapping("/validate-checkin-code")
    public ResponseEntity<ApiResponse<String>> validateCheckinCode(
            @Valid @RequestBody ValidateCodeRequest request,
            Authentication authentication) {
        try {
            // 验证用户身份
            String checkerId = getUserId(authentication);
            
            morningExerciseService.validateCheckinCode(request, checkerId);
            
            // 从签到码中解析学生姓名（简化处理）
            String studentName = "学生"; // 实际应该从服务层返回
            String message = String.format("签到成功！%s 已完成早操考勤签到", studentName);
            
            return ResponseEntity.ok(new ApiResponse<>(true, message, null));
            
        } catch (Exception e) {
            logger.error("验证早操签到码失败: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 6. 请求早操签退码
     * POST /morning-exercise/request-checkout-code
     */
    @PostMapping("/request-checkout-code")
    public ResponseEntity<ApiResponse<MorningExerciseCodeResponse>> requestCheckoutCode(
            @Valid @RequestBody MorningExerciseRequest request,
            Authentication authentication) {
        try {
            // 验证用户身份
            String userId = getUserId(authentication);
            
            MorningExerciseCodeResponse response = morningExerciseService.requestCheckoutCode(request, userId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", response));
            
        } catch (Exception e) {
            logger.error("请求早操签退码失败: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 7. 验证早操签退码
     * POST /morning-exercise/validate-checkout-code
     */
    @PostMapping("/validate-checkout-code")
    public ResponseEntity<ApiResponse<MorningExerciseCheckoutResponse>> validateCheckoutCode(
            @Valid @RequestBody ValidateCodeRequest request,
            Authentication authentication) {
        try {
            // 验证用户身份
            String checkerId = getUserId(authentication);
            
            MorningExerciseCheckoutResponse response = morningExerciseService.validateCheckoutCode(request, checkerId);
            
            // 从签退码中解析学生姓名（简化处理）
            String studentName = "学生"; // 实际应该从服务层返回
            String message = String.format("签退成功！%s 已完成早操考勤，获得%d积分", studentName, response.getPointsEarned());
            
            return ResponseEntity.ok(new ApiResponse<>(true, message, response));
            
        } catch (Exception e) {
            logger.error("验证早操签退码失败: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 8. 扫码授权二级管理员
     * POST /morning-exercise/scan-authorize
     */
    @PostMapping("/scan-authorize")
    public ResponseEntity<ApiResponse<String>> scanAuthorize(
            @RequestBody Map<String, String> requestBody,
            Authentication authentication) {
        try {
            // 验证用户身份
            String authorizerId = getUserId(authentication);
            
            String targetStudentId = requestBody.get("targetStudentId");
            String exerciseId = requestBody.get("exerciseId");
            
            if (targetStudentId == null || exerciseId == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "参数不完整", null));
            }
            
            morningExerciseService.scanAuthorize(targetStudentId, exerciseId, authorizerId);
            
            String message = String.format("授权成功！%s 已成为二级管理员", targetStudentId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, message, null));
            
        } catch (Exception e) {
            logger.error("扫码授权二级管理员失败: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 9. 获取早操考勤权限
     * GET /morning-exercise/user-permissions
     */
    @GetMapping("/user-permissions")
    public ResponseEntity<ApiResponse<List<CheckerAuthorization>>> getUserPermissions(Authentication authentication) {
        try {
            // 验证用户身份
            String userId = getUserId(authentication);
            
            List<CheckerAuthorization> permissions = morningExerciseService.getUserPermissions(userId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", permissions));
            
        } catch (Exception e) {
            logger.error("获取早操考勤权限失败", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 10. 获取用户在早操中的角色
     * GET /morning-exercise/{id}/user-role
     */
    @GetMapping("/{id}/user-role")
    public ResponseEntity<ApiResponse<Map<String, String>>> getUserRoleInExercise(
            @PathVariable String id,
            Authentication authentication) {
        try {
            // 验证用户身份
            String userId = getUserId(authentication);
            
            Optional<String> role = morningExerciseService.getUserRoleInExercise(id, userId);
            
            Map<String, String> responseData = new HashMap<>();
            responseData.put("role", role.orElse("STUDENT"));
            responseData.put("exerciseId", id);
            responseData.put("userId", userId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", responseData));
            
        } catch (Exception e) {
            logger.error("获取用户早操角色失败", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 11. 获取早操考勤记录
     * GET /morning-exercise/{id}/attendance
     */
    @GetMapping("/{id}/attendance")
    public ResponseEntity<ApiResponse<List<MorningExerciseAttendanceDto>>> getAttendanceRecords(
            @PathVariable String id,
            Authentication authentication) {
        try {
            // 验证用户身份
            String userId = getUserId(authentication);
            
            List<MorningExerciseAttendanceDto> attendances = morningExerciseService.getAttendanceRecords(id);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", attendances));
            
        } catch (Exception e) {
            logger.error("获取早操考勤记录失败", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 12. 获取早操考勤统计
     * GET /morning-exercise/{id}/stats
     */
    @GetMapping("/{id}/stats")
    public ResponseEntity<ApiResponse<MorningExerciseStatsDto>> getExerciseStats(
            @PathVariable String id,
            Authentication authentication) {
        try {
            // 验证用户身份
            String userId = getUserId(authentication);
            
            MorningExerciseStatsDto stats = morningExerciseService.getExerciseStats(id);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", stats));
            
        } catch (Exception e) {
            logger.error("获取早操考勤统计失败", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 从Authentication获取用户ID
     */
    private String getUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
