package com.example.pehd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableScheduling
@EnableAsync
public class SchedulingConfig {
    // 启用定时任务和异步处理
} 