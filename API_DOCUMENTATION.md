# PE校园 API文档

## 概述

PE校园是一个体育活动管理应用，本文档描述了后端API接口规范。

### 基础信息

- **基础URL**: `https://api.pe-campus.com/v1`
- **认证方式**: JWT Bearer Token
- **内容类型**: `application/json`
- **字符编码**: UTF-8

### 通用响应格式

所有API接口都返回统一的响应格式：

```json
{
  "success": boolean,
  "message": "响应消息",
  "data": {} // 具体数据，可选
}
```

### 错误码

| 状态码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未授权或Token过期 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 认证相关API

### 1. 用户登录

**接口**: `POST /auth/login`

**描述**: 用户通过姓名、学号、手机号和验证码登录

**请求体**:
```json
{
  "name": "张三",
  "studentId": "2021001",
  "phoneNumber": "13800138000",
  "verificationCode": "123456"
}
```

**响应**:
```json
{
  "success": true,
  "message": "登录成功",
  "data": {
    "user": {
      "id": "user_123",
      "name": "张三",
      "studentId": "2021001",
      "school": "某大学",
      "college": "计算机学院",
      "phoneNumber": "13800138000",
      "avatar": "",
      "points": 100,
      "peActivityPoints": 70,
      "morningExercisePoints": 30,
      "integrityScore": 95,
      "isLoggedIn": true,
      "role": "STUDENT",
      "pointsLastUpdated": "2024-01-15T10:30:00Z"
    },
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...",
    "expiresIn": 3600
  }
}
```

### 2. 用户注册

**接口**: `POST /auth/register`

**描述**: 新用户注册

**请求体**:
```json
{
  "name": "李四",
  "studentId": "2021002",
  "phoneNumber": "13900139000",
  "verificationCode": "123456",
  "school": "某大学",
  "college": "体育学院"
}
```

**响应**: 同登录接口

### 3. 发送验证码

**接口**: `POST /auth/send-code`

**描述**: 发送短信验证码

**请求体**:
```json
{
  "phoneNumber": "13800138000",
  "type": "login"
}
```

**type值说明**:
- `login`: 登录验证码
- `register`: 注册验证码
- `change_phone`: 修改手机号验证码

**响应**:
```json
{
  "success": true,
  "message": "验证码已发送"
}
```

### 4. 刷新Token

**接口**: `POST /auth/refresh`

**描述**: 使用刷新Token获取新的访问Token

**请求头**: 无需Authorization

**请求体**:
```json
{
  "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4..."
}
```

**响应**: 同登录接口

### 5. 用户登出

**接口**: `POST /auth/logout`

**描述**: 用户登出，使Token失效

**请求头**: `Authorization: Bearer {accessToken}`

**响应**:
```json
{
  "success": true,
  "message": "登出成功"
}
```

### 6. 验证Token

**接口**: `POST /auth/verify-token`

**描述**: 验证当前Token是否有效

**请求头**: `Authorization: Bearer {accessToken}`

**响应**:
```json
{
  "success": true,
  "message": "Token有效",
  "data": {
    // 用户信息对象
  }
}
```

---

## 用户相关API

### 1. 获取用户资料

**接口**: `GET /user/profile`

**描述**: 获取当前用户的详细资料

**请求头**: `Authorization: Bearer {accessToken}`

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    // 用户信息对象
  }
}
```

### 2. 更新用户资料

**接口**: `PUT /user/profile`

**描述**: 更新用户资料信息

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "name": "张三",
  "school": "某大学",
  "college": "计算机学院",
  "avatar": "头像URL"
}
```

**响应**:
```json
{
  "success": true,
  "message": "更新成功",
  "data": {
    // 更新后的用户信息
  }
}
```

### 3. 修改密码

**接口**: `PUT /user/password`

**描述**: 修改用户密码

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "oldPassword": "旧密码",
  "newPassword": "新密码",
  "confirmPassword": "确认新密码"
}
```

**响应**:
```json
{
  "success": true,
  "message": "密码修改成功"
}
```

### 4. 修改手机号

**接口**: `PUT /user/phone`

**描述**: 修改绑定的手机号

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "newPhoneNumber": "13700137000",
  "verificationCode": "123456"
}
```

**响应**:
```json
{
  "success": true,
  "message": "手机号修改成功"
}
```

---

## PE活动相关API

### 1. 获取活动列表

**接口**: `GET /activities`

**描述**: 获取所有PE活动列表

**请求参数**:
- `page`: 页码（可选，默认1）
- `limit`: 每页数量（可选，默认10）
- `category`: 活动类别（可选）
- `status`: 活动状态（可选）

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": [
    {
      "id": "activity_123",
      "title": "篮球比赛",
      "description": "校内篮球比赛，欢迎参加",
      "location": "体育馆",
      "maxParticipants": 20,
      "currentParticipants": 15,
      "registrationStartTime": "2024-01-01T09:00:00Z",
      "registrationEndTime": "2024-01-05T18:00:00Z",
      "activityStartTime": "2024-01-06T14:00:00Z",
      "activityEndTime": "2024-01-06T16:00:00Z",
      "organizer": "体育部",
      "organizerId": "org_456",
      "isRegistered": false,
      "category": "球类运动",
      "points": 10,
      "imageUrl": "活动图片URL"
    }
  ]
}
```

### 2. 获取单个活动详情

**接口**: `GET /activities/{id}`

**描述**: 获取指定活动的详细信息

**路径参数**:
- `id`: 活动ID

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    // 活动详情对象
  }
}
```

### 3. 创建活动

**接口**: `POST /activities`

**描述**: 创建新的PE活动

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "title": "跑步活动",
  "description": "晨跑活动，强身健体",
  "location": "操场",
  "maxParticipants": 50,
  "registrationStartTime": "2024-01-01T09:00:00Z",
  "registrationEndTime": "2024-01-05T18:00:00Z",
  "activityStartTime": "2024-01-06T06:30:00Z",
  "activityEndTime": "2024-01-06T07:30:00Z",
  "category": "跑步",
  "points": 10
}
```

**响应**:
```json
{
  "success": true,
  "message": "活动创建成功",
  "data": {
    // 创建的活动对象
  }
}
```

### 4. 报名活动

**接口**: `POST /activities/{id}/register`

**描述**: 报名参加指定活动

**请求头**: `Authorization: Bearer {accessToken}`

**路径参数**:
- `id`: 活动ID

**响应**:
```json
{
  "success": true,
  "message": "报名成功"
}
```

### 5. 取消报名

**接口**: `DELETE /activities/{id}/register`

**描述**: 取消报名指定活动

**请求头**: `Authorization: Bearer {accessToken}`

**路径参数**:
- `id`: 活动ID

**响应**:
```json
{
  "success": true,
  "message": "取消报名成功"
}
```

### 6. 获取我的活动

**接口**: `GET /activities/my-activities`

**描述**: 获取当前用户参与的活动列表

**请求头**: `Authorization: Bearer {accessToken}`

**请求参数**:
- `type`: 活动类型（可选）
  - `registered`: 已报名的活动
  - `created`: 我创建的活动
  - `participated`: 已参与的活动

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": [
    // 活动列表
  ]
}
```

### 7. 获取活动参与者列表

**接口**: `GET /activities/{id}/participants`

**描述**: 获取指定活动的参与者列表（仅活动创建者可访问）

