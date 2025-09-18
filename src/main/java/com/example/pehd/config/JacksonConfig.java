package com.example.pehd.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
public class JacksonConfig {

    // 支持多种日期时间格式
    private static final List<DateTimeFormatter> DATE_TIME_FORMATTERS = Arrays.asList(
        // 24小时制英文格式（新增）
        DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss", Locale.US),
        DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm:ss", Locale.US),
        // 12小时制英文格式
        DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm:ss a", Locale.US),
        DateTimeFormatter.ofPattern("MMM d, yyyy h:mm:ss a", Locale.US),
        // 标准格式
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"),
        DateTimeFormatter.ISO_LOCAL_DATE_TIME
    );

    // 默认输出格式 - 使用ISO格式符合API文档
    private static final DateTimeFormatter OUTPUT_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        
        // 配置LocalDateTime的序列化器和反序列化器
        javaTimeModule.addSerializer(LocalDateTime.class, 
            new LocalDateTimeSerializer(OUTPUT_FORMATTER));
        javaTimeModule.addDeserializer(LocalDateTime.class, 
            new FlexibleLocalDateTimeDeserializer());

        return Jackson2ObjectMapperBuilder.json()
                .modules(javaTimeModule)
                .build();
    }

    /**
     * 灵活的LocalDateTime反序列化器，支持多种日期时间格式
     */
    public static class FlexibleLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        
        @Override
        public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            String dateString = parser.getValueAsString();
            
            if (dateString == null || dateString.trim().isEmpty()) {
                return null;
            }
            
            // 尝试使用不同的格式解析日期时间
            for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
                try {
                    return LocalDateTime.parse(dateString.trim(), formatter);
                } catch (DateTimeParseException e) {
                    // 继续尝试下一个格式
                }
            }
            
            // 如果所有格式都失败，抛出异常
            throw new RuntimeException("无法解析日期时间字符串: " + dateString + 
                ", 支持的格式包括: MMM dd, yyyy h:mm:ss a, yyyy-MM-dd HH:mm:ss 等");
        }
    }
}