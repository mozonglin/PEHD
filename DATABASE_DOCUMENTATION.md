# PE校园数据库开发文档

## 概述
PE校园数据库是一个体育活动管理系统，支持活动管理、用户签到签退、积分系统、早操考勤等功能。

## 数据库表结构

### 1. 用户相关表

#### users - 用户表
用户基础信息和权限管理
- `id`: 用户唯一标识
- `name`: 用户姓名
- `student_id`: 学号（唯一）
- `school/college`: 学校/学院信息
- `phone_number`: 手机号（唯一）
- `points`: 总积分
- `pe_activity_points`: PE活动积分
- `morning_exercise_points`: 早操积分
- `study_hours`: 学时
- `integrity_score`: 诚信度（默认100）
- `role`: 用户角色（STUDENT/CHECKER/SUB_CHECKER/ADMIN）
- `is_logged_in`: 登录状态

#### user_permissions - 用户权限设置表
管理用户各种权限设置
- `user_id`: 用户ID
- `camera_permission`: 相机权限
- `location_permission`: 位置权限
- `storage_permission`: 存储权限
- `notification_permission`: 通知权限
- `phone_permission`: 电话权限

#### user_points_summary - 用户积分汇总表
用户积分统计汇总
- `user_id`: 用户ID
- `total_points`: 总积分
- `pe_activity_points`: PE活动积分
- `morning_exercise_points`: 早操积分

### 2. 活动相关表

#### activities - 活动表
PE活动基础信息
- `id`: 活动ID
- `title`: 活动标题
- `description`: 活动描述
- `location`: 活动地点
- `max_participants`: 最大参与人数
- `current_participants`: 当前参与人数
- `registration_start_time/registration_end_time`: 报名时间段
- `activity_start_time/activity_end_time`: 活动时间段
- `organizer/organizer_id`: 组织者信息
- `category`: 活动类别
- `points`: 积分奖励
- `approval_status`: 审核状态
  - `PENDING`: 审核中
  - `APPROVED`: 已通过
  - `REJECTED`: 已拒绝
  - `DRAFT`: 草稿
- `reviewed_by`: 审核人ID
- `reviewed_at`: 审核时间
- `review_comment`: 审核意见

#### activity_registrations - 活动报名表
用户活动报名记录
- `user_id`: 用户ID
- `activity_id`: 活动ID
- `status`: 报名状态（REGISTERED/ATTENDED/ABSENT/CANCELLED）

### 3. 签到签退相关表

#### attendance_records - 签到记录表
PE活动签到签退记录
- `activity_id`: 活动ID
- `user_id`: 用户ID
- `check_in_time`: 签到时间
- `check_in_location`: 签到地点
- `checked_in_by`: 签到操作者ID
- `check_out_time`: 签退时间
- `check_out_location`: 签退地点
- `checked_out_by`: 签退操作者ID
- `is_checked_out`: 是否已签退
- `duration`: 参与时长（分钟）
- `points_earned`: 获得积分

#### activity_checkin_codes - 活动签到码表
活动签到二维码数据
- `unique_code`: 唯一签到码
- `activity_id`: 活动ID
- `student_id`: 学号
- `expires_at`: 过期时间戳
- `is_valid/is_used`: 有效性和使用状态

#### activity_checkout_codes - 活动签退码表
活动签退二维码数据（结构同签到码表）

### 4. 早操考勤相关表

#### morning_exercises - 早操考勤活动表
早操活动基础信息
- `id`: 早操活动ID
- `title`: 活动标题（默认"早操考勤"）
- `location`: 活动地点（默认"操场"）
- `date`: 日期
- `start_time/end_time`: 早操时间段
- `checked_in_count`: 签到人数
- `checked_out_count`: 签退人数
- `created_by`: 创建者ID

#### morning_exercise_attendance - 早操考勤记录表
早操签到签退记录
- `exercise_id`: 早操活动ID
- `student_id`: 学号
- `check_in_time`: 签到时间
- `checked_by/checked_by_name`: 签到员信息
- `check_out_time`: 签退时间
- `checked_out_by/checked_out_by_name`: 签退操作者信息
- `is_checked_out`: 是否已签退
- `points_earned`: 获得积分（早操固定1分）
- `qr_code_data`: 二维码数据

#### morning_exercise_checkout_codes - 早操签退码表
早操签退二维码数据

#### checker_authorizations - 签到员授权表
签到员权限管理
- `student_id`: 学号
- `role`: 角色（CHECKER/SUB_CHECKER）
- `authorized_by`: 授权人ID
- `exercise_id`: 早操活动ID
- `is_active`: 是否活跃

