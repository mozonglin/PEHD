package com.example.pehd.service;

import com.example.pehd.dto.*;
import com.example.pehd.entity.User;
import com.example.pehd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private VerificationService verificationService;
    
    /**
     * 根据用户ID获取用户信息
     */
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        return convertToDto(user);
    }
    
    /**
     * 更新用户资料
     */
    public UserDto updateProfile(String userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        user.setName(request.getName());
        user.setSchool(request.getSchool());
        user.setCollege(request.getCollege());
        user.setAvatar(request.getAvatar());
        user.setUpdatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }
    
    /**
     * 修改手机号
     */
    public void updatePhone(String userId, UpdatePhoneRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 验证验证码
        if (!verificationService.verifyCode(request.getNewPhoneNumber(), request.getVerificationCode(), "change_phone")) {
            throw new RuntimeException("验证码错误或已过期");
        }
        
        // 检查新手机号是否已被使用
        if (userRepository.existsByPhoneNumber(request.getNewPhoneNumber())) {
            throw new RuntimeException("该手机号已被使用");
        }
        
        user.setPhoneNumber(request.getNewPhoneNumber());
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
    }
    
    /**
     * 积分兑换
     */
    public void exchangePoints(String userId, PointExchangeRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 检查积分是否足够
        if (user.getPoints() < request.getPoints()) {
            throw new RuntimeException("积分不足");
        }
        
        // 根据兑换类型计算学时
        int studyHours = 0;
        switch (request.getType()) {
            case "PARTICIPATE_ACTIVITY":
                studyHours = request.getPoints() / 10; // 10积分 = 1学时
                break;
            case "CREATE_ACTIVITY":
                studyHours = request.getPoints() / 5; // 5积分 = 1学时
                break;
            default:
                throw new RuntimeException("无效的兑换类型");
        }
        
        // 更新用户积分和学时
        user.setPoints(user.getPoints() - request.getPoints());
        user.setStudyHours(user.getStudyHours() + studyHours);
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
    }
    
    /**
     * 获取用户积分信息
     */
    public UserPointsDto getUserPoints(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        return new UserPointsDto(
            user.getPoints(),
            user.getPeActivityPoints(),
            user.getMorningExercisePoints(),
            user.getIntegrityScore()
        );
    }
    
    /**
     * 获取用户诚信度
     */
    public Integer getIntegrityScore(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        return user.getIntegrityScore();
    }
    
    /**
     * 获取用户权限设置
     */
    public UserPermissionsDto getPermissions(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 这里可以从数据库或缓存中获取权限设置
        // 暂时返回默认值
        return new UserPermissionsDto(true, false, true, true, false);
    }
    
    /**
     * 更新用户权限设置
     */
    public void updatePermissions(String userId, UserPermissionsDto permissions) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 这里可以将权限设置保存到数据库或缓存中
        // 暂时只做验证
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    /**
     * 转换User实体为DTO
     */
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setStudentId(user.getStudentId());
        dto.setSchool(user.getSchool());
        dto.setCollege(user.getCollege());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAvatar(user.getAvatar());
        dto.setPoints(user.getPoints());
        dto.setStudyHours(user.getStudyHours());
        dto.setIntegrityScore(user.getIntegrityScore());
        dto.setIsLoggedIn(user.getIsLoggedIn());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
} 