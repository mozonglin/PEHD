-- 更新user1表，添加班级和阳光跑相关字段

-- 添加班级字段（如果不存在）
ALTER TABLE `user1` 
ADD COLUMN IF NOT EXISTS `class_name` varchar(100) DEFAULT NULL COMMENT '班级名称' AFTER `college`;

-- 添加阳光跑统计字段
ALTER TABLE `user1` 
ADD COLUMN IF NOT EXISTS `sunshine_total_runs` int(11) NOT NULL DEFAULT '0' COMMENT '阳光跑总次数' AFTER `morning_exercise_points`,
ADD COLUMN IF NOT EXISTS `sunshine_total_distance` double NOT NULL DEFAULT '0' COMMENT '阳光跑总距离（公里）' AFTER `sunshine_total_runs`,
ADD COLUMN IF NOT EXISTS `sunshine_total_duration` bigint(20) NOT NULL DEFAULT '0' COMMENT '阳光跑总时长（秒）' AFTER `sunshine_total_distance`,
ADD COLUMN IF NOT EXISTS `sunshine_total_calories` int(11) NOT NULL DEFAULT '0' COMMENT '阳光跑总卡路里' AFTER `sunshine_total_duration`;

-- 添加索引
ALTER TABLE `user1` ADD INDEX IF NOT EXISTS `idx_class_name` (`class_name`);

