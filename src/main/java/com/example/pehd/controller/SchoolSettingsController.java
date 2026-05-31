package com.example.pehd.controller;

import com.example.pehd.dto.ApiResponse;
import com.example.pehd.entity.HomeworkExerciseStandard;
import com.example.pehd.entity.SchoolSettings;
import com.example.pehd.repository.HomeworkExerciseStandardRepository;
import com.example.pehd.repository.SchoolSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/school-settings")
@CrossOrigin(origins = "*")
public class SchoolSettingsController {

    @Autowired
    private SchoolSettingsRepository schoolSettingsRepository;

    @Autowired
    private HomeworkExerciseStandardRepository homeworkExerciseStandardRepository;

    @GetMapping("/sunshine-run-distance")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSunshineRunDistance(@RequestParam String school) {
        Optional<SchoolSettings> settings = schoolSettingsRepository.findBySchool(school);
        int distance = settings.map(SchoolSettings::getSunshineRunDistance).orElse(1600);
        Map<String, Object> data = Map.of("sunshineRunDistance", distance);
        return ResponseEntity.ok(ApiResponse.success("查询成功", data));
    }

    @GetMapping("/sunshine-run-settings")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSunshineRunSettings(
            @RequestParam String school,
            @RequestParam(required = false) String gender) {
        Optional<SchoolSettings> optSettings = schoolSettingsRepository.findBySchool(school);
        SchoolSettings settings = optSettings.orElse(null);

        Map<String, Object> data = new HashMap<>();

        int runsPerWeek = 3;
        int totalWeeks = 16;
        if (settings == null) {
            data.put("sunshineRunDistance", 1600);
            data.put("paceMin", 3.0);
            data.put("paceMax", 8.0);
        } else if ("女".equals(gender) || "female".equalsIgnoreCase(gender)) {
            data.put("sunshineRunDistance",
                    settings.getSunshineRunDistanceFemale() != null ? settings.getSunshineRunDistanceFemale() : settings.getSunshineRunDistance());
            data.put("paceMin", settings.getSunshineRunPaceMinFemale() != null ? settings.getSunshineRunPaceMinFemale() : 3.5);
            data.put("paceMax", settings.getSunshineRunPaceMaxFemale() != null ? settings.getSunshineRunPaceMaxFemale() : 8.0);
            if (settings.getSunshineRunRunsPerWeek() != null) runsPerWeek = settings.getSunshineRunRunsPerWeek();
            if (settings.getSunshineRunTotalWeeks() != null) totalWeeks = settings.getSunshineRunTotalWeeks();
        } else {
            data.put("sunshineRunDistance",
                    settings.getSunshineRunDistanceMale() != null ? settings.getSunshineRunDistanceMale() : settings.getSunshineRunDistance());
            data.put("paceMin", settings.getSunshineRunPaceMinMale() != null ? settings.getSunshineRunPaceMinMale() : 3.0);
            data.put("paceMax", settings.getSunshineRunPaceMaxMale() != null ? settings.getSunshineRunPaceMaxMale() : 7.0);
            if (settings.getSunshineRunRunsPerWeek() != null) runsPerWeek = settings.getSunshineRunRunsPerWeek();
            if (settings.getSunshineRunTotalWeeks() != null) totalWeeks = settings.getSunshineRunTotalWeeks();
        }

        data.put("runsPerWeek", runsPerWeek);
        data.put("totalWeeks", totalWeeks);
        data.put("gender", gender != null ? gender : "男");
        return ResponseEntity.ok(ApiResponse.success("查询成功", data));
    }

    @GetMapping("/homework-standards")
    public ResponseEntity<ApiResponse<List<HomeworkExerciseStandard>>> getHomeworkStandards(@RequestParam String school) {
        List<HomeworkExerciseStandard> standards = homeworkExerciseStandardRepository.findBySchool(school);
        return ResponseEntity.ok(ApiResponse.success("查询成功", standards));
    }

    @GetMapping("/homework-submission-settings")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getHomeworkSubmissionSettings(@RequestParam String school) {
        Optional<SchoolSettings> opt = schoolSettingsRepository.findBySchool(school);
        int weekly = opt.map(SchoolSettings::getHomeworkWeeklySubmissionsRequired).orElse(3);
        int weeks = opt.map(SchoolSettings::getHomeworkSubmissionSemesterWeeks).orElse(16);
        Map<String, Object> data = new HashMap<>();
        data.put("weeklySubmissionsRequired", weekly);
        data.put("submissionSemesterWeeks", weeks);
        return ResponseEntity.ok(ApiResponse.success("查询成功", data));
    }
}
