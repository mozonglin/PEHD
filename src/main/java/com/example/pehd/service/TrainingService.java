package com.example.pehd.service;

import com.example.pehd.entity.*;
import com.example.pehd.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrainingService {

    @Autowired private StudentFitnessProfileRepository profileRepository;
    @Autowired private TrainingTaskRepository taskRepository;
    @Autowired private TrainingTaskRecordRepository recordRepository;
    @Autowired private UserService userService;

    /** 获取或创建学生体能档案 */
    public StudentFitnessProfile getOrCreateProfile(User user) {
        return profileRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    StudentFitnessProfile p = new StudentFitnessProfile();
                    p.setUserId(user.getId());
                    p.setStudentId(user.getStudentId());
                    p.setRealName(user.getName());
                    p.setClassName(user.getClassName());
                    p.setDepartmentName(user.getCollege());
                    return profileRepository.save(p);
                });
    }

    /** 上传体测数据 */
    @Transactional
    public StudentFitnessProfile uploadTiceData(User user, Map<String, Object> data) {
        StudentFitnessProfile p = getOrCreateProfile(user);
        if (data.get("vitalCapacity") != null) p.setVitalCapacity(toInt(data.get("vitalCapacity")));
        if (data.get("sitAndReach") != null) p.setSitAndReach(toBigDecimal(data.get("sitAndReach")));
        if (data.get("standingLongJump") != null) p.setStandingLongJump(toBigDecimal(data.get("standingLongJump")));
        if (data.get("sitUps") != null) p.setSitUps(toInt(data.get("sitUps")));
        if (data.get("sprint50m") != null) p.setSprint50m(toBigDecimal(data.get("sprint50m")));
        if (data.get("longRun") != null) p.setLongRun(toInt(data.get("longRun")));
        if (data.get("pullUps") != null) p.setPullUps(toInt(data.get("pullUps")));
        if (data.get("bmi") != null) p.setBmi(toBigDecimal(data.get("bmi")));
        if (data.get("height") != null) p.setHeight(toBigDecimal(data.get("height")));
        if (data.get("weight") != null) p.setWeight(toBigDecimal(data.get("weight")));
        if (data.get("ticeTotalScore") != null) p.setTiceTotalScore(toBigDecimal(data.get("ticeTotalScore")));
        if (data.get("ticeGrade") != null) p.setTiceGrade(data.get("ticeGrade").toString());
        p.setTiceUpdatedAt(LocalDateTime.now());
        return profileRepository.save(p);
    }

    /** 获取学生的待完成训练任务 */
    public List<Map<String, Object>> getStudentActiveTasks(String userId) {
        List<TrainingTaskRecord> records = recordRepository.findActiveRecordsByUserId(userId);
        return records.stream().map(r -> {
            Map<String, Object> item = new LinkedHashMap<>();
            TrainingTask task = r.getTask();
            if (task == null) {
                task = taskRepository.findById(r.getTaskId()).orElse(null);
            }
            if (task == null) return null;
            item.put("taskId", task.getId());
            item.put("title", task.getTitle());
            item.put("description", task.getDescription());
            item.put("trainingType", task.getTrainingType().name());
            item.put("difficulty", task.getDifficulty().name());
            item.put("startDate", task.getStartDate().toString());
            item.put("endDate", task.getEndDate().toString());
            item.put("teacherName", task.getTeacherName());
            item.put("requirements", task.getRequirements());
            item.put("recordId", r.getId());
            item.put("status", r.getStatus().name());
            item.put("completionRate", r.getCompletionRate());
            item.put("startedAt", r.getStartedAt());
            item.put("completedAt", r.getCompletedAt());
            return item;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /** 学生更新任务完成状态 */
    @Transactional
    public TrainingTaskRecord updateTaskProgress(String userId, Long taskId, String completionData, BigDecimal completionRate) {
        TrainingTaskRecord record = recordRepository.findByTaskIdAndUserId(taskId, userId)
                .orElseThrow(() -> new RuntimeException("任务记录不存在"));
        if (record.getStartedAt() == null) {
            record.setStartedAt(LocalDateTime.now());
        }
        record.setCompletionData(completionData);
        record.setCompletionRate(completionRate);
        if (completionRate.compareTo(new BigDecimal("100")) >= 0) {
            record.setStatus(TrainingTaskRecord.RecordStatus.completed);
            record.setCompletedAt(LocalDateTime.now());
        } else {
            record.setStatus(TrainingTaskRecord.RecordStatus.in_progress);
        }
        return recordRepository.save(record);
    }

    // 工具方法
    private Integer toInt(Object v) {
        if (v instanceof Number) return ((Number) v).intValue();
        try { return Integer.parseInt(v.toString()); } catch (Exception e) { return null; }
    }
    private BigDecimal toBigDecimal(Object v) {
        if (v instanceof BigDecimal) return (BigDecimal) v;
        try { return new BigDecimal(v.toString()); } catch (Exception e) { return null; }
    }
}
