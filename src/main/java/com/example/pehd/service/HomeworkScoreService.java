package com.example.pehd.service;

import com.example.pehd.dto.*;
import com.example.pehd.entity.ExerciseType;
import com.example.pehd.entity.HomeworkScore;
import com.example.pehd.entity.User;
import com.example.pehd.entity.UserRole;
import com.example.pehd.repository.HomeworkScoreRepository;
import com.example.pehd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeworkScoreService {
    
    @Autowired
    private HomeworkScoreRepository homeworkScoreRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 上传课后作业成绩
     */
    @Transactional
    public HomeworkScoreDto uploadScore(UploadHomeworkScoreRequest request) {
        // 验证学生是否存在
        User user = userRepository.findByStudentId(request.getStudentId())
            .orElseThrow(() -> new RuntimeException("学生不存在"));
        
        // 创建作业成绩记录
        HomeworkScore score = new HomeworkScore();
        score.setStudentId(request.getStudentId());
        score.setExerciseType(request.getExerciseType());
        score.setCount(request.getCount());
        // 忽略前端传的timestamp，统一使用后端当前时间
        score.setTimestamp(LocalDateTime.now());
        
        score = homeworkScoreRepository.save(score);
        
        // 更新用户表中的总次数
        updateUserTotalCount(user, request.getExerciseType(), request.getCount());
        
        return convertToDto(score);
    }
    
    /**
     * 更新用户表中的总次数
     */
    private void updateUserTotalCount(User user, ExerciseType exerciseType, Integer count) {
        switch (exerciseType) {
            case SQUAT:
                user.setTotalSquat(user.getTotalSquat() + count);
                break;
            case SIT_UP:
                user.setTotalSitUp(user.getTotalSitUp() + count);
                break;
            case PUSH_UP:
                user.setTotalPushUp(user.getTotalPushUp() + count);
                break;
            case PULL_UP:
                user.setTotalPullUp(user.getTotalPullUp() + count);
                break;
            case JUMP_ROPE:
                user.setTotalJumpRope(user.getTotalJumpRope() + count);
                break;
        }
        userRepository.save(user);
    }
    
    /**
     * 获取我的作业成绩列表
     */
    public HomeworkScoreListResponse getMyScores(String studentId, ExerciseType exerciseType, 
                                                   int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HomeworkScore> scorePage;
        
        if (exerciseType != null) {
            scorePage = homeworkScoreRepository.findByStudentIdAndExerciseTypeOrderByTimestampDesc(
                studentId, exerciseType, pageable);
        } else {
            scorePage = homeworkScoreRepository.findByStudentIdOrderByTimestampDesc(
                studentId, pageable);
        }
        
        List<HomeworkScoreDto> scores = scorePage.getContent().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        
        return new HomeworkScoreListResponse(scorePage.getTotalElements(), scores);
    }
    
    /**
     * 获取我的作业成绩统计
     */
    public HomeworkScoreStatsDto getMyStats(String studentId) {
        // 获取用户信息（从users1表读取总次数）
        User user = userRepository.findByStudentId(studentId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 获取该学生的所有作业记录
        List<HomeworkScore> allScores = homeworkScoreRepository.findByStudentIdOrderByTimestampDesc(studentId);
        
        // 计算总提交次数
        int totalSubmissions = allScores.size();
        
        // 分别计算每个项目的统计数据（传入user对象以获取总次数）
        ExerciseStatsDto squat = calculateExerciseStats(studentId, ExerciseType.SQUAT, user.getTotalSquat());
        ExerciseStatsDto sitUp = calculateExerciseStats(studentId, ExerciseType.SIT_UP, user.getTotalSitUp());
        ExerciseStatsDto pushUp = calculateExerciseStats(studentId, ExerciseType.PUSH_UP, user.getTotalPushUp());
        ExerciseStatsDto pullUp = calculateExerciseStats(studentId, ExerciseType.PULL_UP, user.getTotalPullUp());
        ExerciseStatsDto jumpRope = calculateExerciseStats(studentId, ExerciseType.JUMP_ROPE, user.getTotalJumpRope());
        
        return new HomeworkScoreStatsDto(totalSubmissions, squat, sitUp, pushUp, pullUp, jumpRope);
    }
    
    /**
     * 计算单个项目的统计数据
     * @param totalCount 从users1表中读取的总次数
     */
    private ExerciseStatsDto calculateExerciseStats(String studentId, ExerciseType exerciseType, Integer totalCount) {
        List<HomeworkScore> scores = homeworkScoreRepository
            .findByStudentIdAndExerciseTypeOrderByTimestampDesc(studentId, exerciseType);
        
        if (scores.isEmpty()) {
            return new ExerciseStatsDto(0, 0, 0, null);
        }
        
        // count使用users1表中的总次数
        int count = totalCount != null ? totalCount : 0;
        int bestCount = scores.stream()
            .mapToInt(HomeworkScore::getCount)
            .max()
            .orElse(0);
        
        HomeworkScore lastScore = scores.get(0); // 已按时间降序排列
        int lastCount = lastScore.getCount();
        LocalDateTime lastTime = lastScore.getTimestamp();
        
        return new ExerciseStatsDto(count, bestCount, lastCount, lastTime);
    }
    
    /**
     * 获取最佳成绩
     */
    public BestScoreDto getBestScore(String studentId, ExerciseType exerciseType) {
        HomeworkScore bestScore = homeworkScoreRepository.findBestScore(studentId, exerciseType)
            .orElse(null);
        
        if (bestScore == null) {
            return null;
        }
        
        return new BestScoreDto(
            bestScore.getStudentId(),
            bestScore.getExerciseType(),
            bestScore.getCount(),
            bestScore.getTimestamp()
        );
    }
    
    /**
     * 获取班级所有人的课后作业次数（签到员权限）
     */
    public List<ClassHomeworkScoreDto> getClassHomeworkScores(String currentStudentId, LocalDate date) {
        // 获取当前用户信息
        User currentUser = userRepository.findByStudentId(currentStudentId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 验证是否为签到员（CHECKER或SUB_CHECKER）
        if (currentUser.getRole() != UserRole.CHECKER && currentUser.getRole() != UserRole.SUB_CHECKER) {
            throw new RuntimeException("权限不足，仅签到员可以查看");
        }
        
        // 获取班级课后作业记录
        List<HomeworkScore> scores;
        if (date != null) {
            scores = homeworkScoreRepository.findByClassNameAndDate(currentUser.getClassName(), date);
        } else {
            scores = homeworkScoreRepository.findByClassName(currentUser.getClassName());
        }
        
        // 转换为DTO
        List<ClassHomeworkScoreDto> result = new ArrayList<>();
        for (HomeworkScore score : scores) {
            User student = userRepository.findByStudentId(score.getStudentId())
                .orElse(null);
            
            if (student != null) {
                ClassHomeworkScoreDto dto = new ClassHomeworkScoreDto(
                    score.getId(),
                    score.getStudentId(),
                    student.getName(),
                    student.getClassName(),
                    score.getExerciseType(),
                    score.getCount(),
                    score.getTimestamp()
                );
                result.add(dto);
            }
        }
        
        return result;
    }
    
    /**
     * 将Entity转换为DTO
     */
    private HomeworkScoreDto convertToDto(HomeworkScore score) {
        return new HomeworkScoreDto(
            score.getId(),
            score.getStudentId(),
            score.getExerciseType(),
            score.getCount(),
            score.getTimestamp()
        );
    }
}

