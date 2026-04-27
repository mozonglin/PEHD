package com.example.pehd.controller;

import com.example.pehd.dto.ApiResponse;
import com.example.pehd.entity.SchoolSettings;
import com.example.pehd.repository.SchoolSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/school-settings")
@CrossOrigin(origins = "*")
public class SchoolSettingsController {

    @Autowired
    private SchoolSettingsRepository schoolSettingsRepository;

    @GetMapping("/sunshine-run-distance")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSunshineRunDistance(@RequestParam String school) {
        Optional<SchoolSettings> settings = schoolSettingsRepository.findBySchool(school);
        int distance = settings.map(SchoolSettings::getSunshineRunDistance).orElse(1600);
        Map<String, Object> data = Map.of("sunshineRunDistance", distance);
        return ResponseEntity.ok(ApiResponse.success("查询成功", data));
    }
}