**请求头**: `Authorization: Bearer {accessToken}`

**路径参数**:
- `id`: 活动ID

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": [
    {
      "id": "user_123",
      "name": "张三",
      "studentId": "2021001",
      "school": "某大学",
      "college": "计算机学院",
      "phoneNumber": "13800138000",
      "points": 100,
      "integrityScore": 95
    }
  ]
}
```

### 8. 获取活动签到记录

**接口**: `GET /activities/{id}/attendance`

**描述**: 获取指定活动的签到记录（仅活动创建者可访问）

**请求头**: `Authorization: Bearer {accessToken}`

**路径参数**:
- `id`: 活动ID

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": [
    {
      "id": "attendance_001",
      "activityId": "activity_123",
      "userId": "user_123",
      "userName": "张三",
      "studentId": "2021001",
      "checkInTime": "2024-01-01T10:00:00Z",
      "checkInLocation": "体育馆",
      "checkOutTime": "2024-01-01T12:00:00Z",
      "checkOutLocation": "体育馆",
      "isCheckedOut": true,
      "duration": 120,
      "pointsEarned": 10
    }
  ]
}
```

### 9. 移除活动参与者

**接口**: `DELETE /activities/{id}/participants/{userId}`

**描述**: 移除指定活动的参与者（仅活动创建者可操作）

**请求头**: `Authorization: Bearer {accessToken}`

**路径参数**:
- `id`: 活动ID
- `userId`: 用户ID

**响应**:
```json
{
  "success": true,
  "message": "参与者移除成功"
}
```

### 10. 请求活动签退码

**接口**: `POST /activities/request-checkout-code`

**描述**: 用户请求获取活动的签退二维码

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "activityId": "activity_123",
  "activityName": "篮球比赛",
  "studentName": "张三",
  "timestamp": 1640995200000
}
```

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "uniqueCode": "ACT_CHECKOUT_12345678",
    "activityId": "activity_123",
    "activityName": "篮球比赛",
    "studentId": "2021001",
    "studentName": "张三",
    "timestamp": 1640995200000,
    "expiresAt": 1640995240000,
    "isValid": true
  }
}
```

**验证逻辑**:
1. **用户验证**: 检查用户是否已登录且已报名该活动
2. **签到状态验证**: 用户必须已经完成签到
3. **活动状态验证**: 活动必须处于可签退状态（活动进行中或结束后30分钟内）
4. **生成唯一码**: 包含活动信息、用户信息和时间戳的唯一标识
5. **有效期设置**: 二维码有效期为40秒

### 11. 验证活动签退码

**接口**: `POST /activities/validate-checkout-code`

**描述**: 扫码者验证签退码并完成签退

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "uniqueCode": "ACT_CHECKOUT_12345678"
}
```

**验证逻辑**:
1. **签退码验证**: 
   - 检查签退码格式正确
   - 验证签退码在有效期内（40秒）
   - 确认签退码未被使用过
2. **权限验证**: 检查扫码者是否有签退权限
3. **活动状态验证**: 确认活动状态允许签退
4. **签到状态验证**: 确认该学生已完成签到
5. **重复签退检查**: 防止同一学生重复签退
6. **后端积分计算**: 根据签到到签退的时长自动计算积分
7. **积分发放**: 将计算的积分发放到用户账户
8. **WebSocket推送**: 签退成功后通过WebSocket推送积分更新和活动更新

**成功响应**:
```json
{
  "success": true,
  "message": "签退成功！张三 已完成 篮球比赛 签退",
  "data": {
    "pointsEarned": 10,
    "participationDuration": 120,
    "calculationRule": "参与时长120分钟，获得积分 = floor(120/30) * 基础积分",
    "totalPoints": 110,
    "activityPoints": 80,
    "morningExercisePoints": 30
  }
}
```

**失败响应示例**:
```json
{
  "success": false,
  "message": "签退码已过期，请重新获取"
}
```

**可能的错误消息**:
- "签退码格式错误"
- "签退码已过期，请重新获取"
- "签退码已被使用"
- "您没有签退权限"
- "活动不存在或已结束"
- "该学生尚未签到，无法签退"
- "该学生已经签退过了"

**说明**:
- 只有已签到的用户才能进行签退
- 签退后系统会自动计算参与时长并给予相应积分
- 积分计算规则：PE活动积分 = floor(参与时长(分钟) / 30) * 基础积分，由后端自动计算
- 积分发放：所有积分计算和发放均由后端服务完成，前端只负责显示
- WebSocket推送：积分变动通过WebSocket实时推送给用户

---

## 签到相关API

### 1. 获取签到码

**接口**: `GET /checkin/code`

**描述**: 获取当前用户的签到二维码

**请求头**: `Authorization: Bearer {accessToken}`

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "code": "QR_CODE_DATA_123456",
    "timestamp": 1640995200000,
    "activityId": "",
    "isOwner": false
  }
}
```



## 积分相关API

### 1. 获取积分记录

**接口**: `GET /points/records`

**描述**: 获取用户的积分获得历史记录

**请求头**: `Authorization: Bearer {accessToken}`

**请求参数**:
- `page`: 页码（可选，默认1）
- `limit`: 每页数量（可选，默认20）
- `activityType`: 活动类型过滤（可选，PE_ACTIVITY或MORNING_EXERCISE）

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": [
    {
      "id": "record_123",
      "userId": "user_123",
      "activityId": "activity_123",
      "activityName": "篮球比赛",
      "activityType": "PE_ACTIVITY",
      "pointsEarned": 10,
      "earnedReason": "完成PE活动签到签退",
      "calculationRule": "参与时长120分钟，获得积分 = floor(120/30) * 基础积分",
      "participationDuration": 120,
      "earnedAt": "2024-01-01T10:00:00Z"
    },
    {
      "id": "record_124",
      "userId": "user_123",
      "activityId": "morning_001",
      "activityName": "早操考勤",
      "activityType": "MORNING_EXERCISE",
      "pointsEarned": 1,
      "earnedReason": "完成早操签到签退",
      "calculationRule": "早操固定积分1分",
      "participationDuration": 0,
      "earnedAt": "2024-01-02T07:00:00Z"
    }
  ]
}
```

### 2. 获取积分汇总

**接口**: `GET /points/summary`

**描述**: 获取用户的积分汇总信息

**请求头**: `Authorization: Bearer {accessToken}`

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "totalPoints": 100,
    "peActivityPoints": 70,
    "morningExercisePoints": 30,
    "thisMonthPoints": 15,
    "lastMonthPoints": 25,
    "lastUpdated": "2024-01-15T10:30:00Z"
  }
}
```

**积分获得规则**:
- **早操**: 完成签到和签退获得固定1积分
- **PE活动**: 根据签到到签退的时长获得相应积分，计算公式：积分 = floor(参与时长(分钟) / 30) * 基础积分
- **积分计算**: 所有积分计算逻辑由后端自动处理，无需客户端干预
- **积分发放**: 积分在签退完成后由后端统一发放到用户账户
- **实时推送**: 积分变动通过WebSocket实时推送给用户

### 3. 获取用户积分详情

**接口**: `GET /points/details`

**描述**: 获取用户的详细积分信息

