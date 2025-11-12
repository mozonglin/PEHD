package com.example.pehd.service;

import com.example.pehd.dto.*;
import com.example.pehd.entity.SunshineRunRecord;
import com.example.pehd.entity.User;
import com.example.pehd.repository.SunshineRunRepository;
import com.example.pehd.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 阳光跑服务
 */
@Service
@Transactional
public class SunshineRunService {
    
    private static final Logger logger = LoggerFactory.getLogger(SunshineRunService.class);
    
    @Autowired
    private SunshineRunRepository sunshineRunRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * 上传阳光跑记录
     */
    public SunshineRunRecordDto uploadRecord(String userId, SunshineRunUploadRequest request) {
        logger.info("用户 {} 上传阳光跑记录", userId);
        
        // 数据验证
        if (request.getStartTime() >= request.getEndTime()) {
            throw new RuntimeException("开始时间必须小于结束时间");
        }
        if (request.getCheckPointsCount() > request.getTotalCheckPoints()) {
            throw new RuntimeException("完成打卡点数量不能超过总打卡点数量");
        }
        
        // 获取用户信息
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 创建记录
        SunshineRunRecord record = new SunshineRunRecord();
        record.setId(UUID.randomUUID().toString());
        record.setUserId(userId);
        record.setUserName(user.getName());
        record.setStudentId(user.getStudentId());
        record.setClassName(user.getClassName());
        record.setStartTime(request.getStartTime());
        record.setEndTime(request.getEndTime());
        record.setTotalDistance(request.getTotalDistance());
        record.setTotalDuration(request.getTotalDuration());
        record.setAvgPace(request.getAvgPace());
        record.setCalories(request.getCalories());
        record.setCheckPointsCount(request.getCheckPointsCount());
        record.setTotalCheckPoints(request.getTotalCheckPoints());
        record.setCreatedAt(java.time.LocalDateTime.now());
        
        // 序列化路径点为JSON
        try {
            if (request.getPathPoints() != null && !request.getPathPoints().isEmpty()) {
                String pathPointsJson = objectMapper.writeValueAsString(request.getPathPoints());
                record.setPathPoints(pathPointsJson);
            }
        } catch (Exception e) {
            logger.error("序列化路径点失败", e);
        }
        
        sunshineRunRepository.save(record);
        
        // 更新用户统计
        user.setSunshineTotalRuns(user.getSunshineTotalRuns() + 1);
        user.setSunshineTotalDistance(user.getSunshineTotalDistance() + request.getTotalDistance());
        user.setSunshineTotalDuration(user.getSunshineTotalDuration() + request.getTotalDuration());
        user.setSunshineTotalCalories(user.getSunshineTotalCalories() + request.getCalories());
        userRepository.save(user);
        
        return convertToDto(record);
    }
    
    /**
     * 获取用户的阳光跑记录列表
     */
    public List<SunshineRunRecordDto> getMyRecords(String userId, int page, int size) {
        logger.info("获取用户 {} 的阳光跑记录", userId);
        
        Pageable pageable = PageRequest.of(page, size);
        List<SunshineRunRecord> records = sunshineRunRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        
        List<SunshineRunRecordDto> dtos = new ArrayList<>();
        for (SunshineRunRecord record : records) {
            dtos.add(convertToDto(record));
        }
        return dtos;
    }
    
