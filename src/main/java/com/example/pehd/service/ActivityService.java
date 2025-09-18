package com.example.pehd.service;

import com.example.pehd.dto.ActivityDto;
import com.example.pehd.dto.CreateActivityRequest;
import com.example.pehd.dto.UserDto;
import com.example.pehd.entity.Activity;
import com.example.pehd.entity.ActivityApprovalStatus;
import com.example.pehd.entity.ActivityRegistration;
import com.example.pehd.entity.User;
import com.example.pehd.repository.ActivityRepository;
import com.example.pehd.repository.ActivityRegistrationRepository;
import com.example.pehd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActivityService {
    
    @Autowired
    private ActivityRepository activityRepository;
    
    @Autowired
    private ActivityRegistrationRepository registrationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 获取活动列表
     */
    public List<ActivityDto> getActivities(String userId, int page, int limit, String category, String status) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        Page<Activity> activities;
        if (category != null && !category.isEmpty()) {
            // 获取所有符合分类的活动
            activities = activityRepository.findByCategoryContainingIgnoreCase(category, pageable);
        } else {
            // 获取所有活动
            activities = activityRepository.findAll(pageable);
        }
        
        return activities.stream()
                .filter(activity -> {
                    // 如果是活动创建者，显示所有状态的活动
                    if (activity.getOrganizerId().equals(userId)) {
                        return true;
                    }
                    // 如果不是创建者，只显示已审核通过的活动
                    return ActivityApprovalStatus.APPROVED.equals(activity.getApprovalStatus());
                })
                .map(activity -> convertToDto(activity, userId))
                .collect(Collectors.toList());
    }
    
    /**
     * 获取单个活动详情
     */
    public ActivityDto getActivityById(String activityId, String userId) {
        Optional<Activity> optionalActivity = activityRepository.findById(activityId);
        if (optionalActivity.isPresent()) {
            Activity activity = optionalActivity.get();
            
            // 如果不是活动创建者，只能查看已审核通过的活动
            if (!activity.getOrganizerId().equals(userId) && 
                !ActivityApprovalStatus.APPROVED.equals(activity.getApprovalStatus())) {
                throw new RuntimeException("活动不存在或未通过审核");
            }
            
            return convertToDto(activity, userId);
        }
        throw new RuntimeException("活动不存在");
    }
    
    /**
     * 创建活动
     */
    public ActivityDto createActivity(CreateActivityRequest request, String userId) {
        // 获取用户信息
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 验证时间逻辑
        validateActivityTimes(request);
        
        // 创建活动
        Activity activity = new Activity();
        activity.setId(UUID.randomUUID().toString());
        activity.setTitle(request.getTitle());
        activity.setDescription(request.getDescription());
        activity.setLocation(request.getLocation());
        activity.setMaxParticipants(request.getMaxParticipants());
        activity.setCurrentParticipants(0);
        activity.setRegistrationStartTime(request.getRegistrationStartTime());
        activity.setRegistrationEndTime(request.getRegistrationEndTime());
        activity.setActivityStartTime(request.getActivityStartTime());
        activity.setActivityEndTime(request.getActivityEndTime());
        activity.setOrganizer(user.getName());
        activity.setOrganizerId(userId);
        activity.setCategory(request.getCategory());
        activity.setPoints(request.getPoints());
        activity.setImageUrl(request.getImageUrl());
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());
        
        // 设置审核状态
        if (request.getIsDraft() != null && request.getIsDraft()) {
            activity.setApprovalStatus(ActivityApprovalStatus.DRAFT);
        } else {
            activity.setApprovalStatus(ActivityApprovalStatus.PENDING);
        }
        
        Activity savedActivity = activityRepository.save(activity);
        return convertToDto(savedActivity, userId);
    }
    
    /**
     * 更新活动（仅限草稿或被拒绝的活动）
     */
    public ActivityDto updateActivity(String activityId, CreateActivityRequest request, String userId) {
        // 检查活动是否存在
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        
        // 检查权限：只有活动创建者可以修改
        if (!activity.getOrganizerId().equals(userId)) {
            throw new RuntimeException("只有活动创建者可以修改活动");
        }
        
        // 检查活动状态：只有草稿或被拒绝的活动可以编辑
        if (!ActivityApprovalStatus.DRAFT.equals(activity.getApprovalStatus()) &&
            !ActivityApprovalStatus.REJECTED.equals(activity.getApprovalStatus())) {
            throw new RuntimeException("只有草稿或被拒绝的活动可以编辑");
        }
        
        // 验证时间逻辑
        validateActivityTimes(request);
        
        // 更新活动信息
        activity.setTitle(request.getTitle());
        activity.setDescription(request.getDescription());
        activity.setLocation(request.getLocation());
        activity.setMaxParticipants(request.getMaxParticipants());
        activity.setRegistrationStartTime(request.getRegistrationStartTime());
        activity.setRegistrationEndTime(request.getRegistrationEndTime());
        activity.setActivityStartTime(request.getActivityStartTime());
        activity.setActivityEndTime(request.getActivityEndTime());
        activity.setCategory(request.getCategory());
        activity.setPoints(request.getPoints());
        activity.setImageUrl(request.getImageUrl());
        activity.setUpdatedAt(LocalDateTime.now());
        
        // 更新审核状态
        if (request.getIsDraft() != null && request.getIsDraft()) {
            activity.setApprovalStatus(ActivityApprovalStatus.DRAFT);
        } else {
            activity.setApprovalStatus(ActivityApprovalStatus.PENDING);
            // 清除之前的审核信息
            activity.setReviewedBy(null);
            activity.setReviewedAt(null);
            activity.setReviewComment(null);
        }
        
        Activity savedActivity = activityRepository.save(activity);
        return convertToDto(savedActivity, userId);
    }
    
    /**
     * 报名活动
     */
    public void registerActivity(String activityId, String userId) {
        // 检查活动是否存在
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        
        // 检查活动审核状态
        if (!ActivityApprovalStatus.APPROVED.equals(activity.getApprovalStatus())) {
            throw new RuntimeException("活动未通过审核，无法报名");
        }
        
        // 检查用户是否存在
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 检查是否已报名
        if (registrationRepository.existsByUserIdAndActivityId(userId, activityId)) {
            throw new RuntimeException("您已经报名了此活动");
        }
        
        // 检查报名时间
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(activity.getRegistrationStartTime())) {
            throw new RuntimeException("报名尚未开始");
        }
        if (now.isAfter(activity.getRegistrationEndTime())) {
            throw new RuntimeException("报名已结束");
        }
        
        // 检查人数限制
        if (activity.getCurrentParticipants() >= activity.getMaxParticipants()) {
            throw new RuntimeException("活动人数已满");
        }
        
        // 创建报名记录
        ActivityRegistration registration = new ActivityRegistration();
        registration.setId(UUID.randomUUID().toString());
        registration.setUserId(userId);
        registration.setActivityId(activityId);
        registration.setStatus(ActivityRegistration.RegistrationStatus.REGISTERED);
        registration.setCreatedAt(LocalDateTime.now());
        
        registrationRepository.save(registration);
        
        // 更新活动参与人数
        activity.setCurrentParticipants(activity.getCurrentParticipants() + 1);
        activity.setUpdatedAt(LocalDateTime.now());
        activityRepository.save(activity);
    }
    
    /**
     * 取消报名
     */
    public void cancelRegistration(String activityId, String userId) {
        // 检查活动是否存在
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        
        // 检查活动审核状态
        if (!ActivityApprovalStatus.APPROVED.equals(activity.getApprovalStatus())) {
            throw new RuntimeException("活动未通过审核，无法取消报名");
        }
        
        // 检查报名记录
        ActivityRegistration registration = registrationRepository.findByUserIdAndActivityId(userId, activityId)
                .orElseThrow(() -> new RuntimeException("您未报名此活动"));
        
        // 检查是否可以取消报名
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(activity.getActivityStartTime())) {
            throw new RuntimeException("活动已开始，无法取消报名");
        }
        
        // 删除报名记录
        registrationRepository.delete(registration);
        
        // 更新活动参与人数
        activity.setCurrentParticipants(activity.getCurrentParticipants() - 1);
        activity.setUpdatedAt(LocalDateTime.now());
        activityRepository.save(activity);
    }
    
    /**
     * 获取我的活动
     */
    public List<ActivityDto> getMyActivities(String userId, String type) {
        List<Activity> activities;
        
        switch (type) {
            case "registered":
                // 获取已报名的活动（只显示已审核通过的）
                List<String> registeredActivityIds = registrationRepository.findByUserId(userId)
                        .stream()
                        .map(ActivityRegistration::getActivityId)
                        .collect(Collectors.toList());
                activities = activityRepository.findByIdInAndApprovalStatus(registeredActivityIds, ActivityApprovalStatus.APPROVED);
                break;
            case "created":
                // 获取我创建的活动（显示所有状态）
                activities = activityRepository.findByOrganizerId(userId);
                break;
            case "participated":
                // 获取已参与的活动（已签到，只显示已审核通过的）
                List<String> participatedActivityIds = registrationRepository.findByUserIdAndStatus(userId, ActivityRegistration.RegistrationStatus.ATTENDED)
                        .stream()
                        .map(ActivityRegistration::getActivityId)
                        .collect(Collectors.toList());
                activities = activityRepository.findByIdInAndApprovalStatus(participatedActivityIds, ActivityApprovalStatus.APPROVED);
                break;
            default:
                activities = List.of();
        }
        
        return activities.stream()
                .map(activity -> convertToDto(activity, userId))
                .collect(Collectors.toList());
    }
    
    /**
     * 获取活动参与者列表
     */
    public List<UserDto> getActivityParticipants(String activityId, String userId) {
        // 检查活动是否存在
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        
        // 检查权限：只有活动创建者可以查看
        if (!activity.getOrganizerId().equals(userId)) {
            throw new RuntimeException("您没有权限查看此活动的参与者");
        }
        
        // 获取参与者列表
        List<String> participantIds = registrationRepository.findByActivityId(activityId)
                .stream()
                .map(ActivityRegistration::getUserId)
                .collect(Collectors.toList());
        
        return userRepository.findByIdIn(participantIds)
                .stream()
                .map(this::convertUserToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * 移除活动参与者
     */
    public void removeParticipant(String activityId, String participantId, String userId) {
        // 检查活动是否存在
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        
        // 检查权限：只有活动创建者可以移除
        if (!activity.getOrganizerId().equals(userId)) {
            throw new RuntimeException("您没有权限执行此操作");
        }
        
        // 检查参与者是否存在
        ActivityRegistration registration = registrationRepository.findByUserIdAndActivityId(participantId, activityId)
                .orElseThrow(() -> new RuntimeException("参与者不存在"));
        
        // 删除报名记录
        registrationRepository.delete(registration);
        
        // 更新活动参与人数
        activity.setCurrentParticipants(activity.getCurrentParticipants() - 1);
        activity.setUpdatedAt(LocalDateTime.now());
        activityRepository.save(activity);
    }
    
    /**
     * 检查用户是否已报名活动
     */
    public boolean isUserRegistered(String userId, String activityId) {
        return registrationRepository.existsByUserIdAndActivityId(userId, activityId);
    }
    
    /**
     * 检查活动是否可以签到
     */
    public boolean canCheckIn(String activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        
        // 检查活动审核状态
        if (!ActivityApprovalStatus.APPROVED.equals(activity.getApprovalStatus())) {
            throw new RuntimeException("活动未通过审核，无法签到");
        }
        
        LocalDateTime now = LocalDateTime.now();
        // 活动开始时间到结束后30分钟内可以签到
        return now.isAfter(activity.getActivityStartTime()) && 
               now.isBefore(activity.getActivityEndTime().plusMinutes(30));
    }
    
    /**
     * 检查活动是否可以签退
     */
    public boolean canCheckOut(String activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        
        // 检查活动审核状态
        if (!ActivityApprovalStatus.APPROVED.equals(activity.getApprovalStatus())) {
            throw new RuntimeException("活动未通过审核，无法签退");
        }
        
        LocalDateTime now = LocalDateTime.now();
        // 活动开始时间到活动截止后30分钟内可以签退
        return now.isAfter(activity.getActivityStartTime()) && 
               now.isBefore(activity.getActivityEndTime().plusMinutes(30));
    }
    
    /**
     * 转换Activity实体为DTO
     */
    private ActivityDto convertToDto(Activity activity, String userId) {
        ActivityDto dto = new ActivityDto();
        dto.setId(activity.getId());
        dto.setTitle(activity.getTitle());
        dto.setDescription(activity.getDescription());
        dto.setLocation(activity.getLocation());
        dto.setMaxParticipants(activity.getMaxParticipants());
        dto.setCurrentParticipants(activity.getCurrentParticipants());
        dto.setRegistrationStartTime(activity.getRegistrationStartTime());
        dto.setRegistrationEndTime(activity.getRegistrationEndTime());
        dto.setActivityStartTime(activity.getActivityStartTime());
        dto.setActivityEndTime(activity.getActivityEndTime());
        dto.setOrganizer(activity.getOrganizer());
        dto.setOrganizerId(activity.getOrganizerId());
        dto.setCategory(activity.getCategory());
        dto.setPoints(activity.getPoints());
        dto.setImageUrl(activity.getImageUrl());
        dto.setCreatedAt(activity.getCreatedAt());
        dto.setUpdatedAt(activity.getUpdatedAt());
        
        // 设置审核状态相关字段
        dto.setApprovalStatus(activity.getApprovalStatus());
        dto.setReviewedBy(activity.getReviewedBy());
        dto.setReviewedAt(activity.getReviewedAt());
        dto.setReviewComment(activity.getReviewComment());
        
        // 检查用户是否已报名
        if (userId != null) {
            dto.setIsRegistered(isUserRegistered(userId, activity.getId()));
        }
        
        return dto;
    }
    
    /**
     * 转换User实体为DTO
     */
    private UserDto convertUserToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setStudentId(user.getStudentId());
        dto.setSchool(user.getSchool());
        dto.setCollege(user.getCollege());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAvatar(user.getAvatar());
        dto.setPoints(user.getPoints());
        dto.setPeActivityPoints(user.getPeActivityPoints());
        dto.setMorningExercisePoints(user.getMorningExercisePoints());
        dto.setStudyHours(user.getStudyHours());
        dto.setIntegrityScore(user.getIntegrityScore());
        dto.setRole(user.getRole().name());
        dto.setIsLoggedIn(user.getIsLoggedIn());
        dto.setPointsLastUpdated(user.getPointsLastUpdated());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
    
    /**
     * 验证活动时间逻辑
     */
    private void validateActivityTimes(CreateActivityRequest request) {
        LocalDateTime now = LocalDateTime.now();
        
        // 报名开始时间不能早于当前时间
        if (request.getRegistrationStartTime().isBefore(now)) {
            throw new RuntimeException("报名开始时间不能早于当前时间");
        }
        
        // 报名结束时间必须晚于开始时间
        if (request.getRegistrationEndTime().isBefore(request.getRegistrationStartTime())) {
            throw new RuntimeException("报名结束时间必须晚于开始时间");
        }
        
        // 活动开始时间必须晚于报名开始时间
        if (request.getActivityStartTime().isBefore(request.getRegistrationStartTime())) {
            throw new RuntimeException("活动开始时间必须晚于报名开始时间");
        }
        
        // 活动结束时间必须晚于开始时间
        if (request.getActivityEndTime().isBefore(request.getActivityStartTime())) {
            throw new RuntimeException("活动结束时间必须晚于开始时间");
        }
    }
} 