**请求头**: `Authorization: Bearer {accessToken}`

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "userId": "user_123",
    "totalPoints": 100,
    "peActivityPoints": 70,
    "morningExercisePoints": 30,
    "recentActivities": [
      {
        "activityName": "篮球比赛",
        "pointsEarned": 10,
        "earnedAt": "2024-01-01T10:00:00Z"
      }
    ],
    "pointsHistory": {
      "thisWeek": 15,
      "thisMonth": 25,
      "lastMonth": 20
    },
    "calculationRules": {
      "peActivity": "积分 = floor(参与时长(分钟) / 30) * 基础积分",
      "morningExercise": "早操固定积分1分"
    }
  }
}
```

---

## WebSocket实时推送API

### 1. WebSocket连接

**接口**: `WebSocket /ws/activity-updates`

**描述**: 建立WebSocket连接，接收实时推送消息

**连接认证**: 需要在连接时传递JWT Token作为查询参数

**连接URL**: `wss://api.pe-campus.com/v1/ws/activity-updates?token={accessToken}`

### 2. 推送消息格式

#### 2.1 积分更新推送
```json
{
  "type": "POINTS_UPDATE",
  "userId": "user_123",
  "pointsEarned": 10,
  "totalPoints": 110,
  "peActivityPoints": 80,
  "morningExercisePoints": 30,
  "activityName": "篮球比赛",
  "earnedReason": "完成PE活动签到签退",
  "calculationRule": "参与时长120分钟，获得积分 = floor(120/30) * 基础积分",
  "participationDuration": 120,
  "timestamp": "2024-01-15T10:30:00Z"
}
```

#### 2.2 活动更新推送
```json
{
  "type": "ACTIVITY_UPDATE",
  "userId": "user_123",
  "updatedActivities": [
    {
      "id": "activity_123",
      "title": "篮球比赛",
      "status": "COMPLETED",
      "userAttendanceStatus": "CHECKED_OUT",
      "pointsEarned": 10
    }
  ],
  "timestamp": "2024-01-15T10:30:00Z"
}
```

#### 2.3 早操更新推送
```json
{
  "type": "MORNING_EXERCISE_UPDATE",
  "userId": "user_123",
  "exerciseId": "morning_001",
  "exerciseName": "早操考勤",
  "status": "COMPLETED",
  "pointsEarned": 1,
  "timestamp": "2024-01-15T07:30:00Z"
}
```

### 3. 心跳机制

**客户端发送心跳**:
```json
{
  "type": "PING",
  "timestamp": 1640995200000
}
```

**服务端回应**:
```json
{
  "type": "PONG",
  "timestamp": 1640995200000
}
```

### 4. 连接状态管理

#### 4.1 连接成功
```json
{
  "type": "CONNECTION_SUCCESS",
  "userId": "user_123",
  "connectionId": "conn_123456",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

#### 4.2 连接错误
```json
{
  "type": "CONNECTION_ERROR",
  "error": "Token expired",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

---

## 积分计算服务API

### 1. 积分计算结果查询

**接口**: `GET /points/calculation-result/{activityId}/{userId}`

**描述**: 查询指定活动的积分计算结果（内部服务调用）

**请求头**: `Authorization: Bearer {accessToken}`

**路径参数**:
- `activityId`: 活动ID
- `userId`: 用户ID

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "userId": "user_123",
    "activityId": "activity_123",
    "activityType": "PE_ACTIVITY",
    "checkInTime": "2024-01-15T14:00:00Z",
    "checkOutTime": "2024-01-15T16:00:00Z",
    "participationDuration": 120,
    "pointsEarned": 10,
    "calculationRule": "参与时长120分钟，获得积分 = floor(120/30) * 基础积分",
    "calculatedAt": "2024-01-15T16:00:00Z"
  }
}
```

### 2. 积分发放状态查询

**接口**: `GET /points/grant-status/{userId}`

**描述**: 查询用户的积分发放状态

**请求头**: `Authorization: Bearer {accessToken}`

**路径参数**:
- `userId`: 用户ID

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "userId": "user_123",
    "totalPointsGranted": 100,
    "pendingGrants": 0,
    "lastGrantedAt": "2024-01-15T16:00:00Z",
    "recentGrants": [
      {
        "activityId": "activity_123",
        "pointsGranted": 10,
        "grantedAt": "2024-01-15T16:00:00Z"
      }
    ]
  }
}
```

---

## 诚信度相关API

### 1. 获取诚信度

**接口**: `GET /integrity/score`

**描述**: 获取用户当前的诚信度分数

**请求头**: `Authorization: Bearer {accessToken}`

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": 95
}
```

---

## 权限设置相关API

### 1. 获取权限设置

**接口**: `GET /settings/permissions`

**描述**: 获取用户的权限设置状态

**请求头**: `Authorization: Bearer {accessToken}`

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "cameraPermission": true,
    "locationPermission": false,
    "storagePermission": true,
    "notificationPermission": true,
    "phonePermission": false
  }
}
```

### 2. 更新权限设置

**接口**: `PUT /settings/permissions`

