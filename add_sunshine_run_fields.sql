-- 在user1表中添加阳光跑统计字段
ALTER TABLE `user1` 
ADD COLUMN `sunshine_total_runs` int(11) NOT NULL DEFAULT '0' COMMENT '阳光跑总次数' AFTER `morning_exercise_points`,
ADD COLUMN `sunshine_total_distance` double NOT NULL DEFAULT '0' COMMENT '阳光跑总距离（公里）' AFTER `sunshine_total_runs`,
ADD COLUMN `sunshine_total_duration` bigint(20) NOT NULL DEFAULT '0' COMMENT '阳光跑总时长（秒）' AFTER `sunshine_total_distance`,
ADD COLUMN `sunshine_total_calories` int(11) NOT NULL DEFAULT '0' COMMENT '阳光跑总卡路里' AFTER `sunshine_total_duration`;

