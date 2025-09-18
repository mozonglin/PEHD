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
        // 验证验证码
        // boolean isCodeValid = verificationService.verifyCode(
        //     loginRequest.getPhoneNumber(), 
        //     loginRequest.getVerificationCode(), 
        //     "login"
        // );
        // 
        // if (!isCodeValid) {
        //     throw new RuntimeException("验证码错误或已过期");
        // }
        
        // 暂时跳过验证码验证，直接返回true
        boolean isCodeValid = true;
        
        // 查找用户
        Optional<User> userOptional = userRepository.findByNameAndStudentIdAndPhoneNumber(
            loginRequest.getName(), 
            loginRequest.getStudentId(), 
            loginRequest.getPhoneNumber()
        );
        
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            throw new RuntimeException("用户信息不匹配，请检查姓名、学号和手机号");
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
     * 用户注册
     */
    public LoginResponse register(LoginRequest registerRequest) {
        // TODO: 暂时禁用验证码校验，等报备成功后再启用
        // 验证验证码
        // boolean isCodeValid = verificationService.verifyCode(
        //     registerRequest.getPhoneNumber(), 
        //     registerRequest.getVerificationCode(), 
        //     "register"
        // );
        // 
        // if (!isCodeValid) {
        //     throw new RuntimeException("验证码错误或已过期");
        // }
        
        // 暂时跳过验证码验证，直接返回true
        boolean isCodeValid = true;
        
        // 验证学生信息是否在checkstudent表中存在
        StudentValidationService.StudentValidationResult validationResult = 
            studentValidationService.validateStudent(registerRequest.getStudentId(), registerRequest.getName());
        
        if (!validationResult.isValid()) {
            throw new RuntimeException(validationResult.getMessage());
        }
        
        // 检查用户是否已存在
        if (userRepository.existsByStudentId(registerRequest.getStudentId())) {
            throw new RuntimeException("学号已存在");
        }
        
        if (userRepository.existsByPhoneNumber(registerRequest.getPhoneNumber())) {
            throw new RuntimeException("手机号已存在");
        }
        
        // 创建新用户，使用从checkstudent表中获取的学校、学院和班级信息
        User user = new User(
            registerRequest.getName(), 
            registerRequest.getStudentId(), 
            registerRequest.getPhoneNumber(),
            validationResult.getSchool(), // 从checkstudent表中获取学校信息
            validationResult.getCollege(), // 从checkstudent表中获取学院信息
            validationResult.getClassName() // 从checkstudent表中获取班级信息
        );
        
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