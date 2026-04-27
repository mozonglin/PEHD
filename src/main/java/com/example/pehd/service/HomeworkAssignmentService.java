package com.example.pehd.service;

import com.example.pehd.entity.HomeworkAssignment;
import com.example.pehd.entity.HomeworkSubmission;
import com.example.pehd.repository.HomeworkAssignmentRepository;
import com.example.pehd.repository.HomeworkSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class HomeworkAssignmentService {

    @Autowired
    private HomeworkAssignmentRepository homeworkAssignmentRepository;

    @Autowired
    private HomeworkSubmissionRepository homeworkSubmissionRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getMyAssignments(String studentId) {
        List<String> tempClassIds = jdbcTemplate.queryForList(
                "SELECT temp_class_id FROM temp_class_enrollments WHERE student_id = ?",
                String.class, studentId);

        if (tempClassIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (String tempClassId : tempClassIds) {
            List<HomeworkAssignment> assignments =
                    homeworkAssignmentRepository.findByTempClassIdAndStatus(tempClassId, "active");
            for (HomeworkAssignment a : assignments) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("id", a.getId());
                item.put("title", a.getTitle());
                item.put("exerciseType", a.getExerciseType());
                item.put("requiredCount", a.getRequiredCount());
                item.put("startTime", a.getStartTime());
                item.put("deadline", a.getDeadline());
                item.put("status", a.getStatus());

                Optional<HomeworkSubmission> sub =
                        homeworkSubmissionRepository.findByAssignmentIdAndStudentId(a.getId(), studentId);
                if (sub.isPresent()) {
                    HomeworkSubmission s = sub.get();
                    item.put("submitted", true);
                    item.put("completedCount", s.getCompletedCount());
                    item.put("submissionStatus", s.getStatus());
                    item.put("submittedAt", s.getSubmittedAt());
                } else {
                    item.put("submitted", false);
                    item.put("completedCount", 0);
                    item.put("submissionStatus", "pending");
                    item.put("submittedAt", null);
                }

                result.add(item);
            }
        }
        return result;
    }

    @Transactional
    public HomeworkSubmission submitAssignment(String assignmentId, String studentId, int completedCount) {
        HomeworkAssignment assignment = homeworkAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("作业不存在"));

        HomeworkSubmission submission = homeworkSubmissionRepository
                .findByAssignmentIdAndStudentId(assignmentId, studentId)
                .orElse(new HomeworkSubmission());

        if (submission.getId() == null) {
            submission.setAssignmentId(assignmentId);
            submission.setStudentId(studentId);
        }

        submission.setCompletedCount(completedCount);
        submission.setStatus("submitted");
        submission.setSubmittedAt(LocalDateTime.now());

        return homeworkSubmissionRepository.save(submission);
    }

    public HomeworkSubmission getMySubmission(String assignmentId, String studentId) {
        return homeworkSubmissionRepository.findByAssignmentIdAndStudentId(assignmentId, studentId)
                .orElse(null);
    }
}
