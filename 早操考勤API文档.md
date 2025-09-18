# 早操考勤API文档

## 概述

早操考勤系统是PE校园管理系统的专门模块，采用基于二维码的签到签退机制。系统支持动态40秒刷新的安全二维码，完整的权限控制体系，以及后端统一的积分计算发放机制。

## 认证说明

所有API接口都需要在请求头中包含JWT Token：
```
Authorization: Bearer {accessToken}
```

## 早操考勤活动管理

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
    {
      "id": "morning_exercise_123",
      "title": "早操考勤",
      "description": "每日早操考勤",
      "location": "操场",
      "date": "2024-01-15T00:00:00Z",
      "startTime": "2024-01-15T06:30:00Z",
      "endTime": "2024-01-15T07:30:00Z",
      "type": "MORNING_EXERCISE",
      "isActive": false,
      "totalParticipants": 300,
      "checkedInCount": 280,
      "createdBy": "admin_123",
      "createdAt": "2024-01-14T20:00:00Z"
    }
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
    "id": "morning_exercise_123",
    "title": "早操考勤",
    "description": "每日早操考勤",
    "location": "操场",
    "date": "2024-01-15T00:00:00Z",
    "startTime": "2024-01-15T06:30:00Z",
    "endTime": "2024-01-15T07:30:00Z",
    "type": "MORNING_EXERCISE",
    "isActive": true,
    "totalParticipants": 0,
    "checkedInCount": 0,
    "createdBy": "admin_123",
    "createdAt": "2024-01-14T20:00:00Z"
  }
}
```

## 早操签到流程

### 4. 请求早操签到码

**接口**: `POST /morning-exercise/request-checkin-code`

**描述**: 用户请求获取早操考勤活动的签到二维码

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

**验证逻辑**:
1. **用户验证**: 检查用户是否已登录且为学生身份
2. **活动状态验证**: 早操必须存在且处于活跃状态
3. **时间验证**: 当前时间必须在早操开始时间范围内
4. **生成唯一码**: 包含早操信息、用户信息和时间戳的唯一标识
5. **有效期设置**: 二维码有效期为40秒

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "uniqueCode": "ME_CHECKIN_12345678",
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

**错误响应示例**:
```json
{
  "success": false,
  "message": "早操尚未开始或已结束"
}
```

### 5. 验证早操签到码

**接口**: `POST /morning-exercise/validate-checkin-code`

**描述**: 签到员或二级管理员扫描学生二维码进行签到

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "uniqueCode": "ME_CHECKIN_12345678"
}
```

**验证逻辑**:
1. **权限验证**: 检查操作者是否为签到员(CHECKER)或二级管理员(SUB_CHECKER)
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
  "message": "签到成功！张三 已完成早操考勤签到"
}
```

**失败响应示例**:
```json
{
  "success": false,
  "message": "签到码已过期，请重新获取"
}
```

其他可能的错误信息：
- "权限不足，仅签到员或二级管理员可以扫码"
- "签到码格式错误"
- "该学生已经签到过了"
- "早操活动不存在或已结束"

## 早操签退流程

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

**验证逻辑**:
1. **用户验证**: 检查用户是否已登录
2. **签到状态验证**: 用户必须已经完成早操签到
3. **活动状态验证**: 早操必须处于可签退状态（活动进行中或结束后30分钟内）
4. **生成唯一码**: 包含早操信息、用户信息和时间戳的唯一标识
5. **有效期设置**: 二维码有效期为40秒

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

**错误响应示例**:
```json
{
  "success": false,
  "message": "该学生尚未签到，无法签退"
}
```

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
  "message": "签退成功！张三 已完成早操考勤，获得1积分",
  "data": {
    "pointsEarned": 1,
    "totalPoints": 156,
    "calculationRule": "早操完成签到签退固定获得1积分",
    "earnedReason": "早操考勤完成",
    "participationDuration": 60,
    "calculatedAt": "2024-01-15T07:30:00Z"
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

其他可能的错误信息：
- "该学生尚未签到，无法签退"
- "该学生已经签退过了"
- "权限不足，仅签到员或二级管理员可以扫码"

## 早操权限管理

### 8. 扫码授权二级管理员

**接口**: `POST /morning-exercise/scan-authorize`

**描述**: 签到员扫描学生二维码，授权其为二级管理员

**请求头**: `Authorization: Bearer {accessToken}`

**请求体**:
```json
{
  "targetStudentId": "2021002",
  "exerciseId": "morning_exercise_123"
}
```

**验证逻辑**:
1. **权限验证**: 只有签到员(CHECKER)或管理员(ADMIN)可以授权
2. **目标用户验证**: 确认被授权用户存在且为学生身份
3. **活动验证**: 确认早操活动存在且有效
4. **重复授权检查**: 防止重复授权

**成功响应**:
```json
{
  "success": true,
  "message": "授权成功！李四 已成为二级管理员"
}
```

### 9. 获取早操考勤权限

**接口**: `GET /morning-exercise/user-permissions`

**描述**: 获取用户在早操考勤中的权限信息

**请求头**: `Authorization: Bearer {accessToken}`

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": [
    {
      "id": "auth_123",
      "exerciseId": "morning_exercise_123",
      "exerciseName": "早操考勤",
      "authorizedBy": "admin_001",
      "authorizedAt": "2024-01-15T06:00:00Z",
      "role": "SUB_CHECKER",
      "isActive": true
    }
  ]
}
```

### 10. 获取用户在早操中的角色

**接口**: `GET /morning-exercise/{id}/user-role`

**描述**: 获取用户在指定早操考勤活动中的角色

**请求头**: `Authorization: Bearer {accessToken}`

