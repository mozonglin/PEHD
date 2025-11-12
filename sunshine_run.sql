-- 阳光跑系统数据库脚本

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 在users1表中添加阳光跑统计字段
-- ----------------------------
ALTER TABLE `users1` 
ADD COLUMN `sunshine_total_runs` int(11) NOT NULL DEFAULT '0' COMMENT '阳光跑总次数' AFTER `morning_exercise_points`,
ADD COLUMN `sunshine_total_distance` double NOT NULL DEFAULT '0' COMMENT '阳光跑总距离（米）' AFTER `sunshine_total_runs`,
ADD COLUMN `sunshine_total_duration` bigint(20) NOT NULL DEFAULT '0' COMMENT '阳光跑总时长（毫秒）' AFTER `sunshine_total_distance`,
ADD COLUMN `sunshine_total_calories` int(11) NOT NULL DEFAULT '0' COMMENT '阳光跑总卡路里' AFTER `sunshine_total_duration`;

-- ----------------------------
-- 2. 创建阳光跑记录表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sunshine_run_records` (
  `id` varchar(36) NOT NULL COMMENT '记录ID',
  `user_id` varchar(36) NOT NULL COMMENT '用户ID',
  `user_name` varchar(50) NOT NULL COMMENT '用户姓名',
  `student_id` varchar(20) NOT NULL COMMENT '学号',
  `class_name` varchar(100) DEFAULT NULL COMMENT '班级名称',
  `start_time` bigint(20) NOT NULL COMMENT '开始时间戳（毫秒）',
  `end_time` bigint(20) NOT NULL COMMENT '结束时间戳（毫秒）',
  `total_distance` double NOT NULL COMMENT '总距离（米）',
  `total_duration` bigint(20) NOT NULL COMMENT '总时长（毫秒）',
  `avg_pace` double NOT NULL COMMENT '平均配速（分钟/公里）',
  `calories` int(11) NOT NULL COMMENT '消耗卡路里',
  `check_points_count` int(11) NOT NULL COMMENT '完成打卡点数量',
  `total_check_points` int(11) NOT NULL COMMENT '总打卡点数量',
  `path_points` text COMMENT '跑步轨迹点（JSON格式）',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_class_name` (`class_name`),
  KEY `idx_created_at` (`created_at`),
  CONSTRAINT `fk_sunshine_user` FOREIGN KEY (`user_id`) REFERENCES `users1` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='阳光跑记录表';

SET FOREIGN_KEY_CHECKS = 1;