**描述**: 更新用户的权限设置

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "cameraPermission": true,
  "locationPermission": true,
  "storagePermission": true,
  "notificationPermission": true,
  "phonePermission": true
}
```

**响应**:
```json
{
  "success": true,
  "message": "更新成功"
}
```

---

## 数据库设计建议

### 用户表 (users)
```sql
CREATE TABLE users (
  id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  student_id VARCHAR(20) UNIQUE NOT NULL,
  school VARCHAR(100),
  college VARCHAR(100),
  phone_number VARCHAR(20) UNIQUE NOT NULL,
  avatar TEXT,
  points INT DEFAULT 0,
  pe_activity_points INT DEFAULT 0,
  morning_exercise_points INT DEFAULT 0,
  integrity_score INT DEFAULT 100,
  role ENUM('STUDENT', 'CHECKER', 'SUB_CHECKER', 'ADMIN') DEFAULT 'STUDENT',
  points_last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 活动表 (activities)
```sql
CREATE TABLE activities (
  id VARCHAR(36) PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  location VARCHAR(200),
  max_participants INT,
  current_participants INT DEFAULT 0,
  registration_start_time TIMESTAMP,
  registration_end_time TIMESTAMP,
  activity_start_time TIMESTAMP,
  activity_end_time TIMESTAMP,
  organizer VARCHAR(100),
  organizer_id VARCHAR(36),
  category VARCHAR(50),
  points INT DEFAULT 0,
  image_url TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 活动报名表 (activity_registrations)
```sql
CREATE TABLE activity_registrations (
  id VARCHAR(36) PRIMARY KEY,
  user_id VARCHAR(36) NOT NULL,
  activity_id VARCHAR(36) NOT NULL,
  status ENUM('registered', 'attended', 'absent', 'cancelled') DEFAULT 'registered',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (activity_id) REFERENCES activities(id),
  UNIQUE KEY unique_user_activity (user_id, activity_id)
);
```

### 签到记录表 (attendance_records)
```sql
CREATE TABLE attendance_records (
  id VARCHAR(36) PRIMARY KEY,
  activity_id VARCHAR(36) NOT NULL,
  user_id VARCHAR(36) NOT NULL,
  user_name VARCHAR(50) NOT NULL,
  student_id VARCHAR(20) NOT NULL,
  
  -- 签到信息
  check_in_time TIMESTAMP NULL,
  check_in_location VARCHAR(200),
  checked_in_by VARCHAR(36),
  
  -- 签退信息
  check_out_time TIMESTAMP NULL,
  check_out_location VARCHAR(200),
  checked_out_by VARCHAR(36),
  is_checked_out BOOLEAN DEFAULT FALSE,
  
  -- 积分信息
  duration INT DEFAULT 0,                       -- 参与时长(分钟)
  points_earned INT DEFAULT 0,                  -- 获得积分
  
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (activity_id) REFERENCES activities(id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  UNIQUE KEY unique_activity_user (activity_id, user_id)
);
```

### 积分记录表 (points_records)
```sql
CREATE TABLE points_records (
  id VARCHAR(36) PRIMARY KEY,
  user_id VARCHAR(36) NOT NULL,
  activity_id VARCHAR(36) NOT NULL,
  activity_name VARCHAR(200) NOT NULL,
  activity_type VARCHAR(20) NOT NULL,        -- PE_ACTIVITY, MORNING_EXERCISE
  points_earned INT NOT NULL,
  earned_reason VARCHAR(200),                -- 获得原因
  calculation_rule VARCHAR(200),             -- 计算规则
  participation_duration INT DEFAULT 0,      -- 参与时长(分钟)
  earned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  
  INDEX idx_user_id (user_id),
  INDEX idx_user_earned_at (user_id, earned_at),
  INDEX idx_activity_id (activity_id),
  INDEX idx_points_earned (points_earned)
);
```

### 用户积分汇总表 (user_points_summary)
```sql
CREATE TABLE user_points_summary (
  user_id VARCHAR(36) PRIMARY KEY,
  total_points INT DEFAULT 0,
  pe_activity_points INT DEFAULT 0,
  morning_exercise_points INT DEFAULT 0,
  last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  
  INDEX idx_total_points (total_points)
);
```

### 活动签到码表 (activity_checkin_codes)
```sql
CREATE TABLE activity_checkin_codes (
  id VARCHAR(36) PRIMARY KEY,
  unique_code VARCHAR(100) UNIQUE NOT NULL,    -- 唯一签到码
  activity_id VARCHAR(36) NOT NULL,            -- 活动ID
  activity_name VARCHAR(200) NOT NULL,         -- 活动名称
  student_id VARCHAR(20) NOT NULL,             -- 学生ID
  student_name VARCHAR(50) NOT NULL,           -- 学生姓名
  timestamp BIGINT NOT NULL,                   -- 生成时间戳
  expires_at BIGINT NOT NULL,                  -- 过期时间戳
  is_valid BOOLEAN DEFAULT TRUE,               -- 是否有效
  is_used BOOLEAN DEFAULT FALSE,               -- 是否已使用
  used_at TIMESTAMP NULL,                      -- 使用时间
  used_by VARCHAR(36) NULL,                    -- 使用者ID
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_unique_code (unique_code),
  INDEX idx_activity_student (activity_id, student_id),
  INDEX idx_expires_at (expires_at)
);
```

### 活动签退码表 (activity_checkout_codes)
```sql
CREATE TABLE activity_checkout_codes (
  id VARCHAR(36) PRIMARY KEY,
  unique_code VARCHAR(100) UNIQUE NOT NULL,    -- 唯一签退码
  activity_id VARCHAR(36) NOT NULL,            -- 活动ID
  activity_name VARCHAR(200) NOT NULL,         -- 活动名称
  student_id VARCHAR(20) NOT NULL,             -- 学生ID
  student_name VARCHAR(50) NOT NULL,           -- 学生姓名
  timestamp BIGINT NOT NULL,                   -- 生成时间戳
  expires_at BIGINT NOT NULL,                  -- 过期时间戳
  is_valid BOOLEAN DEFAULT TRUE,               -- 是否有效
  is_used BOOLEAN DEFAULT FALSE,               -- 是否已使用
  used_at TIMESTAMP NULL,                      -- 使用时间
  used_by VARCHAR(36) NULL,                    -- 使用者ID
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_unique_code (unique_code),
  INDEX idx_activity_student (activity_id, student_id),
  INDEX idx_expires_at (expires_at)
);
```

### 早操考勤活动表 (morning_exercises)
```sql
CREATE TABLE morning_exercises (
  id VARCHAR(36) PRIMARY KEY,
  title VARCHAR(200) NOT NULL DEFAULT '早操考勤',
  description TEXT DEFAULT '每日早操考勤',
  location VARCHAR(200) DEFAULT '操场',
  date DATE NOT NULL,
  start_time TIMESTAMP NOT NULL,
  end_time TIMESTAMP NOT NULL,
  type ENUM('MORNING_EXERCISE') DEFAULT 'MORNING_EXERCISE',
  is_active BOOLEAN DEFAULT TRUE,
  total_participants INT DEFAULT 0,
  checked_in_count INT DEFAULT 0,
  checked_out_count INT DEFAULT 0,
  created_by VARCHAR(36) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (created_by) REFERENCES users(id)
);
```

### 早操考勤记录表 (morning_exercise_attendance)
```sql
CREATE TABLE morning_exercise_attendance (
  id VARCHAR(36) PRIMARY KEY,
  exercise_id VARCHAR(36) NOT NULL,
  student_id VARCHAR(20) NOT NULL,
  student_name VARCHAR(50) NOT NULL,
  check_in_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  check_in_location VARCHAR(200) DEFAULT '',
  checked_by VARCHAR(36) NOT NULL,
  checked_by_name VARCHAR(50) NOT NULL,
  check_out_time TIMESTAMP NULL,
  check_out_location VARCHAR(200) DEFAULT '',
  checked_out_by VARCHAR(36) NULL,
  checked_out_by_name VARCHAR(50) DEFAULT '',
  is_checked_out BOOLEAN DEFAULT FALSE,
  points_earned INT DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (exercise_id) REFERENCES morning_exercises(id),
  FOREIGN KEY (checked_by) REFERENCES users(id),
  FOREIGN KEY (checked_out_by) REFERENCES users(id),
  UNIQUE KEY unique_exercise_student (exercise_id, student_id)
);
```

### 早操签退码表 (morning_exercise_checkout_codes)
```sql
CREATE TABLE morning_exercise_checkout_codes (
  id VARCHAR(36) PRIMARY KEY,
  unique_code VARCHAR(100) UNIQUE NOT NULL,
  exercise_id VARCHAR(36) NOT NULL,
  exercise_name VARCHAR(200) NOT NULL,
  student_id VARCHAR(20) NOT NULL,
  student_name VARCHAR(50) NOT NULL,
  timestamp BIGINT NOT NULL,
  expires_at BIGINT NOT NULL,
  is_valid BOOLEAN DEFAULT TRUE,
  is_used BOOLEAN DEFAULT FALSE,
  used_at TIMESTAMP NULL,
  used_by VARCHAR(36) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_unique_code (unique_code),
  INDEX idx_exercise_student (exercise_id, student_id),
  INDEX idx_expires_at (expires_at)
);
```

### 签到员授权表 (checker_authorizations)
```sql
CREATE TABLE checker_authorizations (
  id VARCHAR(36) PRIMARY KEY,
  student_id VARCHAR(20) NOT NULL,
  student_name VARCHAR(50) NOT NULL,
  role ENUM('CHECKER', 'SUB_CHECKER') NOT NULL,
  authorized_by VARCHAR(36) NOT NULL,
  authorized_by_name VARCHAR(50) NOT NULL,
  authorized_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  is_active BOOLEAN DEFAULT TRUE,
  exercise_id VARCHAR(36) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (authorized_by) REFERENCES users(id),
  FOREIGN KEY (exercise_id) REFERENCES morning_exercises(id),
  UNIQUE KEY unique_student_exercise (student_id, exercise_id)
);
```

### WebSocket连接表 (websocket_connections)
```sql
CREATE TABLE websocket_connections (
  id VARCHAR(36) PRIMARY KEY,
  user_id VARCHAR(36) NOT NULL,
  connection_id VARCHAR(100) NOT NULL,
  connected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_ping_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  is_active BOOLEAN DEFAULT TRUE,
  FOREIGN KEY (user_id) REFERENCES users(id),
  INDEX idx_user_connection (user_id, connection_id),
  INDEX idx_active_connections (is_active, last_ping_at)
);
```

### JWT Token黑名单表 (token_blacklist)
```sql
CREATE TABLE token_blacklist (
  id VARCHAR(36) PRIMARY KEY,
  token_jti VARCHAR(255) UNIQUE NOT NULL,
  user_id VARCHAR(36),
  expires_at TIMESTAMP NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 安全建议

1. **HTTPS**: 所有API调用必须使用HTTPS
2. **Token安全**: 
   - Access Token有效期建议1小时
   - Refresh Token有效期建议7天
   - 实现Token黑名单机制
3. **验证码**:
   - 验证码有效期5分钟
   - 实现防刷机制（同一手机号1分钟内只能发送一次）
4. **请求限制**: 实现API调用频率限制
5. **数据验证**: 严格验证所有输入参数
6. **日志记录**: 记录所有关键操作的审计日志
7. **二维码安全**:
   - 所有签到/签退码40秒后自动失效
   - 使用Redis存储已使用的码，防止重复使用
   - 实现分布式锁防止并发问题
8. **积分安全**:
   - 所有积分计算逻辑在后端完成，前端无权修改
   - 积分发放记录唯一性约束，防止重复发放
   - 使用事务保证积分计算和发放的原子性
9. **WebSocket安全**:
   - WebSocket连接需要JWT Token认证
   - 实现心跳机制检测连接状态
   - 推送消息包含签名验证
10. **权限控制**:
    - 早操扫码权限：只有CHECKER和SUB_CHECKER可以扫码
    - PE活动扫码权限：任何已登录用户都可以扫码
    - 积分查询权限：用户只能查询自己的积分
    - 管理员权限：可以查询所有用户积分和操作记录

---

## 新版签到码相关API

### 1. 请求活动签到码

**接口**: `POST /activities/request-checkin-code`

**描述**: 用户请求获取活动的签到二维码

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "activityId": "activity_123",
  "activityName": "篮球比赛",
  "studentName": "张三",
  "timestamp": 1640995200000
}
```

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "uniqueCode": "ACT_CHECKIN_12345678",
    "activityId": "activity_123",
    "activityName": "篮球比赛",
    "studentId": "2021001",
    "studentName": "张三",
    "timestamp": 1640995200000,
    "expiresAt": 1640995240000,
    "isValid": true
  }
}
```

**验证逻辑**:
1. **用户验证**: 检查用户是否已登录且已报名该活动
2. **活动状态验证**: 活动必须处于可签到状态（活动进行中或结束后30分钟内）
3. **生成唯一码**: 包含活动信息、用户信息和时间戳的唯一标识
4. **有效期设置**: 二维码有效期为40秒

### 2. 验证签到码

**接口**: `POST /activities/validate-checkin-code`

**描述**: 扫码者验证签到码并完成签到

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "uniqueCode": "ACT_CHECKIN_12345678"
}
```

**验证逻辑**:
1. **签到码验证**: 
   - 检查签到码格式正确
   - 验证签到码在有效期内（40秒）
   - 确认签到码未被使用过
2. **权限验证**: 检查扫码者是否有签到权限
3. **活动状态验证**: 确认活动状态允许签到
4. **重复签到检查**: 防止同一学生重复签到
5. **WebSocket推送**: 签到成功后通过WebSocket推送更新的活动列表

**成功响应**:
```json
{
  "success": true,
  "message": "签到成功！张三 已完成 篮球比赛 签到"
}
```

**失败响应示例**:
```json
{
  "success": false,
  "message": "签到码已过期，请重新获取"
}
```

**可能的错误消息**:
- "签到码格式错误"
- "签到码已过期，请重新获取"
- "签到码已被使用"
- "您没有签到权限"
- "活动不存在或已结束"
- "该学生已经签到过了"

### 3. WebSocket实时推送

**接口**: `WebSocket /ws/activity-updates`

**描述**: 实时推送活动状态更新

**连接认证**: 需要在连接时传递JWT Token

**推送消息格式**:
```json
{
  "type": "ACTIVITY_UPDATE",
  "data": {
    "userId": "user_123",
    "updatedActivities": [
      // 更新后的活动列表
    ]
  }
}
```

**推送场景**:
- 用户签到成功后
- 活动状态发生变化时
- 用户报名/取消报名活动时

### 4. 请求活动签退码

**接口**: `POST /activities/request-checkout-code`

**描述**: 用户请求获取活动的签退二维码

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "activityId": "activity_123",
  "activityName": "篮球比赛",
  "studentName": "张三",
  "timestamp": 1640995200000
}
```

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "uniqueCode": "ACT_CHECKOUT_12345678",
    "activityId": "activity_123",
    "activityName": "篮球比赛",
    "studentId": "2021001",
    "studentName": "张三",
    "timestamp": 1640995200000,
    "expiresAt": 1640995240000,
    "isValid": true
  }
}
```

**验证逻辑**:
1. **用户验证**: 检查用户是否已登录且已报名该活动
2. **签到状态验证**: 用户必须已经完成签到
3. **活动状态验证**: 活动必须处于可签退状态（活动进行中或结束后30分钟内）
4. **生成唯一码**: 包含活动信息、用户信息和时间戳的唯一标识
5. **有效期设置**: 二维码有效期为40秒

### 5. 验证活动签退码

**接口**: `POST /activities/validate-checkout-code`

**描述**: 扫码者验证签退码并完成签退

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "uniqueCode": "ACT_CHECKOUT_12345678"
}
```

