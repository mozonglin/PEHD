-- 课后作业成绩功能数据库脚本

-- 1. 创建课后作业成绩记录表
CREATE TABLE IF NOT EXISTS homework_scores (
    id VARCHAR(36) PRIMARY KEY COMMENT '记录ID',
    student_id VARCHAR(20) NOT NULL COMMENT '学号',
    exercise_type VARCHAR(20) NOT NULL COMMENT '项目类型：SQUAT/SIT_UP/PUSH_UP/PULL_UP/JUMP_ROPE',
    count INT NOT NULL COMMENT '次数',
    timestamp DATETIME NOT NULL COMMENT '提交时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_student_id (student_id),
    INDEX idx_exercise_type (exercise_type),
    INDEX idx_timestamp (timestamp),
    INDEX idx_student_exercise (student_id, exercise_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课后作业成绩记录表';

-- 2. 在users1表中添加总次数字段
ALTER TABLE users1 
ADD COLUMN total_squat INT NOT NULL DEFAULT 0 COMMENT '深蹲总次数',
ADD COLUMN total_sit_up INT NOT NULL DEFAULT 0 COMMENT '仰卧起坐总次数',
ADD COLUMN total_push_up INT NOT NULL DEFAULT 0 COMMENT '俯卧撑总次数',
ADD COLUMN total_pull_up INT NOT NULL DEFAULT 0 COMMENT '引体向上总次数',
ADD COLUMN total_jump_rope INT NOT NULL DEFAULT 0 COMMENT '跳绳总次数';

-- 3. 插入测试数据（可选）
-- INSERT INTO homework_scores (id, student_id, exercise_type, count, timestamp) VALUES
-- (UUID(), '2021001', 'SQUAT', 50, '2024-12-23 10:30:00'),
-- (UUID(), '2021001', 'SIT_UP', 60, '2024-12-23 14:20:00'),
-- (UUID(), '2021002', 'SQUAT', 48, '2024-12-23 09:15:00'),
-- (UUID(), '2021002', 'PUSH_UP', 40, '2024-12-22 16:10:00');

-- 4. 更新users1表的总次数（用于测试数据）
-- UPDATE users1 u
-- SET 
--     total_squat = (SELECT COALESCE(SUM(count), 0) FROM homework_scores WHERE student_id = u.student_id AND exercise_type = 'SQUAT'),
--     total_sit_up = (SELECT COALESCE(SUM(count), 0) FROM homework_scores WHERE student_id = u.student_id AND exercise_type = 'SIT_UP'),
--     total_push_up = (SELECT COALESCE(SUM(count), 0) FROM homework_scores WHERE student_id = u.student_id AND exercise_type = 'PUSH_UP'),
--     total_pull_up = (SELECT COALESCE(SUM(count), 0) FROM homework_scores WHERE student_id = u.student_id AND exercise_type = 'PULL_UP'),
--     total_jump_rope = (SELECT COALESCE(SUM(count), 0) FROM homework_scores WHERE student_id = u.student_id AND exercise_type = 'JUMP_ROPE');

