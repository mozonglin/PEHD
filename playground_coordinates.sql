-- 操场坐标表数据库脚本

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 创建操场坐标表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `playground_coordinates` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `school` varchar(100) NOT NULL COMMENT '学校名称',
  `playground_name` varchar(100) NOT NULL COMMENT '操场名称',
  `longitude1` double NOT NULL COMMENT '第一个打卡点经度',
  `latitude1` double NOT NULL COMMENT '第一个打卡点纬度',
  `longitude2` double NOT NULL COMMENT '第二个打卡点经度',
  `latitude2` double NOT NULL COMMENT '第二个打卡点纬度',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时���',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_school_playground` (`school`, `playground_name`) COMMENT '学校和操场组合唯一索引',
  KEY `idx_school` (`school`),
  KEY `idx_playground_name` (`playground_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操场坐标表';

-- ----------------------------
-- 插入示例数据（可选，仅用于测试）
-- ----------------------------
INSERT INTO `playground_coordinates` (`school`, `playground_name`, `longitude1`, `latitude1`, `longitude2`, `latitude2`) VALUES
('清华大学', '东操场', 116.326520, 40.003690, 116.327820, 40.003990),
('清华大学', '西操场', 116.324780, 40.002450, 116.325480, 40.002850),
('北京大学', '五四操场', 116.305560, 39.992220, 116.306660, 39.992620),
('北京大学', '第一体育场', 116.306890, 39.998760, 116.307890, 39.999060);

SET FOREIGN_KEY_CHECKS = 1;