### 5. 积分系统相关表

#### points_records - 积分记录表
详细积分获得记录
- `user_id`: 用户ID
- `activity_id`: 活动ID
- `activity_type`: 活动类型
- `points_earned`: 获得积分
- `earned_reason`: 获得原因
- `calculation_rule`: 计算规则
- `participation_duration`: 参与时长（分钟）

#### point_exchanges - 积分兑换记录表
积分兑换学时记录
- `user_id`: 用户ID
- `type`: 兑换类型（PARTICIPATE_ACTIVITY/CREATE_ACTIVITY）
- `points`: 使用积分
- `study_hours`: 获得学时

### 6. 系统相关表

#### token_blacklist - JWT Token黑名单表
JWT令牌黑名单管理
- `token_jti`: Token JTI
- `user_id`: 用户ID
- `expires_at`: 过期时间

#### websocket_connections - WebSocket连接表
WebSocket连接管理
- `user_id`: 用户ID
- `connection_id`: 连接标识
- `connected_at`: 连接时间
- `last_ping_at`: 最后心跳时间
- `is_active`: 是否活跃

## 积分计算规则

### PE活动积分
- 计算公式：`积分 = floor(参与时长(分钟) / 60)`
- 即每参与1小时获得1积分

### 早操积分
- 计算规则：完成签到和签退获得固定1积分
- 只签到不签退不获得积分

## 数据库关系

### 主要关联关系
1. `users` ↔ `activities`（通过`activity_registrations`）
2. `users` ↔ `attendance_records`（PE活动签到）
3. `users` ↔ `morning_exercise_attendance`（早操签到）
4. `users` ↔ `points_records`（积分记录）
5. `activities` ↔ `activity_checkin_codes`（签到码）
6. `morning_exercises` ↔ `morning_exercise_attendance`（早操考勤）

### 权限体系
- **STUDENT**: 普通学生
- **CHECKER**: 签到员（可以进行签到签退操作）
- **SUB_CHECKER**: 二级管理员
- **ADMIN**: 系统管理员

## 开发建议

### 管理员前端主要功能
1. **用户管理**：查看用户列表、权限设置、积分统计
2. **活动管理**：创建/编辑活动、查看报名情况、签到统计
3. **早操管理**：创建早操、授权签到员、查看考勤记录
4. **积分管理**：查看积分记录、兑换记录、统计分析
5. **系统管理**：WebSocket连接监控、Token管理

### 常用查询场景
1. 用户积分排行榜
2. 活动参与统计
3. 早操出勤率统计

## 活动审核功能

### 审核状态说明
活动审核功能为"我发布的活动"添加了审核状态管理，确保活动内容的合规性。

#### 状态枚举
- **DRAFT**: 草稿状态，活动尚未提交审核
- **PENDING**: 审核中，已提交等待管理员审核
- **APPROVED**: 已通过，活动可正常使用（报名、签到等）
- **REJECTED**: 已拒绝，需要修改后重新提交审核

#### 状态转换流程
```
创建活动 → DRAFT (草稿) 或 PENDING (审核中)
    ↓
DRAFT → 编辑修改 → 提交审核 → PENDING
    ↓
PENDING → 管理员审核 → APPROVED (通过) 或 REJECTED (拒绝)
    ↓
REJECTED → 修改后重新提交 → PENDING
    ↓
APPROVED → 正常使用（报名、签到等）
```

### 业务规则
1. **用户权限**：只有活动创建者可以编辑自己的活动
2. **编辑限制**：只有 `DRAFT` 或 `REJECTED` 状态的活动可以编辑
3. **功能限制**：只有 `APPROVED` 状态的活动可以进行报名、签到签退操作
4. **显示规则**：
   - 普通用户只能看到 `APPROVED` 状态的活动
   - 创建者可以看到自己创建的所有状态的活动

### 数据库索引
- `idx_approval_status`: 审核状态索引，用于快速筛选不同状态的活动
- `idx_organizer_approval`: 组织者和审核状态复合索引，用于"我创建的活动"查询

### 注意事项
- 管理员审核功能在单独的管理员端实现，不在本后端系统中
- 审核状态的变更只能由管理员端进行，用户端只能创建、编辑和提交审核
- 为保证现有数据的兼容性，数据库更新脚本会将现有活动状态设置为 `APPROVED`
4. 签到员工作量统计
5. 积分获得趋势分析

## 注意事项
- 所有时间字段使用`timestamp`类型
- 用户ID和活动ID使用36位UUID
- 积分计算由后端完成，通过WebSocket实时推送
- 数据库使用utf8mb4字符集，支持emoji等特殊字符 