    /**
     * 获取用户的阳光跑统计
     */
    public SunshineRunStatsDto getMyStats(String userId) {
        logger.info("获取用户 {} 的阳光跑统计", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        return new SunshineRunStatsDto(
            user.getSunshineTotalRuns(),
            user.getSunshineTotalDistance(),
            user.getSunshineTotalDuration(),
            user.getSunshineTotalCalories()
        );
    }
    
    /**
     * 获取班级阳光跑记录（班级管理员）
     */
    public List<SunshineRunRecordDto> getClassRecords(String className, int page, int size) {
        logger.info("获取班级 {} 的阳光跑记录", className);
        
        Pageable pageable = PageRequest.of(page, size);
        List<SunshineRunRecord> records = sunshineRunRepository.findByClassNameOrderByCreatedAtDesc(className, pageable);
        
        List<SunshineRunRecordDto> dtos = new ArrayList<>();
        for (SunshineRunRecord record : records) {
            dtos.add(convertToDto(record));
        }
        return dtos;
    }
    
    /**
     * 获取单条记录详情
     */
    public SunshineRunRecordDto getRecordById(String recordId, String userId) {
        logger.info("获取阳光跑记录详情: {}", recordId);
        
        SunshineRunRecord record = sunshineRunRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("记录不存在"));
        
        // 权限验证：只能查看自己的记录，或班级管理员查看本班学生记录
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        boolean isOwner = record.getUserId().equals(userId);
        boolean isClassChecker = (currentUser.getRole().name().equals("CHECKER") || 
                                 currentUser.getRole().name().equals("SUB_CHECKER")) &&
                                record.getClassName().equals(currentUser.getClassName());
        
        if (!isOwner && !isClassChecker) {
            throw new RuntimeException("权限不足，无法查看此记录");
        }
        
        return convertToDto(record);
    }
    
    /**
     * 删除阳光跑记录
     */
    public void deleteRecord(String recordId, String userId) {
        logger.info("删除阳光跑记录: {}", recordId);
        
        SunshineRunRecord record = sunshineRunRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("记录不存在"));
        
        // 权限验证：只能删除自己的记录
        if (!record.getUserId().equals(userId)) {
            throw new RuntimeException("权限不足，只能删除自己的记录");
        }
        
        // 更新用户统计
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        user.setSunshineTotalRuns(Math.max(0, user.getSunshineTotalRuns() - 1));
        user.setSunshineTotalDistance(Math.max(0.0, user.getSunshineTotalDistance() - record.getTotalDistance()));
        user.setSunshineTotalDuration(Math.max(0L, user.getSunshineTotalDuration() - record.getTotalDuration()));
        user.setSunshineTotalCalories(Math.max(0, user.getSunshineTotalCalories() - record.getCalories()));
        userRepository.save(user);
        
        sunshineRunRepository.delete(record);
    }
    
    /**
     * 获取班级排行榜
     */
    @SuppressWarnings("unchecked")
    public List<SunshineRunRankingDto> getClassRanking(String className) {
        logger.info("获取班级 {} 的排行榜", className);
        
        List<Object> results = sunshineRunRepository.findClassRanking(className);
        
        List<SunshineRunRankingDto> rankings = new ArrayList<>();
        int rank = 1;
        for (Object result : results) {
            Map<String, Object> map = (Map<String, Object>) result;
            
            SunshineRunRankingDto dto = new SunshineRunRankingDto(
                (String) map.get("userName"),
                (String) map.get("studentId"),
                (Double) map.get("totalDistance"),
                ((Long) map.get("totalRuns")).intValue(),
                (Long) map.get("totalDuration"),
                rank++
            );
            rankings.add(dto);
        }
        
        return rankings;
    }
    
    /**
     * 转换为DTO
     */
    private SunshineRunRecordDto convertToDto(SunshineRunRecord record) {
        SunshineRunRecordDto dto = new SunshineRunRecordDto();
        dto.setId(record.getId());
        dto.setUserName(record.getUserName());
        dto.setStudentId(record.getStudentId());
        dto.setStartTime(record.getStartTime());
        dto.setEndTime(record.getEndTime());
        dto.setTotalDistance(record.getTotalDistance());
        dto.setTotalDuration(record.getTotalDuration());
        dto.setAvgPace(record.getAvgPace());
        dto.setCalories(record.getCalories());
        dto.setCheckPointsCount(record.getCheckPointsCount());
        dto.setTotalCheckPoints(record.getTotalCheckPoints());
        dto.setCreatedAt(record.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return dto;
    }
}

