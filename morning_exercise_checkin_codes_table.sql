-- 早操签到码表创建脚本
-- 数据库：pe
-- 用途：创建早操考勤系统中缺失的签到码表

USE pe;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 早操签到码表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `morning_exercise_checkin_codes` (
  `id` varchar(36) NOT NULL COMMENT '签到码ID',
  `unique_code` varchar(100) NOT NULL COMMENT '唯一码',
  `exercise_id` varchar(36) NOT NULL COMMENT '早操ID',
  `exercise_name` varchar(200) NOT NULL COMMENT '早操名称',
  `student_id` varchar(20) NOT NULL COMMENT '学号',
  `student_name` varchar(50) NOT NULL COMMENT '学生姓名',
  `timestamp` bigint(20) NOT NULL COMMENT '时间戳',
  `expires_at` bigint(20) NOT NULL COMMENT '过期时间戳',
  `is_valid` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效',
  `is_used` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已使用',
  `used_at` timestamp NULL DEFAULT NULL COMMENT '使用时间',
  `used_by` varchar(36) DEFAULT NULL COMMENT '使用者ID',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_unique_code` (`unique_code`),
  KEY `idx_exercise_id` (`exercise_id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_expires_at` (`expires_at`),
  KEY `idx_valid_used` (`is_valid`, `is_used`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='早操签到码表';

-- 验证表结构
DESCRIBE morning_exercise_checkin_codes;

-- 显示创建成功信息
SELECT '早操签到码表创建完成！' as message;
SELECT 'morning_exercise_checkin_codes 表已成功创建，支持40秒过期机制' as details;

SET FOREIGN_KEY_CHECKS = 1;