**验证逻辑**:
1. **签退码验证**: 
   - 检查签退码格式正确
   - 验证签退码在有效期内（40秒）
   - 确认签退码未被使用过
2. **权限验证**: 检查扫码者是否有签退权限
3. **活动状态验证**: 确认活动状态允许签退
4. **签到状态验证**: 确认该学生已完成签到
5. **重复签退检查**: 防止同一学生重复签退
6. **积分计算**: 根据签到到签退的时长计算积分
7. **WebSocket推送**: 签退成功后通过WebSocket推送更新

**成功响应**:
```json
{
  "success": true,
  "message": "签退成功！张三 已完成 篮球比赛 签退，获得10积分"
}
```

**失败响应示例**:
```json
{
  "success": false,
  "message": "签退码已过期，请重新获取"
}
```

**可能的错误消息**:
- "签退码格式错误"
- "签退码已过期，请重新获取"
- "签退码已被使用"
- "您没有签退权限"
- "活动不存在或已结束"
- "该学生尚未签到，无法签退"
- "该学生已经签退过了"

---

## 早操考勤相关API

### 1. 获取当前早操考勤活动

**接口**: `GET /morning-exercise/current`

**描述**: 获取当前进行中的早操考勤活动

**请求头**: `Authorization: Bearer {accessToken}`

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "id": "morning_exercise_123",
    "title": "早操考勤",
    "description": "每日早操考勤",
    "location": "操场",
    "date": "2024-01-15T00:00:00Z",
    "startTime": "2024-01-15T06:30:00Z",
    "endTime": "2024-01-15T07:30:00Z",
    "type": "MORNING_EXERCISE",
    "isActive": true,
    "totalParticipants": 300,
    "checkedInCount": 250,
    "createdBy": "admin_123",
    "createdAt": "2024-01-14T20:00:00Z"
  }
}
```

### 2. 获取早操考勤活动列表

**接口**: `GET /morning-exercise/list`

**描述**: 获取早操考勤活动历史列表

**请求头**: `Authorization: Bearer {accessToken}`

**请求参数**:
- `page`: 页码（可选，默认1）
- `limit`: 每页数量（可选，默认10）

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": [
    // 早操考勤活动列表
  ]
}
```

