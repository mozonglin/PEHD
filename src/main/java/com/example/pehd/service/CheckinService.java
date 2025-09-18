package com.example.pehd.service;

import com.example.pehd.dto.CheckinCodeRequest;
import com.example.pehd.dto.CheckinCodeResponse;
import com.example.pehd.dto.CheckoutResponse;
import com.example.pehd.dto.ValidateCodeRequest;
import com.example.pehd.entity.Activity;
import com.example.pehd.entity.ActivityCheckinCode;
import com.example.pehd.entity.ActivityCheckoutCode;
import com.example.pehd.entity.ActivityRegistration;
import com.example.pehd.entity.AttendanceRecord;
import com.example.pehd.entity.PointsRecord;
import com.example.pehd.entity.User;
import com.example.pehd.repository.ActivityRepository;
import com.example.pehd.repository.ActivityCheckinCodeRepository;
import com.example.pehd.repository.ActivityCheckoutCodeRepository;
import com.example.pehd.repository.ActivityRegistrationRepository;
import com.example.pehd.repository.AttendanceRecordRepository;
import com.example.pehd.repository.PointsRecordRepository;
import com.example.pehd.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CheckinService {
    
    private static final Logger logger = LoggerFactory.getLogger(CheckinService.class);
    
    // 签到码有效期：40秒
    private static final long CHECKIN_CODE_EXPIRE_SECONDS = 40;
    
    @Autowired
    private ActivityRepository activityRepository;
    
    @Autowired
    private ActivityCheckinCodeRepository checkinCodeRepository;
    
    @Autowired
    private ActivityRegistrationRepository registrationRepository;
    
    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private ActivityCheckoutCodeRepository checkoutCodeRepository;
    
    @Autowired
    private PointsRecordRepository pointsRecordRepository;
    
    @Autowired
    private WebSocketPushService webSocketPushService;
    
    /**
     * 请求活动签到码
     */
    public CheckinCodeResponse requestCheckinCode(CheckinCodeRequest request, String userId) {
        // 验证用户是否存在
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 验证活动是否存在
        Activity activity = activityRepository.findById(request.getActivityId())
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        
        // 验证用户是否已报名该活动
        if (!activityService.isUserRegistered(userId, request.getActivityId())) {
            throw new RuntimeException("您未报名此活动，无法获取签到码");
        }
        
        // 验证活动是否可以签到
        if (!activityService.canCheckIn(request.getActivityId())) {
            throw new RuntimeException("活动尚未开始或已结束超过30分钟，无法获取签到码");
        }
        
        // 检查是否已经签到
        if (attendanceRecordRepository.existsByActivityIdAndUserId(request.getActivityId(), userId)) {
            throw new RuntimeException("您已经签到过了，无法重复获取签到码");
        }
        
        // 生成唯一签到码
        long currentTime = System.currentTimeMillis();
        long expiresAt = currentTime + (CHECKIN_CODE_EXPIRE_SECONDS * 1000);
        String uniqueCode = generateUniqueCode(request.getActivityId(), userId, currentTime);
        
        // 使现有的签到码失效
        checkinCodeRepository.invalidateByActivityIdAndStudentId(request.getActivityId(), user.getStudentId());
        
        // 创建新的签到码记录
        ActivityCheckinCode checkinCode = new ActivityCheckinCode();
        checkinCode.setId(UUID.randomUUID().toString());
        checkinCode.setUniqueCode(uniqueCode);
        checkinCode.setActivityId(request.getActivityId());
        checkinCode.setActivityName(activity.getTitle());
        checkinCode.setStudentId(user.getStudentId());
        checkinCode.setStudentName(user.getName());
        checkinCode.setTimestamp(currentTime);
        checkinCode.setExpiresAt(expiresAt);
        checkinCode.setIsValid(true);
        checkinCode.setIsUsed(false);
        checkinCode.setCreatedAt(LocalDateTime.now());
        
        checkinCodeRepository.save(checkinCode);
        
        logger.info("用户 {} 获取活动 {} 的签到码: {}", user.getName(), activity.getTitle(), uniqueCode);
        
        return new CheckinCodeResponse(
                uniqueCode,
                request.getActivityId(),
                activity.getTitle(),
                user.getStudentId(),
                user.getName(),
                currentTime,
                expiresAt,
                true
        );
    }
    
    /**
     * 验证签到码并完成签到
     */
    public String validateCheckinCode(ValidateCodeRequest request, String validatorUserId) {
        // 验证扫码者是否存在
        User validator = userRepository.findById(validatorUserId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 查找签到码记录
        Optional<ActivityCheckinCode> optionalCheckinCode = checkinCodeRepository.findByUniqueCode(request.getUniqueCode());
        if (!optionalCheckinCode.isPresent()) {
            throw new RuntimeException("签到码格式错误或不存在");
        }
        
        ActivityCheckinCode checkinCode = optionalCheckinCode.get();
        
        // 验证签到码是否有效
        if (!checkinCode.getIsValid()) {
            throw new RuntimeException("签到码无效");
        }
        
        // 验证签到码是否已使用
        if (checkinCode.getIsUsed()) {
            throw new RuntimeException("签到码已被使用");
        }
        
        // 验证签到码是否过期
        long currentTime = System.currentTimeMillis();
        if (currentTime > checkinCode.getExpiresAt()) {
            throw new RuntimeException("签到码已过期，请重新获取");
        }
        
        // 验证活动是否存在
        Activity activity = activityRepository.findById(checkinCode.getActivityId())
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        
        // 验证活动是否可以签到
        if (!activityService.canCheckIn(checkinCode.getActivityId())) {
            throw new RuntimeException("活动不在签到时间范围内");
        }
        
        // 验证学生是否报名了该活动
        User student = userRepository.findByStudentId(checkinCode.getStudentId())
                .orElseThrow(() -> new RuntimeException("学生不存在"));
        
        if (!activityService.isUserRegistered(student.getId(), checkinCode.getActivityId())) {
            throw new RuntimeException("该学生未报名此活动");
        }
        
        // 检查是否已经签到
        if (attendanceRecordRepository.existsByActivityIdAndUserId(checkinCode.getActivityId(), student.getId())) {
            throw new RuntimeException("该学生已经签到过了");
        }
        
        // 创建签到记录
        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.setId(UUID.randomUUID().toString());
        attendanceRecord.setActivityId(checkinCode.getActivityId());
        attendanceRecord.setUserId(student.getId());
        attendanceRecord.setUserName(student.getName());
        attendanceRecord.setStudentId(student.getStudentId());
        attendanceRecord.setCheckInTime(LocalDateTime.now());
        attendanceRecord.setCheckInLocation(activity.getLocation());
        attendanceRecord.setCheckedInBy(validatorUserId);
        attendanceRecord.setQrCodeData(request.getUniqueCode());
        attendanceRecord.setCreatedAt(LocalDateTime.now());
        
        attendanceRecordRepository.save(attendanceRecord);
        
        // 更新活动报名状态为已参加
        ActivityRegistration registration = registrationRepository.findByUserIdAndActivityId(student.getId(), checkinCode.getActivityId())
                .orElseThrow(() -> new RuntimeException("报名记录不存在"));
        registration.setStatus(ActivityRegistration.RegistrationStatus.ATTENDED);
        registrationRepository.save(registration);
        
        // 标记签到码为已使用
        checkinCode.setIsUsed(true);
        checkinCode.setUsedAt(LocalDateTime.now());
        checkinCode.setUsedBy(validatorUserId);
        checkinCodeRepository.save(checkinCode);
        
        // WebSocket推送签到通知给活动创建者
        if (activity.getOrganizerId() != null) {
            try {
                webSocketPushService.pushCheckInNotification(
                    activity.getOrganizerId(),
                    student.getName(),
                    activity.getTitle()
                );
            } catch (Exception e) {
                logger.error("WebSocket推送签到通知失败: {}", e.getMessage());
            }
        }
        
        // 注意：根据API文档，积分应该在签退时发放，此处不再发放积分
        // 积分发放逻辑已移至签退功能中
        
        logger.info("用户 {} 为学生 {} 完成活动 {} 的签到", validator.getName(), student.getName(), activity.getTitle());
        
        return String.format("签到成功！%s 已完成 %s 签到", student.getName(), activity.getTitle());
    }
    
    /**
     * 生成唯一签到码
     */
    private String generateUniqueCode(String activityId, String userId, long timestamp) {
        // 格式：ACT_CHECKIN_[活动ID前8位]_[用户ID前8位]_[时间戳后8位]
        String activityPrefix = activityId.length() >= 8 ? activityId.substring(0, 8) : activityId;
        String userPrefix = userId.length() >= 8 ? userId.substring(0, 8) : userId;
        String timestampSuffix = String.valueOf(timestamp).substring(String.valueOf(timestamp).length() - 8);
        
        return String.format("ACT_CHECKIN_%s_%s_%s", activityPrefix, userPrefix, timestampSuffix);
    }
    
    /**
     * 清理过期的签到码
     */
    public void cleanupExpiredCodes() {
        long currentTime = System.currentTimeMillis();
        int deletedCount = checkinCodeRepository.deleteByExpiresAtBefore(currentTime);
        if (deletedCount > 0) {
            logger.info("清理了 {} 个过期的签到码", deletedCount);
        }
    }
    
    /**
     * 验证签到码是否有效
     */
    public boolean isCodeValid(String uniqueCode) {
        Optional<ActivityCheckinCode> optionalCode = checkinCodeRepository.findByUniqueCode(uniqueCode);
        if (!optionalCode.isPresent()) {
            return false;
        }
        
        ActivityCheckinCode code = optionalCode.get();
        long currentTime = System.currentTimeMillis();
        
        return code.getIsValid() && !code.getIsUsed() && currentTime <= code.getExpiresAt();
    }
    
    /**
     * 请求活动签退码
     */
    public CheckinCodeResponse requestCheckoutCode(CheckinCodeRequest request, String userId) {
        // 验证用户是否存在
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 验证活动是否存在
        Activity activity = activityRepository.findById(request.getActivityId())
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        
        // 验证用户是否已报名该活动
        if (!activityService.isUserRegistered(userId, request.getActivityId())) {
            throw new RuntimeException("您未报名此活动，无法获取签退码");
        }
        
        // 验证用户是否已完成签到
        AttendanceRecord attendanceRecord = attendanceRecordRepository.findByActivityIdAndUserId(request.getActivityId(), userId)
                .orElseThrow(() -> new RuntimeException("您尚未完成签到，无法获取签退码"));
        
        // 检查是否已经签退
        if (attendanceRecord.getIsCheckedOut()) {
            throw new RuntimeException("您已经签退过了，无法重复获取签退码");
        }
        
        // 验证活动是否可以签退
        if (!activityService.canCheckOut(request.getActivityId())) {
            throw new RuntimeException("活动尚未开始或已截止，无法获取签退码");
        }
        
        // 生成唯一签退码
        long currentTime = System.currentTimeMillis();
        long expiresAt = currentTime + (CHECKIN_CODE_EXPIRE_SECONDS * 1000);
        String uniqueCode = generateUniqueCheckoutCode(request.getActivityId(), userId, currentTime);
        
        // 使现有的签退码失效
        checkoutCodeRepository.invalidateByActivityIdAndStudentId(request.getActivityId(), user.getStudentId());
        
        // 创建新的签退码记录
        ActivityCheckoutCode checkoutCode = new ActivityCheckoutCode();
        checkoutCode.setId(UUID.randomUUID().toString());
        checkoutCode.setUniqueCode(uniqueCode);
        checkoutCode.setActivityId(request.getActivityId());
        checkoutCode.setActivityName(activity.getTitle());
        checkoutCode.setStudentId(user.getStudentId());
        checkoutCode.setStudentName(user.getName());
        checkoutCode.setTimestamp(currentTime);
        checkoutCode.setExpiresAt(expiresAt);
        checkoutCode.setIsValid(true);
        checkoutCode.setIsUsed(false);
        checkoutCode.setCreatedAt(LocalDateTime.now());
        
        checkoutCodeRepository.save(checkoutCode);
        
        logger.info("用户 {} 获取活动 {} 的签退码: {}", user.getName(), activity.getTitle(), uniqueCode);
        
        return new CheckinCodeResponse(
                uniqueCode,
                request.getActivityId(),
                activity.getTitle(),
                user.getStudentId(),
                user.getName(),
                currentTime,
                expiresAt,
                true
        );
    }
    
    /**
     * 验证签退码并完成签退
     */
    public CheckoutResponse validateCheckoutCode(ValidateCodeRequest request, String validatorUserId) {
        // 验证扫码者是否存在
        User validator = userRepository.findById(validatorUserId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 查找签退码记录
        Optional<ActivityCheckoutCode> optionalCheckoutCode = checkoutCodeRepository.findByUniqueCode(request.getUniqueCode());
        if (!optionalCheckoutCode.isPresent()) {
            throw new RuntimeException("签退码格式错误或不存在");
        }
        
        ActivityCheckoutCode checkoutCode = optionalCheckoutCode.get();
        
        // 验证签退码是否有效
        if (!checkoutCode.getIsValid()) {
            throw new RuntimeException("签退码无效");
        }
        
        // 验证签退码是否已使用
        if (checkoutCode.getIsUsed()) {
            throw new RuntimeException("签退码已被使用");
        }
        
        // 验证签退码是否过期
        long currentTime = System.currentTimeMillis();
        if (currentTime > checkoutCode.getExpiresAt()) {
            throw new RuntimeException("签退码已过期，请重新获取");
        }
        
        // 验证活动是否存在
        Activity activity = activityRepository.findById(checkoutCode.getActivityId())
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        
        // 验证活动是否可以签退（再次检查活动时间）
        if (!activityService.canCheckOut(checkoutCode.getActivityId())) {
            throw new RuntimeException("活动已截止，无法完成签退");
        }
        
        // 验证学生是否存在
        User student = userRepository.findByStudentId(checkoutCode.getStudentId())
                .orElseThrow(() -> new RuntimeException("学生不存在"));
        
        // 获取签到记录
        AttendanceRecord attendanceRecord = attendanceRecordRepository.findByActivityIdAndUserId(checkoutCode.getActivityId(), student.getId())
                .orElseThrow(() -> new RuntimeException("该学生尚未签到，无法签退"));
        
        // 检查是否已经签退
        if (attendanceRecord.getIsCheckedOut()) {
            throw new RuntimeException("该学生已经签退过了");
        }
        
        // 计算参与时长（分钟）
        LocalDateTime checkInTime = attendanceRecord.getCheckInTime();
        LocalDateTime checkOutTime = LocalDateTime.now();
        int duration = (int) ChronoUnit.MINUTES.between(checkInTime, checkOutTime);
        
        // 新的计分规则：一分钟一分
        int pointsEarned = duration; // 一分钟一分
        String calculationRule = String.format("参与时长%d分钟，获得积分 = %d分", 
            duration, pointsEarned);
        
        // 更新签到记录
        attendanceRecord.setCheckOutTime(checkOutTime);
        attendanceRecord.setCheckOutLocation(activity.getLocation());
        attendanceRecord.setCheckedOutBy(validatorUserId);
        attendanceRecord.setIsCheckedOut(true);
        attendanceRecord.setDuration(duration);
        attendanceRecord.setPointsEarned(pointsEarned);
        attendanceRecordRepository.save(attendanceRecord);
        
        // 更新用户积分
        student.setPeActivityPoints(student.getPeActivityPoints() + pointsEarned);
        student.setPoints(student.getPoints() + pointsEarned);
        student.setPointsLastUpdated(LocalDateTime.now());
        userRepository.save(student);
        
        // 创建积分记录
        PointsRecord pointsRecord = new PointsRecord();
        pointsRecord.setId(UUID.randomUUID().toString());
        pointsRecord.setUserId(student.getId());
        pointsRecord.setActivityId(checkoutCode.getActivityId());
        pointsRecord.setActivityName(activity.getTitle());
        pointsRecord.setActivityType("PE_ACTIVITY");
        pointsRecord.setPointsEarned(pointsEarned);
        pointsRecord.setEarnedReason("完成PE活动签到签退");
        pointsRecord.setCalculationRule(calculationRule);
        pointsRecord.setParticipationDuration(duration);
        pointsRecord.setEarnedAt(LocalDateTime.now());
        pointsRecordRepository.save(pointsRecord);
        
        // 标记签退码为已使用
        checkoutCode.setIsUsed(true);
        checkoutCode.setUsedAt(LocalDateTime.now());
        checkoutCode.setUsedBy(validatorUserId);
        checkoutCodeRepository.save(checkoutCode);
        
        // WebSocket推送积分更新消息给学生
        try {
            webSocketPushService.pushPointsUpdate(
                student.getId(),
                pointsEarned,
                student.getPoints(),
                student.getPeActivityPoints(),
                student.getMorningExercisePoints(),
                activity.getTitle(),
                "活动签退完成",
                calculationRule,
                duration
            );
        } catch (Exception e) {
            logger.error("WebSocket推送积分更新失败: {}", e.getMessage());
        }
        
        // WebSocket推送签退通知给活动创建者
        if (activity.getOrganizerId() != null) {
            try {
                webSocketPushService.pushCheckOutNotification(
                    activity.getOrganizerId(),
                    student.getName(),
                    activity.getTitle(),
                    pointsEarned
                );
            } catch (Exception e) {
                logger.error("WebSocket推送签退通知失败: {}", e.getMessage());
            }
        }
        
        logger.info("用户 {} 为学生 {} 完成活动 {} 的签退，获得{}积分", validator.getName(), student.getName(), activity.getTitle(), pointsEarned);
        
        String message = String.format("签退成功！%s 已完成 %s 签退", student.getName(), activity.getTitle());
        
        return new CheckoutResponse(
                message,
                pointsEarned,
                duration,
                calculationRule,
                student.getPoints(),
                student.getPeActivityPoints(),
                student.getMorningExercisePoints()
        );
    }
    
    /**
     * 生成唯一签退码
     */
    private String generateUniqueCheckoutCode(String activityId, String userId, long timestamp) {
        // 格式：ACT_CHECKOUT_[活动ID前8位]_[用户ID前8位]_[时间戳后8位]
        String activityPrefix = activityId.length() >= 8 ? activityId.substring(0, 8) : activityId;
        String userPrefix = userId.length() >= 8 ? userId.substring(0, 8) : userId;
        String timestampSuffix = String.valueOf(timestamp).substring(String.valueOf(timestamp).length() - 8);
        
        return String.format("ACT_CHECKOUT_%s_%s_%s", activityPrefix, userPrefix, timestampSuffix);
    }
} 