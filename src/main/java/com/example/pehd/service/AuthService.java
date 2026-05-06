package com.example.pehd.service;

import com.example.pehd.dto.LoginRequest;
import com.example.pehd.dto.LoginResponse;
import com.example.pehd.entity.User;
import com.example.pehd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private VerificationService verificationService;
    
    @Autowired
    private StudentValidationService studentValidationService;
    
    /**
     * 用户登录
     */
    public LoginResponse login(LoginRequest loginRequest) {
        // TODO: 暂时禁用验证码校验，等报备成功后再启用
        boolean isCodeValid = true;
        
        // 先检查学号是否已注册
        Optional<User> byStudentId = userRepository.findByStudentId(loginRequest.getStudentId());
        if (byStudentId.isEmpty()) {
            throw new RuntimeException("该学号尚未鉴权，请先完成学号鉴权");
        }
        
        User user = byStudentId.get();
        
        // 检查学校是否匹配
        if (loginRequest.getSchool() != null && !loginRequest.getSchool().isEmpty()) {
            if (!user.getSchool().equals(loginRequest.getSchool())) {
                throw new RuntimeException("所选学校与该学号注册信息不一致");
            }
        }
        
        // 检查姓名是否匹配
        if (!user.getName().equals(loginRequest.getName())) {
            throw new RuntimeException("姓名与该学号注册信息不一致");
        }
        
        // 检查手机号是否匹配
        if (!user.getPhoneNumber().equals(loginRequest.getPhoneNumber())) {
            throw new RuntimeException("手机号与该学号注册信息不一致");
        }
        
        // 更新登录状态
        user.setIsLoggedIn(true);
        user = userRepository.save(user);
        
        // 生成JWT Token
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        
        return new LoginResponse(user, accessToken, refreshToken, jwtService.getExpirationTime());
    }
    
    /**
     * 用户注册（学号鉴权）
     */
    public LoginResponse register(LoginRequest registerRequest) {
        // TODO: 暂时禁用验证码校验，等报备成功后再启用
        boolean isCodeValid = true;
        
        // 检查是否已鉴权（学号已存在于用户表）
        if (userRepository.existsByStudentId(registerRequest.getStudentId())) {
            throw new RuntimeException("该学号已完成鉴权，无需重复操作，请直接登录");
        }
        
        // 检查手机号是否已被其他账号绑定
        if (userRepository.existsByPhoneNumber(registerRequest.getPhoneNumber())) {
            throw new RuntimeException("该手机号已被其他账号绑定");
        }
        
        // 学校字段必填校验
        if (registerRequest.getSchool() == null || registerRequest.getSchool().trim().isEmpty()) {
            throw new RuntimeException("请选择学校");
        }
        
        // 验证学生信息：学号+姓名+学校 三项必须全部匹配
        StudentValidationService.StudentValidationResult validationResult = 
            studentValidationService.validateStudent(
                registerRequest.getStudentId(), 
                registerRequest.getName(),
                registerRequest.getSchool()
            );
        
        if (!validationResult.isValid()) {
            throw new RuntimeException(validationResult.getMessage());
        }
        
        User user = new User(
            registerRequest.getName(), 
            registerRequest.getStudentId(), 
            registerRequest.getPhoneNumber(),
            validationResult.getSchool(),
            validationResult.getCollege(),
            validationResult.getClassName()
        );
        user.setGender(validationResult.getGender());
        
        user.setIsLoggedIn(true);
        user = userRepository.save(user);
        
        // 生成JWT Token
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        
        return new LoginResponse(user, accessToken, refreshToken, jwtService.getExpirationTime());
    }
    
    /**
     * 发送验证码
     */
    public void sendVerificationCode(String phoneNumber, String type) {
        // TODO: 暂时禁用验证码发送，等报备成功后再启用
        // verificationService.sendVerificationCode(phoneNumber, type);
        
        // 暂时不做实际发送，但保留接口供前端调用
        System.out.println("验证码发送已暂时禁用，手机号: " + phoneNumber + ", 类型: " + type);
    }
    
    /**
     * 刷新Token
     */
    public LoginResponse refreshToken(String refreshToken) {
        try {
            String username = jwtService.extractUsername(refreshToken);
            Optional<User> userOptional = userRepository.findByStudentId(username);
            
            if (userOptional.isEmpty()) {
                throw new RuntimeException("用户不存在");
            }
            
            User user = userOptional.get();
            
            if (jwtService.isTokenValid(refreshToken, user)) {
                String newAccessToken = jwtService.generateToken(user);
                String newRefreshToken = jwtService.generateRefreshToken(user);
                
                return new LoginResponse(user, newAccessToken, newRefreshToken, jwtService.getExpirationTime());
            } else {
                throw new RuntimeException("刷新Token无效");
            }
        } catch (Exception e) {
            throw new RuntimeException("刷新Token失败: " + e.getMessage());
        }
    }
    
    /**
     * 用户登出
     */
    public void logout(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setIsLoggedIn(false);
            userRepository.save(user);
        }
    }
    
    /**
     * 验证Token
     */
    public User verifyToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            Optional<User> userOptional = userRepository.findByStudentId(username);
            
            if (userOptional.isEmpty()) {
                throw new RuntimeException("用户不存在");
            }
            
            User user = userOptional.get();
            
            if (jwtService.isTokenValid(token, user)) {
                return user;
            } else {
                throw new RuntimeException("Token无效");
            }
        } catch (Exception e) {
            throw new RuntimeException("Token验证失败: " + e.getMessage());
        }
    }
} 