### 3. 创建早操考勤活动

**接口**: `POST /morning-exercise/create`

**描述**: 创建新的早操考勤活动（仅管理员可用）

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "title": "早操考勤",
  "description": "每日早操考勤",
  "location": "操场",
  "date": "2024-01-15T00:00:00Z",
  "startTime": "2024-01-15T06:30:00Z",
  "endTime": "2024-01-15T07:30:00Z",
  "type": "MORNING_EXERCISE"
}
```

**响应**:
```json
{
  "success": true,
  "message": "创建成功",
  "data": {
    // 创建的早操考勤活动对象
  }
}
```

### 4. 获取早操考勤二维码

**接口**: `POST /activities/request-checkin-code`

**描述**: 获取早操考勤活动的签到二维码（使用统一的签到码接口）

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "activityId": "morning_exercise_123",
  "activityName": "早操考勤",
  "studentName": "张三",
  "timestamp": 1640995200000
}
```

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "uniqueCode": "ME_CHECKIN_12345678",
    "activityId": "morning_exercise_123",
    "activityName": "早操考勤",
    "studentId": "2021001",
    "studentName": "张三",
    "timestamp": 1640995200000,
    "expiresAt": 1640995240000,
    "isValid": true
  }
}
```

**注意**: 早操考勤现在也使用40秒刷新机制，与普通活动保持一致

### 5. 扫码签到

**接口**: `POST /activities/validate-checkin-code`

**描述**: 签到员或二级管理员扫描学生二维码进行签到（使用统一的验证接口）

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "uniqueCode": "ME_CHECKIN_12345678"
}
```

**验证逻辑**:
1. **权限验证**: 检查操作者是否为签到员或二级管理员
2. **签到码验证**: 
   - 验证签到码格式正确
   - 检查签到码是否在有效期内（40秒）
   - 确认签到码对应的学生信息
3. **活动状态验证**: 
   - 活动必须存在且处于活跃状态
   - 当前时间必须在活动时间范围内
4. **重复签到检查**: 防止同一学生重复签到
5. **WebSocket推送**: 签到成功后推送更新信息

**成功响应**:
```json
{
  "success": true,
  "message": "签到成功！张三 已完成早操考勤"
}
```

**失败响应示例**:
```json
{
  "success": false,
  "message": "签到码已过期，请重新获取"
}
```

### 6. 请求早操签退码

**接口**: `POST /morning-exercise/request-checkout-code`

**描述**: 用户请求获取早操的签退二维码

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "exerciseId": "morning_exercise_123",
  "exerciseName": "早操考勤",
  "studentName": "张三",
  "timestamp": 1640995200000
}
```

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "uniqueCode": "ME_CHECKOUT_12345678",
    "exerciseId": "morning_exercise_123",
    "exerciseName": "早操考勤",
    "studentId": "2021001",
    "studentName": "张三",
    "timestamp": 1640995200000,
    "expiresAt": 1640995240000,
    "isValid": true
  }
}
```

**验证逻辑**:
1. **用户验证**: 检查用户是否已登录
2. **签到状态验证**: 用户必须已经完成早操签到
3. **活动状态验证**: 早操必须处于可签退状态（活动进行中或结束后30分钟内）
4. **生成唯一码**: 包含早操信息、用户信息和时间戳的唯一标识
5. **有效期设置**: 二维码有效期为40秒

### 7. 验证早操签退码

**接口**: `POST /morning-exercise/validate-checkout-code`

**描述**: 签到员或二级管理员扫描学生签退码并完成签退

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "uniqueCode": "ME_CHECKOUT_12345678"
}
```

**验证逻辑**:
1. **签退码验证**: 
   - 检查签退码格式正确
   - 验证签退码在有效期内（40秒）
   - 确认签退码未被使用过
2. **权限验证**: 检查扫码者是否为签到员(CHECKER)或二级管理员(SUB_CHECKER)
3. **活动状态验证**: 确认早操状态允许签退
4. **签到状态验证**: 确认该学生已完成签到
5. **重复签退检查**: 防止同一学生重复签退
6. **后端积分计算**: 后端自动计算早操积分(固定1分)
7. **积分发放**: 将积分发放到用户账户
8. **WebSocket推送**: 签退成功后通过WebSocket推送积分更新

**成功响应**:
```json
{
  "success": true,
  "message": "签退成功！张三 已完成早操考勤",
  "data": {
    "pointsEarned": 1,
    "calculationRule": "早操固定积分1分",
    "totalPoints": 101,
    "peActivityPoints": 70,
    "morningExercisePoints": 31
  }
}
```

**失败响应示例**:
```json
{
  "success": false,
  "message": "签退码已过期，请重新获取"
}
```

**可能的错误消息**:
- "签退码格式错误"
- "签退码已过期，请重新获取"
- "签退码已被使用"
- "您没有签退权限"
- "早操不存在或已结束"
- "该学生尚未签到，无法签退"
- "该学生已经签退过了"

### 8. 扫码授权

**接口**: `POST /morning-exercise/scan-authorize`

**描述**: 签到员扫描学生二维码设置其为二级管理员

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "qrCodeData": "ME_QR_CODE_DATA_12345",
  "exerciseId": "morning_exercise_123",
  "role": "SUB_CHECKER"
}
```

**验证逻辑**:
1. **权限验证**: 检查操作者是否为签到员（只有签到员可以授权）
2. **二维码验证**: 同扫码签到的验证逻辑
3. **角色验证**: 确认要设置的角色有效

**响应**:
```json
{
  "success": true,
  "message": "授权成功！张三 已成为二级管理员"
}
```

### 9. 设置签到员

**接口**: `POST /morning-exercise/set-checker`

