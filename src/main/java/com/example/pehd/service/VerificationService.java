package com.example.pehd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class VerificationService {
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    private final Random random = new Random();
    
    /**
     * 发送验证码
     */
    public void sendVerificationCode(String phoneNumber, String type) {
        String key = getVerificationKey(phoneNumber, type);
        
        // 检查是否在发送间隔内
        if (redisTemplate.hasKey(key)) {
            throw new RuntimeException("验证码已发送，请稍后再试");
        }
        
        // 生成6位随机验证码
        String code = String.format("%06d", random.nextInt(1000000));
        
        // 存储到Redis，有效期5分钟
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
        
        // 这里应该调用短信服务发送验证码
        // 为了演示，我们只是记录日志
        System.out.println("发送验证码到 " + phoneNumber + ": " + code);
        
        // 模拟发送短信
        simulateSendSMS(phoneNumber, code, type);
    }
    
    /**
     * 验证验证码
     */
    public boolean verifyCode(String phoneNumber, String code, String type) {
        String key = getVerificationKey(phoneNumber, type);
        String storedCode = redisTemplate.opsForValue().get(key);
        
        if (storedCode == null) {
            return false;
        }
        
        boolean isValid = storedCode.equals(code);
        
        if (isValid) {
            // 验证成功后删除验证码
            redisTemplate.delete(key);
        }
        
        return isValid;
    }
    
    /**
     * 生成验证码的Redis key
     */
    private String getVerificationKey(String phoneNumber, String type) {
        return "verification_code:" + type + ":" + phoneNumber;
    }
    
    /**
     * 模拟发送短信
     */
    private void simulateSendSMS(String phoneNumber, String code, String type) {
        String message;
        switch (type) {
            case "login":
                message = "您的登录验证码是：" + code + "，有效期5分钟。";
                break;
            case "register":
                message = "您的注册验证码是：" + code + "，有效期5分钟。";
                break;
            case "change_phone":
                message = "您的手机号修改验证码是：" + code + "，有效期5分钟。";
                break;
            default:
                message = "您的验证码是：" + code + "，有效期5分钟。";
        }
        
        System.out.println("短信发送到 " + phoneNumber + ": " + message);
    }
} 