**路径参数**:
- `id`: 早操考勤活动ID

**响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "role": "SUB_CHECKER",
    "exerciseId": "morning_exercise_123",
    "userId": "user_123"
  }
}
```

## 早操考勤统计

### 11. 获取早操考勤记录

**接口**: `GET /morning-exercise/{id}/attendance`

**描述**: 获取指定早操考勤活动的考勤记录

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
      "id": "attendance_123",
      "exerciseId": "morning_exercise_123",
      "studentId": "2021001",
      "studentName": "张三",
      "checkInTime": "2024-01-15T06:35:00Z",
      "checkInLocation": "操场",
      "checkedBy": "checker_001",
      "checkedByName": "李老师",
      "checkOutTime": "2024-01-15T07:25:00Z",
      "checkOutLocation": "操场",
      "checkedOutBy": "checker_001",
      "checkedOutByName": "李老师",
      "isCheckedOut": true,
      "pointsEarned": 1,
      "calculationRule": "早操完成签到签退固定获得1积分",
      "earnedAt": "2024-01-15T07:25:00Z"
    }
  ]
}
```

### 12. 获取早操考勤统计

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
    "exerciseName": "早操考勤",
    "totalParticipants": 300,
    "checkedInCount": 280,
    "checkedOutCount": 275,
    "completionRate": 91.67,
    "onTimeRate": 88.33,
    "totalPointsAwarded": 275,
    "averageParticipationTime": 55,
    "statsGeneratedAt": "2024-01-15T08:00:00Z"
  }
}
```

## 角色权限说明

### 权限体系

| 角色 | 权限 | 说明 |
|------|------|------|
| STUDENT | 生成二维码 | 普通学生，只能生成签到/签退码 |
| SUB_CHECKER | 扫码签到/签退 | 二级管理员，可以扫码但不能授权 |
| CHECKER | 扫码签到/签退/授权 | 签到员，可以扫码和授权其他学生 |
| ADMIN | 全部权限 | 管理员，可以管理早操活动 |

### 权限获取方式

1. **STUDENT**: 系统默认角色，所有注册用户默认为学生
2. **SUB_CHECKER**: 由签到员(CHECKER)或管理员(ADMIN)通过扫码授权获得
3. **CHECKER**: 由管理员手动指定
4. **ADMIN**: 系统管理员权限

## 积分计算说明

### 早操积分规则

- **固定积分**: 每次完成早操签到+签退获得1积分
- **计算时机**: 签退验证成功后立即计算和发放
- **计算方式**: 后端统一处理，前端无需参与计算逻辑
- **发放机制**: 通过WebSocket实时推送积分变动

### 积分计算流程

1. 学生完成签退 → 后端验证签到签退完整性
2. 后端自动计算积分（固定1分）
3. 积分发放到用户账户
4. 通过WebSocket推送积分更新给学生
5. 前端实时显示积分变动

## WebSocket推送消息

### 积分更新推送

```json
{
  "type": "POINTS_UPDATE",
  "userId": "user_123",
  "pointsEarned": 1,
  "totalPoints": 156,
  "activityId": "morning_exercise_123",
  "activityName": "早操考勤",
  "activityType": "MORNING_EXERCISE",
  "earnedReason": "早操考勤完成",
  "calculationRule": "早操完成签到签退固定获得1积分",
  "participationDuration": 60
}
```

### 早操状态更新推送

```json
{
  "type": "MORNING_EXERCISE_UPDATE",
  "userId": "user_123",
  "exerciseId": "morning_exercise_123",
  "exerciseName": "早操考勤",
  "updateType": "CHECK_OUT",
  "updatedExercise": {
    "id": "morning_exercise_123",
    "title": "早操考勤",
    "checkedInCount": 280,
    "isActive": true
  }
}
```

## 错误码说明

| 错误码 | 错误信息 | 说明 |
|--------|----------|------|
| 4001 | 签到码已过期 | 二维码超过40秒有效期 |
| 4002 | 签到码已被使用 | 防止重复使用同一签到码 |
| 4003 | 权限不足 | 用户角色不满足操作要求 |
| 4004 | 早操不存在 | 早操活动ID无效或已删除 |
| 4005 | 重复签到 | 同一学生重复签到 |
| 4006 | 尚未签到 | 签退前必须先完成签到 |
| 4007 | 重复签退 | 同一学生重复签退 |
| 4008 | 早操尚未开始 | 当前时间不在早操时间范围内 |
| 4009 | 早操已结束 | 早操已结束且超过签退时限 |
| 5001 | 积分计算失败 | 后端积分计算异常 |
| 5002 | 积分发放失败 | 积分发放到账户失败 |
| 5003 | WebSocket推送失败 | 实时推送消息失败 |

## 技术特点

### 安全机制

1. **动态二维码**: 40秒自动刷新，防止截图滥用
2. **权限验证**: 基于角色的严格权限控制
3. **防重放攻击**: 唯一码机制和时效性验证
4. **JWT认证**: 所有接口都需要有效Token

### 性能优化

1. **Redis缓存**: 二维码信息缓存，提高验证速度
2. **WebSocket推送**: 实时状态更新，减少轮询请求
3. **后端计算**: 积分计算在后端完成，确保一致性
4. **异步处理**: 积分计算和推送采用异步机制

### 用户体验

1. **自动刷新**: 二维码自动刷新，无需手动操作
2. **实时反馈**: 签到签退结果立即反馈
3. **积分推送**: 积分变动实时推送，及时感知
4. **错误提示**: 详细的错误信息，便于问题排查

---

*文档更新时间: 2024年1月*
*API版本: v1.0*