**描述**: 管理员设置签到员权限

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "studentId": "2021001",
  "role": "CHECKER",
  "exerciseId": "morning_exercise_123"
}
```

**role值说明**:
- `CHECKER`: 签到员（可以签到和授权）
- `SUB_CHECKER`: 二级管理员（只能签到）

**响应**:
```json
{
  "success": true,
  "message": "设置成功"
}
```

### 10. 获取签到员列表

**接口**: `GET /morning-exercise/{id}/checkers`

**描述**: 获取指定早操考勤活动的签到员列表

**请求头**: `Authorization: Bearer {accessToken}`

**路径参数**:
- `id`: 早操考勤活动ID

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": [
    {
      "id": "checker_001",
      "studentId": "2021001",
      "studentName": "张三",
      "role": "CHECKER",
      "authorizedBy": "admin_123",
      "authorizedByName": "管理员",
      "authorizedAt": "2024-01-15T06:00:00Z",
      "isActive": true,
      "exerciseId": "morning_exercise_123"
    }
  ]
}
```

### 11. 移除签到员权限

**接口**: `DELETE /morning-exercise/{id}/checkers/{studentId}`

**描述**: 移除指定学生的签到员权限

**请求头**: `Authorization: Bearer {accessToken}`

**路径参数**:
- `id`: 早操考勤活动ID
- `studentId`: 学生ID

**响应**:
```json
{
  "success": true,
  "message": "移除成功"
}
```

### 12. 获取早操考勤记录

**接口**: `GET /morning-exercise/{id}/attendance`

**描述**: 获取指定早操考勤活动的签到记录

**请求头**: `Authorization: Bearer {accessToken}`

**路径参数**:
- `id`: 早操考勤活动ID

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": [
    {
      "id": "attendance_001",
      "exerciseId": "morning_exercise_123",
      "studentId": "2021001",
      "studentName": "张三",
      "checkInTime": "2024-01-15T06:35:00Z",
      "checkInLocation": "操场",
      "checkedBy": "checker_001",
      "checkedByName": "李四",
      "qrCodeData": "ME_QR_CODE_DATA_12345",
      "isValid": true
    }
  ]
}
```

### 13. 获取早操考勤统计

**接口**: `GET /morning-exercise/{id}/stats`

**描述**: 获取指定早操考勤活动的统计信息

**请求头**: `Authorization: Bearer {accessToken}`

**路径参数**:
- `id`: 早操考勤活动ID

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "exerciseId": "morning_exercise_123",
    "totalStudents": 300,
    "checkedInCount": 250,
    "checkedOutCount": 200,
    "completedCount": 180,
    "attendanceRate": 83.33,
    "completionRate": 60.00,
    "topCheckers": [
      {
        "id": "checker_001",
        "name": "李四",
        "studentId": "2021002",
        "checkedCount": 25
      }
    ],
    "totalPointsEarned": 180
  }
}
```

### 14. 获取用户在早操考勤中的角色

**接口**: `GET /morning-exercise/{id}/user-role`

**描述**: 获取当前用户在指定早操考勤活动中的角色

**请求头**: `Authorization: Bearer {accessToken}`

**路径参数**:
- `id`: 早操考勤活动ID

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": "CHECKER"
}
```

**角色说明**:
- `STUDENT`: 普通学生
- `CHECKER`: 签到员
- `SUB_CHECKER`: 二级管理员
- `ADMIN`: 管理员

### 15. 获取用户的早操考勤权限

**接口**: `GET /morning-exercise/user-permissions`

**描述**: 获取当前用户的早操考勤权限列表

**请求头**: `Authorization: Bearer {accessToken}`

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": [
    {
      "id": "permission_001",
      "studentId": "2021001",
      "studentName": "张三",
      "role": "CHECKER",
      "authorizedBy": "admin_123",
      "authorizedByName": "管理员",
      "authorizedAt": "2024-01-15T06:00:00Z",
      "isActive": true,
      "exerciseId": "morning_exercise_123"
    }
  ]
}
```

### 16. 请求早操签退码

**接口**: `POST /morning-exercise/request-checkout-code`

**描述**: 用户请求获取早操的签退二维码

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "exerciseId": "morning_exercise_123",
  "exerciseName": "早操考勤",
  "studentName": "张三",
  "timestamp": 1640995200000
}
```

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "uniqueCode": "ME_CHECKOUT_12345678",
    "exerciseId": "morning_exercise_123",
    "exerciseName": "早操考勤",
    "studentId": "2021001",
    "studentName": "张三",
    "timestamp": 1640995200000,
    "expiresAt": 1640995240000,
    "isValid": true
  }
}
```

**验证逻辑**:
1. **用户验证**: 检查用户是否已登录
2. **签到状态验证**: 用户必须已经完成早操签到
3. **活动状态验证**: 早操必须处于可签退状态（活动进行中或结束后30分钟内）
4. **生成唯一码**: 包含早操信息、用户信息和时间戳的唯一标识
5. **有效期设置**: 二维码有效期为40秒

### 17. 验证早操签退码

**接口**: `POST /morning-exercise/validate-checkout-code`

