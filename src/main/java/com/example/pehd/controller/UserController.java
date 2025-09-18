package com.example.pehd.controller;

import com.example.pehd.dto.*;
import com.example.pehd.entity.User;
import com.example.pehd.service.AuthService;
import com.example.pehd.service.UserService;
import com.example.pehd.service.VerificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private VerificationService verificationService;
    
    /**
     * 获取用户资料
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto>> getProfile(Authentication authentication) {
        try {
            String userId = getUserId(authentication);
            UserDto userDto = userService.getUserById(userId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", userDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 更新用户资料
     */
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {
        
        try {
            String userId = getUserId(authentication);
            UserDto userDto = userService.updateProfile(userId, request);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "更新成功", userDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 修改手机号
     */
    @PutMapping("/phone")
    public ResponseEntity<ApiResponse<Void>> updatePhone(
            @Valid @RequestBody UpdatePhoneRequest request,
            Authentication authentication) {
        
        try {
            String userId = getUserId(authentication);
            userService.updatePhone(userId, request);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "手机号修改成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 获取用户积分
     */
    @GetMapping("/points")
    public ResponseEntity<ApiResponse<UserPointsDto>> getUserPoints(Authentication authentication) {
        try {
            String userId = getUserId(authentication);
            UserPointsDto userPoints = userService.getUserPoints(userId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", userPoints));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 获取积分兑换记录
     */
    @GetMapping("/points/exchanges")
    public ResponseEntity<ApiResponse<Object>> getPointExchanges(Authentication authentication) {
        try {
            String userId = getUserId(authentication);
            // 这里暂时返回空列表，后续可以扩展
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", new java.util.ArrayList<>()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 积分兑换
     */
    @PostMapping("/points/exchange")
    public ResponseEntity<ApiResponse<Void>> exchangePoints(
            @Valid @RequestBody PointExchangeRequest request,
            Authentication authentication) {
        
        try {
            String userId = getUserId(authentication);
            userService.exchangePoints(userId, request);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "兑换成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 获取诚信度
     */
    @GetMapping("/integrity/score")
    public ResponseEntity<ApiResponse<Integer>> getIntegrityScore(Authentication authentication) {
        try {
            String userId = getUserId(authentication);
            Integer score = userService.getIntegrityScore(userId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", score));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 获取权限设置
     */
    @GetMapping("/settings/permissions")
    public ResponseEntity<ApiResponse<UserPermissionsDto>> getPermissions(Authentication authentication) {
        try {
            String userId = getUserId(authentication);
            UserPermissionsDto permissions = userService.getPermissions(userId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "获取成功", permissions));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    /**
     * 更新权限设置
     */
    @PutMapping("/settings/permissions")
    public ResponseEntity<ApiResponse<Void>> updatePermissions(
            @Valid @RequestBody UserPermissionsDto request,
            Authentication authentication) {
        
        try {
            String userId = getUserId(authentication);
            userService.updatePermissions(userId, request);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "更新成功"));
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
} 