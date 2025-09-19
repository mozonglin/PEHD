package com.example.pehd.service;

import com.example.pehd.dto.*;
import com.example.pehd.entity.*;
import com.example.pehd.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class MorningExerciseService {
    
    private static final Logger logger = LoggerFactory.getLogger(MorningExerciseService.class);
    
    // 签到码和签退码有效期：40秒
    private static final long CODE_EXPIRE_SECONDS = 40;
    
    // 早操固定积分
    private static final int MORNING_EXERCISE_POINTS = 1;
    
    @Autowired
    private MorningExerciseRepository morningExerciseRepository;
    
    @Autowired
    private MorningExerciseAttendanceRepository attendanceRepository;
    
    @Autowired
    private MorningExerciseCheckinCodeRepository checkinCodeRepository;
    
    @Autowired
    private MorningExerciseCheckoutCodeRepository checkoutCodeRepository;
    
    @Autowired
    private CheckerAuthorizationRepository authorizationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PointsRecordRepository pointsRecordRepository;
    

    
    // 暂时不使用 WebSocket，根据用户要求
    // @Autowired
    // private WebSocketPushService webSocketPushService;
    
    /**
     * 获取当前进行中的早操考勤活动（按用户学院匹配）
     */
    public Optional<MorningExerciseDto> getCurrentMorningExercise(String userId) {
        // 获取用户信息
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        String college = user.getCollege();
        if (college == null || college.trim().isEmpty()) {
            throw new RuntimeException("用户学院信息缺失，无法查询早操活动");
        }
        
        LocalDateTime now = LocalDateTime.now();
        Optional<MorningExercise> currentExercise = morningExerciseRepository.findCurrentActiveExerciseByCollege(now, college);
        
        return currentExercise.map(this::convertToDto);
    }
    
    /**
     * 获取早操考勤活动列表
     */
    public List<MorningExerciseDto> getMorningExerciseList(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<MorningExercise> exercisePage = morningExerciseRepository.findAll(pageable);
        
        return exercisePage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * 创建早操考勤活动
     */
    public MorningExerciseDto createMorningExercise(CreateMorningExerciseRequest request, String createdBy) {
        // 验证创建者权限（需要是管理员）
        User creator = userRepository.findById(createdBy)
                .orElseThrow(() -> new RuntimeException("创建者不存在"));
        
        if (!UserRole.ADMIN.equals(creator.getRole())) {
            throw new RuntimeException("权限不足，仅管理员可以创建早操考勤活动");
        }
        
        // 检查时间冲突
        if (morningExerciseRepository.existsConflictingExercise(request.getDate(), request.getStartTime(), request.getEndTime())) {
            throw new RuntimeException("该时间段已存在其他早操考勤活动");
        }
        
        // 创建早操活动
        MorningExercise exercise = new MorningExercise();
        exercise.setId(UUID.randomUUID().toString());
        exercise.setTitle(request.getTitle());
        exercise.setDescription(request.getDescription());
        exercise.setLocation(request.getLocation());
        exercise.setDate(request.getDate());
        exercise.setStartTime(request.getStartTime());
        exercise.setEndTime(request.getEndTime());
        exercise.setType(MorningExercise.ExerciseType.MORNING_EXERCISE);
        exercise.setIsActive(true);
        exercise.setCreatedBy(createdBy);
        
        MorningExercise savedExercise = morningExerciseRepository.save(exercise);
        
        logger.info("早操考勤活动创建成功: {}", savedExercise.getId());
        return convertToDto(savedExercise);
    }
    
    /**
     * 请求早操签到码
     */
    public MorningExerciseCodeResponse requestCheckinCode(MorningExerciseRequest request, String userId) {
        // 1. 用户验证：检查用户是否已登录且为学生身份
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 2. 活动状态验证：早操必须存在且处于活跃状态
        MorningExercise exercise = morningExerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new RuntimeException("早操活动不存在"));
        
        if (!exercise.getIsActive()) {
            throw new RuntimeException("早操活动已结束");
        }
        
        // 3. 时间验证：当前时间必须在早操开始时间范围内
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(exercise.getStartTime())) {
            throw new RuntimeException("早操尚未开始");
        }
        if (now.isAfter(exercise.getEndTime())) {
            throw new RuntimeException("早操已结束");
        }
        
        // 检查是否已经签到
        if (attendanceRepository.existsByExerciseIdAndStudentId(request.getExerciseId(), user.getStudentId())) {
            throw new RuntimeException("您已经签到过了，无法重复获取签到码");
        }
        
        // 4. 生成唯一码：包含早操信息、用户信息和时间戳的唯一标识
        long currentTime = System.currentTimeMillis();
        long expiresAt = currentTime + (CODE_EXPIRE_SECONDS * 1000);
        String uniqueCode = generateCheckinUniqueCode(request.getExerciseId(), user.getStudentId(), currentTime);
        
        // 使现有的签到码失效
        checkinCodeRepository.invalidateCodesForStudent(request.getExerciseId(), user.getStudentId());
        
        // 5. 有效期设置：二维码有效期为40秒
        MorningExerciseCheckinCode checkinCode = new MorningExerciseCheckinCode(
                UUID.randomUUID().toString(),
                uniqueCode,
                request.getExerciseId(),
                request.getExerciseName(),
                user.getStudentId(),
                user.getName(),
                currentTime,
                expiresAt
        );
        
        checkinCodeRepository.save(checkinCode);
        
        logger.info("早操签到码生成成功: 学生 {} 申请早操 {} 的签到码", user.getStudentId(), request.getExerciseId());
        
        return new MorningExerciseCodeResponse(
                uniqueCode,
                request.getExerciseId(),
                request.getExerciseName(),
                user.getStudentId(),
                user.getName(),
                currentTime,
                expiresAt,
                true
        );
    }
    
    /**
     * 验证早操签到码
     */
    public void validateCheckinCode(ValidateCodeRequest request, String checkerId) {
        // 1. 权限验证：检查操作者是否为签到员(CHECKER)或二级管理员(SUB_CHECKER)
        User checker = userRepository.findById(checkerId)
                .orElseThrow(() -> new RuntimeException("操作者不存在"));
        
        // 2. 签到码验证
        MorningExerciseCheckinCode checkinCode = checkinCodeRepository.findByUniqueCode(request.getUniqueCode())
                .orElseThrow(() -> new RuntimeException("签到码格式错误"));
        
        // 验证签到码格式正确
        if (!checkinCode.getUniqueCode().startsWith("ME_CHECKIN_")) {
            throw new RuntimeException("签到码格式错误");
        }
        
        // 检查签到码是否在有效期内（40秒）
        if (checkinCode.isExpired()) {
            throw new RuntimeException("签到码已过期，请重新获取");
        }
        
        // 检查签到码是否已使用
        if (checkinCode.getIsUsed()) {
            throw new RuntimeException("签到码已被使用");
        }
        
        // 3. 活动状态验证
        MorningExercise exercise = morningExerciseRepository.findById(checkinCode.getExerciseId())
                .orElseThrow(() -> new RuntimeException("早操活动不存在或已结束"));
        
        if (!exercise.getIsActive()) {
            throw new RuntimeException("早操活动已结束");
        }
        
        // 当前时间必须在活动时间范围内
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(exercise.getStartTime()) || now.isAfter(exercise.getEndTime())) {
            throw new RuntimeException("早操活动不在有效时间范围内");
        }
        
        // 检查操作者权限
        if (!hasCheckinPermission(checker, checkinCode.getExerciseId())) {
            throw new RuntimeException("权限不足，仅签到员或二级管理员可以扫码");
        }
        
        // 4. 重复签到检查：防止同一学生重复签到
        if (attendanceRepository.existsByExerciseIdAndStudentId(checkinCode.getExerciseId(), checkinCode.getStudentId())) {
            throw new RuntimeException("该学生已经签到过了");
        }
        
        // 创建考勤记录
        MorningExerciseAttendance attendance = new MorningExerciseAttendance();
        attendance.setId(UUID.randomUUID().toString());
        attendance.setExerciseId(checkinCode.getExerciseId());
        attendance.setStudentId(checkinCode.getStudentId());
        attendance.setStudentName(checkinCode.getStudentName());
        attendance.setCheckInTime(now);
        attendance.setCheckInLocation(exercise.getLocation());
        attendance.setCheckedBy(checkerId);
        attendance.setCheckedByName(checker.getName());
        attendance.setQrCodeData(checkinCode.getUniqueCode());
        
        attendanceRepository.save(attendance);
        
        // 标记签到码为已使用
        checkinCode.markAsUsed(checkerId);
        checkinCodeRepository.save(checkinCode);
        
        // 更新早操的签到人数
        exercise.setCheckedInCount(exercise.getCheckedInCount() + 1);
        morningExerciseRepository.save(exercise);
        
        logger.info("早操签到成功: 学生 {} 在早操 {} 中签到，操作者: {}", 
                checkinCode.getStudentName(), exercise.getTitle(), checker.getName());
    }
    
    /**
     * 请求早操签退码
     */
    public MorningExerciseCodeResponse requestCheckoutCode(MorningExerciseRequest request, String userId) {
        // 1. 用户验证：检查用户是否已登录
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 2. 签到状态验证：用户必须已经完成早操签到
        MorningExerciseAttendance attendance = attendanceRepository.findByExerciseIdAndStudentId(
                request.getExerciseId(), user.getStudentId())
                .orElseThrow(() -> new RuntimeException("该学生尚未签到，无法签退"));
        
        if (attendance.getIsCheckedOut()) {
            throw new RuntimeException("该学生已经签退过了");
        }
        
        // 3. 活动状态验证：早操必须处于可签退状态（活动进行中或结束后30分钟内）
        MorningExercise exercise = morningExerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new RuntimeException("早操活动不存在"));
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime maxCheckoutTime = exercise.getEndTime().plusMinutes(30);
        
        if (now.isAfter(maxCheckoutTime)) {
            throw new RuntimeException("签退时间已过，无法签退");
        }
        
        // 4. 生成唯一码：包含早操信息、用户信息和时间戳的唯一标识
        long currentTime = System.currentTimeMillis();
        long expiresAt = currentTime + (CODE_EXPIRE_SECONDS * 1000);
        String uniqueCode = generateCheckoutUniqueCode(request.getExerciseId(), user.getStudentId(), currentTime);
        
        // 使现有的签退码失效
        checkoutCodeRepository.invalidateCodesForStudent(request.getExerciseId(), user.getStudentId());
        
        // 5. 有效期设置：二维码有效期为40秒
        MorningExerciseCheckoutCode checkoutCode = new MorningExerciseCheckoutCode(
                UUID.randomUUID().toString(),
                uniqueCode,
                request.getExerciseId(),
                request.getExerciseName(),
                user.getStudentId(),
                user.getName(),
                currentTime,
                expiresAt
        );
        
        checkoutCodeRepository.save(checkoutCode);
        
        logger.info("早操签退码生成成功: 学生 {} 申请早操 {} 的签退码", user.getStudentId(), request.getExerciseId());
        
        return new MorningExerciseCodeResponse(
                uniqueCode,
                request.getExerciseId(),
                request.getExerciseName(),
                user.getStudentId(),
                user.getName(),
                currentTime,
                expiresAt,
                true
        );
    }
    
    /**
     * 验证早操签退码
     */
    public MorningExerciseCheckoutResponse validateCheckoutCode(ValidateCodeRequest request, String checkerId) {
        // 1. 签退码验证
        MorningExerciseCheckoutCode checkoutCode = checkoutCodeRepository.findByUniqueCode(request.getUniqueCode())
                .orElseThrow(() -> new RuntimeException("签退码格式错误"));
        
        // 检查签退码格式正确
        if (!checkoutCode.getUniqueCode().startsWith("ME_CHECKOUT_")) {
            throw new RuntimeException("签退码格式错误");
        }
        
        // 验证签退码在有效期内（40秒）
        if (checkoutCode.isExpired()) {
            throw new RuntimeException("签退码已过期，请重新获取");
        }
        
        // 确认签退码未被使用过
        if (checkoutCode.getIsUsed()) {
            throw new RuntimeException("签退码已被使用");
        }
        
        // 2. 权限验证：检查扫码者是否为签到员(CHECKER)或二级管理员(SUB_CHECKER)
        User checker = userRepository.findById(checkerId)
                .orElseThrow(() -> new RuntimeException("操作者不存在"));
        
        if (!hasCheckinPermission(checker, checkoutCode.getExerciseId())) {
            throw new RuntimeException("权限不足，仅签到员或二级管理员可以扫码");
        }
        
        // 3. 活动状态验证：确认早操状态允许签退
        MorningExercise exercise = morningExerciseRepository.findById(checkoutCode.getExerciseId())
                .orElseThrow(() -> new RuntimeException("早操活动不存在"));
        
        // 4. 签到状态验证：确认该学生已完成签到
        MorningExerciseAttendance attendance = attendanceRepository.findByExerciseIdAndStudentId(
                checkoutCode.getExerciseId(), checkoutCode.getStudentId())
                .orElseThrow(() -> new RuntimeException("该学生尚未签到，无法签退"));
        
        // 5. 重复签退检查：防止同一学生重复签退
        if (attendance.getIsCheckedOut()) {
            throw new RuntimeException("该学生已经签退过了");
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        // 更新考勤记录
        attendance.setCheckOutTime(now);
        attendance.setCheckOutLocation(exercise.getLocation());
        attendance.setCheckedOutBy(checkerId);
        attendance.setCheckedOutByName(checker.getName());
        attendance.setIsCheckedOut(true);
        
        // 6. 后端积分计算：后端自动计算早操积分(固定1分)
        attendance.setPointsEarned(MORNING_EXERCISE_POINTS);
        
        attendanceRepository.save(attendance);
        
        // 标记签退码为已使用
        checkoutCode.markAsUsed(checkerId);
        checkoutCodeRepository.save(checkoutCode);
        
        // 更新早操的签退人数
        exercise.setCheckedOutCount(exercise.getCheckedOutCount() + 1);
        morningExerciseRepository.save(exercise);
        
        // 7. 积分发放：将积分发放到用户账户
        User student = userRepository.findByStudentId(checkoutCode.getStudentId())
                .orElseThrow(() -> new RuntimeException("学生不存在"));
        
        // 更新用户积分
        student.setMorningExercisePoints(student.getMorningExercisePoints() + MORNING_EXERCISE_POINTS);
        student.setPoints(student.getPoints() + MORNING_EXERCISE_POINTS);
        userRepository.save(student);
        
        // 记录积分获得记录
        PointsRecord pointsRecord = new PointsRecord();
        pointsRecord.setId(UUID.randomUUID().toString());
        pointsRecord.setUserId(student.getId());
        pointsRecord.setActivityId(checkoutCode.getExerciseId());
        pointsRecord.setActivityName(checkoutCode.getExerciseName());
        pointsRecord.setActivityType("MORNING_EXERCISE");
        pointsRecord.setPointsEarned(MORNING_EXERCISE_POINTS);
        pointsRecord.setEarnedReason("早操考勤完成");
        pointsRecord.setCalculationRule("早操完成签到签退固定获得1积分");
        pointsRecord.setParticipationDuration(calculateParticipationDuration(attendance.getCheckInTime(), now));
        pointsRecord.setEarnedAt(now);
        
        pointsRecordRepository.save(pointsRecord);
        
        logger.info("早操签退成功: 学生 {} 完成早操 {}，获得 {} 积分", 
                checkoutCode.getStudentName(), exercise.getTitle(), MORNING_EXERCISE_POINTS);
        
        // 8. WebSocket推送：签退成功后通过WebSocket推送积分更新
        // 暂时不实现 WebSocket 推送
        
        return new MorningExerciseCheckoutResponse(
                MORNING_EXERCISE_POINTS,
                student.getPoints(),
                "早操完成签到签退固定获得1积分",
                "早操考勤完成",
                calculateParticipationDuration(attendance.getCheckInTime(), now),
                now
        );
    }
    
    /**
     * 扫码授权二级管理员
     */
    public void scanAuthorize(String targetStudentId, String exerciseId, String authorizerId) {
        // 1. 权限验证：只有签到员(CHECKER)或管理员(ADMIN)可以授权
        User authorizer = userRepository.findById(authorizerId)
                .orElseThrow(() -> new RuntimeException("授权者不存在"));
        
        if (!hasAuthorizationPermission(authorizer, exerciseId)) {
            throw new RuntimeException("权限不足，仅签到员或管理员可以授权");
        }
        
        // 2. 目标用户验证：确认被授权用户存在且为学生身份
        User targetUser = userRepository.findByStudentId(targetStudentId)
                .orElseThrow(() -> new RuntimeException("目标学生不存在"));
        
        // 3. 活动验证：确认早操活动存在且有效
        MorningExercise exercise = morningExerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("早操活动不存在"));
        
        // 4. 重复授权检查：防止重复授权
        if (authorizationRepository.existsByStudentIdAndExerciseIdAndIsActive(targetStudentId, exerciseId, true)) {
            throw new RuntimeException("该学生已经被授权为二级管理员");
        }
        
        // 创建授权记录
        CheckerAuthorization authorization = new CheckerAuthorization();
        authorization.setId(UUID.randomUUID().toString());
        authorization.setStudentId(targetStudentId);
        authorization.setStudentName(targetUser.getName());
        authorization.setRole(CheckerAuthorization.CheckerRole.SUB_CHECKER);
        authorization.setAuthorizedBy(authorizerId);
        authorization.setAuthorizedByName(authorizer.getName());
        authorization.setExerciseId(exerciseId);
        authorization.setIsActive(true);
        
        authorizationRepository.save(authorization);
        
        logger.info("二级管理员授权成功: {} 授权 {} 为早操 {} 的二级管理员", 
                authorizer.getName(), targetUser.getName(), exercise.getTitle());
    }
    
    /**
     * 获取早操考勤权限
     */
    public List<CheckerAuthorization> getUserPermissions(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        return authorizationRepository.findByStudentIdOrderByAuthorizedAtDesc(user.getStudentId());
    }
    
    /**
     * 获取用户在早操中的角色
     */
    public Optional<String> getUserRoleInExercise(String exerciseId, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 检查用户的基础角色
        if (UserRole.ADMIN.equals(user.getRole())) {
            return Optional.of("ADMIN");
        }
        
        // 如果用户在系统中就是CHECKER，直接返回CHECKER
        if (UserRole.CHECKER.equals(user.getRole())) {
            return Optional.of("CHECKER");
        }
        
        // 检查用户在早操中的特殊授权角色（SUB_CHECKER等）
        Optional<CheckerAuthorization.CheckerRole> specialRole = authorizationRepository.findUserRoleInExercise(user.getStudentId(), exerciseId);
        if (specialRole.isPresent()) {
            return Optional.of(specialRole.get().toString());
        }
        
        // 默认返回STUDENT
        return Optional.of("STUDENT");
    }
    
    /**
     * 获取早操考勤记录
     */
    public List<MorningExerciseAttendanceDto> getAttendanceRecords(String exerciseId) {
        List<MorningExerciseAttendance> attendances = attendanceRepository.findByExerciseIdOrderByCheckInTimeDesc(exerciseId);
        
        return attendances.stream()
                .map(this::convertAttendanceToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取早操考勤统计
     */
    public MorningExerciseStatsDto getExerciseStats(String exerciseId) {
        MorningExercise exercise = morningExerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("早操活动不存在"));
        
        Long checkedInCount = attendanceRepository.countByExerciseId(exerciseId);
        Long checkedOutCount = attendanceRepository.countCheckedOutByExerciseId(exerciseId);
        
        // 计算完成率和准时率（简化计算）
        double completionRate = checkedInCount > 0 ? (checkedOutCount.doubleValue() / checkedInCount.doubleValue()) * 100 : 0;
        double onTimeRate = checkedInCount > 0 ? (checkedInCount.doubleValue() / exercise.getTotalParticipants()) * 100 : 0;
        
        return new MorningExerciseStatsDto(
                exerciseId,
                exercise.getTitle(),
                exercise.getTotalParticipants(),
                checkedInCount.intValue(),
                checkedOutCount.intValue(),
                completionRate,
                onTimeRate,
                checkedOutCount.intValue(), // 总积分奖励 = 签退人数 * 1分
                55, // 平均参与时长（简化为固定值）
                LocalDateTime.now()
        );
    }
    
    // 辅助方法
    
    private MorningExerciseDto convertToDto(MorningExercise exercise) {
        return new MorningExerciseDto(
                exercise.getId(),
                exercise.getTitle(),
                exercise.getDescription(),
                exercise.getLocation(),
                exercise.getDate(),
                exercise.getStartTime(),
                exercise.getEndTime(),
                exercise.getType().toString(),
                exercise.getIsActive(),
                exercise.getTotalParticipants(),
                exercise.getCheckedInCount(),
                exercise.getCheckedOutCount(),
                exercise.getCreatedBy(),
                exercise.getCreatedAt()
        );
    }
    
    private MorningExerciseAttendanceDto convertAttendanceToDto(MorningExerciseAttendance attendance) {
        return new MorningExerciseAttendanceDto(
                attendance.getId(),
                attendance.getExerciseId(),
                attendance.getStudentId(),
                attendance.getStudentName(),
                attendance.getCheckInTime(),
                attendance.getCheckInLocation(),
                attendance.getCheckedBy(),
                attendance.getCheckedByName(),
                attendance.getCheckOutTime(),
                attendance.getCheckOutLocation(),
                attendance.getCheckedOutBy(),
                attendance.getCheckedOutByName(),
                attendance.getIsCheckedOut(),
                attendance.getPointsEarned(),
                "早操完成签到签退固定获得1积分",
                attendance.getCheckOutTime()
        );
    }
    
    private String generateCheckinUniqueCode(String exerciseId, String studentId, long timestamp) {
        return "ME_CHECKIN_" + exerciseId.substring(exerciseId.length() - 8) + 
               "_" + studentId + "_" + timestamp % 100000;
    }
    
    private String generateCheckoutUniqueCode(String exerciseId, String studentId, long timestamp) {
        return "ME_CHECKOUT_" + exerciseId.substring(exerciseId.length() - 8) + 
               "_" + studentId + "_" + timestamp % 100000;
    }
    
    private boolean hasCheckinPermission(User user, String exerciseId) {
        // 系统管理员拥有所有权限
        if (UserRole.ADMIN.equals(user.getRole())) {
            return true;
        }
        
        // 系统级CHECKER拥有权限
        if (UserRole.CHECKER.equals(user.getRole())) {
            return true;
        }
        
        // 检查在早操中的特殊授权（SUB_CHECKER等）
        Optional<CheckerAuthorization.CheckerRole> specialRole = authorizationRepository.findUserRoleInExercise(user.getStudentId(), exerciseId);
        return specialRole.isPresent() && (CheckerAuthorization.CheckerRole.CHECKER.equals(specialRole.get()) || CheckerAuthorization.CheckerRole.SUB_CHECKER.equals(specialRole.get()));
    }
    
    private boolean hasAuthorizationPermission(User user, String exerciseId) {
        // 系统管理员拥有所有权限
        if (UserRole.ADMIN.equals(user.getRole())) {
            return true;
        }
        
        // 系统级CHECKER可以授权
        if (UserRole.CHECKER.equals(user.getRole())) {
            return true;
        }
        
        // 检查在早操中的特殊授权（只有CHECKER级别可以授权）
        Optional<CheckerAuthorization.CheckerRole> specialRole = authorizationRepository.findUserRoleInExercise(user.getStudentId(), exerciseId);
        return specialRole.isPresent() && CheckerAuthorization.CheckerRole.CHECKER.equals(specialRole.get());
    }
    
    private int calculateParticipationDuration(LocalDateTime checkInTime, LocalDateTime checkOutTime) {
        return (int) ChronoUnit.MINUTES.between(checkInTime, checkOutTime);
    }
}