**描述**: 签到员或二级管理员扫描学生签退码并完成签退

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "uniqueCode": "ME_CHECKOUT_12345678"
}
```

**验证逻辑**:
1. **签退码验证**: 
   - 检查签退码格式正确
   - 验证签退码在有效期内（40秒）
   - 确认签退码未被使用过
2. **权限验证**: 检查扫码者是否为签到员或二级管理员
3. **活动状态验证**: 确认早操状态允许签退
4. **签到状态验证**: 确认该学生已完成签到
5. **重复签退检查**: 防止同一学生重复签退
6. **积分发放**: 完成签退后给予1积分
7. **WebSocket推送**: 签退成功后通过WebSocket推送更新

**成功响应**:
```json
{
  "success": true,
  "message": "签退成功！张三 已完成早操考勤并获得1积分"
}
```

**失败响应示例**:
```json
{
  "success": false,
  "message": "签退码已过期，请重新获取"
}
```

**可能的错误消息**:
- "签退码格式错误"
- "签退码已过期，请重新获取"
- "签退码已被使用"
- "您没有签退权限"
- "早操不存在或已结束"
- "该学生尚未签到，无法签退"
- "该学生已经签退过了"

**说明**:
- 只有已签到的学生才能进行签退
- 签退后系统会自动给予1积分
- 只有完成签到和签退才能获得积分
- 积分发放逻辑由后端自动处理

---

## 早操考勤数据库扩展

### 早操考勤活动表 (morning_exercises)
```sql
CREATE TABLE morning_exercises (
  id VARCHAR(36) PRIMARY KEY,
  title VARCHAR(200) NOT NULL DEFAULT '早操考勤',
  description TEXT DEFAULT '每日早操考勤',
  location VARCHAR(200) DEFAULT '操场',
  date DATE NOT NULL,
  start_time TIMESTAMP NOT NULL,
  end_time TIMESTAMP NOT NULL,
  type ENUM('MORNING_EXERCISE') DEFAULT 'MORNING_EXERCISE',
  is_active BOOLEAN DEFAULT TRUE,
  total_participants INT DEFAULT 0,
  checked_in_count INT DEFAULT 0,
  created_by VARCHAR(36) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (created_by) REFERENCES users(id)
);
```

### 早操考勤记录表 (morning_exercise_attendance)
```sql
CREATE TABLE morning_exercise_attendance (
  id VARCHAR(36) PRIMARY KEY,
  exercise_id VARCHAR(36) NOT NULL,
  student_id VARCHAR(20) NOT NULL,
  student_name VARCHAR(50) NOT NULL,
  check_in_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  check_in_location VARCHAR(200) DEFAULT '',
  checked_by VARCHAR(36) NOT NULL,
  checked_by_name VARCHAR(50) NOT NULL,
  qr_code_data TEXT NOT NULL,
  is_valid BOOLEAN DEFAULT TRUE,
  check_out_time TIMESTAMP NULL,
  check_out_location VARCHAR(200) DEFAULT '',
  checked_out_by VARCHAR(36) NULL,
  checked_out_by_name VARCHAR(50) DEFAULT '',
  is_checked_out BOOLEAN DEFAULT FALSE,
  points_earned INT DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (exercise_id) REFERENCES morning_exercises(id),
  FOREIGN KEY (checked_by) REFERENCES users(id),
  FOREIGN KEY (checked_out_by) REFERENCES users(id),
  UNIQUE KEY unique_exercise_student (exercise_id, student_id)
);
```

### 签到员授权表 (checker_authorizations)
```sql
CREATE TABLE checker_authorizations (
  id VARCHAR(36) PRIMARY KEY,
  student_id VARCHAR(20) NOT NULL,
  student_name VARCHAR(50) NOT NULL,
  role ENUM('CHECKER', 'SUB_CHECKER') NOT NULL,
  authorized_by VARCHAR(36) NOT NULL,
  authorized_by_name VARCHAR(50) NOT NULL,
  authorized_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  is_active BOOLEAN DEFAULT TRUE,
  exercise_id VARCHAR(36) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (authorized_by) REFERENCES users(id),
  FOREIGN KEY (exercise_id) REFERENCES morning_exercises(id),
  UNIQUE KEY unique_student_exercise (student_id, exercise_id)
);
```

### 活动签到码表 (activity_checkin_codes)
```sql
CREATE TABLE activity_checkin_codes (
  id VARCHAR(36) PRIMARY KEY,
  unique_code VARCHAR(100) UNIQUE NOT NULL,
  activity_id VARCHAR(36) NOT NULL,
  activity_name VARCHAR(200) NOT NULL,
  student_id VARCHAR(20) NOT NULL,
  student_name VARCHAR(50) NOT NULL,
  timestamp BIGINT NOT NULL,
  expires_at BIGINT NOT NULL,
  is_valid BOOLEAN DEFAULT TRUE,
  is_used BOOLEAN DEFAULT FALSE,
  used_at TIMESTAMP NULL,
  used_by VARCHAR(36) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_unique_code (unique_code),
  INDEX idx_activity_student (activity_id, student_id),
  INDEX idx_expires_at (expires_at),
  INDEX idx_is_valid (is_valid, is_used)
);
```

### 活动签退码表 (activity_checkout_codes)
```sql
CREATE TABLE activity_checkout_codes (
  id VARCHAR(36) PRIMARY KEY,
  unique_code VARCHAR(100) UNIQUE NOT NULL,
  activity_id VARCHAR(36) NOT NULL,
  activity_name VARCHAR(200) NOT NULL,
  student_id VARCHAR(20) NOT NULL,
  student_name VARCHAR(50) NOT NULL,
  timestamp BIGINT NOT NULL,
  expires_at BIGINT NOT NULL,
  is_valid BOOLEAN DEFAULT TRUE,
  is_used BOOLEAN DEFAULT FALSE,
  used_at TIMESTAMP NULL,
  used_by VARCHAR(36) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_unique_code (unique_code),
  INDEX idx_activity_student (activity_id, student_id),
  INDEX idx_expires_at (expires_at),
  INDEX idx_is_valid (is_valid, is_used)
);
```

### 早操签退码表 (morning_exercise_checkout_codes)
```sql
CREATE TABLE morning_exercise_checkout_codes (
  id VARCHAR(36) PRIMARY KEY,
  unique_code VARCHAR(100) UNIQUE NOT NULL,
  exercise_id VARCHAR(36) NOT NULL,
  exercise_name VARCHAR(200) NOT NULL,
  student_id VARCHAR(20) NOT NULL,
  student_name VARCHAR(50) NOT NULL,
  timestamp BIGINT NOT NULL,
  expires_at BIGINT NOT NULL,
  is_valid BOOLEAN DEFAULT TRUE,
  is_used BOOLEAN DEFAULT FALSE,
  used_at TIMESTAMP NULL,
  used_by VARCHAR(36) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_unique_code (unique_code),
  INDEX idx_exercise_student (exercise_id, student_id),
  INDEX idx_expires_at (expires_at),
  INDEX idx_is_valid (is_valid, is_used)
);
```

### WebSocket连接表 (websocket_connections)
```sql
CREATE TABLE websocket_connections (
  id VARCHAR(36) PRIMARY KEY,
  user_id VARCHAR(36) NOT NULL,
  connection_id VARCHAR(100) NOT NULL,
  connected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_ping_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  is_active BOOLEAN DEFAULT TRUE,
  FOREIGN KEY (user_id) REFERENCES users(id),
  INDEX idx_user_connection (user_id, connection_id),
  INDEX idx_active_connections (is_active, last_ping_at)
);
```

---

## 部署建议

1. **环境配置**:
   - 开发环境: `dev.pe-campus.com`
   - 测试环境: `test.pe-campus.com`
   - 生产环境: `api.pe-campus.com`

2. **监控**:
   - API响应时间监控
   - 错误率监控
   - 数据库性能监控
   - WebSocket连接状态监控
   - 积分计算成功率监控

3. **备份**:
   - 数据库定期备份
   - 用户数据加密存储
   - 积分记录完整备份

---

## 总结

本API文档已根据《签到签退实现逻辑文档》进行了全面更新，确保后端API与实现逻辑完全一致。主要更新包括：

### 核心业务逻辑一致性
1. **积分计算完全后端化**：所有积分计算逻辑由后端统一处理，前端只负责展示
2. **WebSocket实时推送**：积分变动、活动状态更新通过WebSocket实时推送
3. **40秒动态二维码**：所有签到/签退码采用40秒刷新机制
4. **权限控制精确化**：早操扫码权限严格限制为CHECKER和SUB_CHECKER

### 数据模型完整性
1. **用户表增强**：添加分类积分字段、角色字段、积分更新时间
2. **积分记录表**：完整的积分获取历史记录，包含计算规则和参与时长
3. **签到码表**：支持40秒过期机制的动态码管理
4. **WebSocket连接表**：管理实时连接状态和心跳机制

### 安全机制强化
1. **事务保证**：积分计算、发放、记录在同一事务中完成
2. **防重复机制**：分布式锁和唯一性约束防止积分重复发放
3. **权限验证**：严格的角色权限控制和操作权限验证
4. **审计追踪**：完整的操作日志和积分变动记录

### API接口规范化
1. **统一响应格式**：所有接口返回统一的数据结构
2. **错误处理完善**：详细的错误码和错误信息
3. **参数验证严格**：所有输入参数的格式和权限验证
4. **WebSocket协议**：标准化的推送消息格式和连接管理

### 积分系统特点
- **PE活动积分**：`积分 = floor(参与时长(分钟) / 30) * 基础积分`
- **早操积分**：完成签到和签退获得固定1积分
- **实时推送**：积分变动通过WebSocket立即推送给用户
- **数据一致性**：后端计算确保积分公平性和准确性

本API文档现已与实现逻辑文档完全同步，可作为后端开发的权威参考，确保系统实现的一致性和稳定性。 