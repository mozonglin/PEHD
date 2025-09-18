package com.example.pehd;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DateTimeSerializationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLocalDateTimeDeserialization() throws Exception {
        // 测试不同格式的日期时间字符串解析
        String[] testDateStrings = {
            "\"Aug 1, 2025 7:51:00 PM\"",
            "\"Dec 25, 2024 10:30:15 AM\"", 
            "\"2024-12-25 10:30:15\"",
            "\"2024-12-25T10:30:15\""
        };

        for (String dateString : testDateStrings) {
            try {
                LocalDateTime result = objectMapper.readValue(dateString, LocalDateTime.class);
                assertNotNull(result);
                System.out.println("成功解析: " + dateString + " -> " + result);
            } catch (Exception e) {
                fail("无法解析日期字符串: " + dateString + ", 错误: " + e.getMessage());
            }
        }
    }

    @Test
    public void testLocalDateTimeSerialization() throws Exception {
        LocalDateTime now = LocalDateTime.of(2024, 12, 25, 15, 30, 45);
        String serialized = objectMapper.writeValueAsString(now);
        
        assertNotNull(serialized);
        System.out.println("序列化结果: " + serialized);
        
        // 验证能够往返序列化
        LocalDateTime deserialized = objectMapper.readValue(serialized, LocalDateTime.class);
        assertEquals(now, deserialized);
    }
}