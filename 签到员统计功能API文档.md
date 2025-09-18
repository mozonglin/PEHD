# PE校园签到员统计功能API文档

## 概述
PE校园统计功能为签到员提供班级考勤统计、达标率分析、积分排名等统计数据。**重要：签到员只能查看自己班级的数据**，无法跨班级查看。系统会从`users`表中获取校级管理员设置的目标值进行达标率计算。

**基础URL**: `/statistics`

**权限要求**: 仅限`CHECKER`、`SUB_CHECKER`角色访问

**权限说明**: 签到员只能查看所属班级(`class_name`字段)的数据

**请求头**: `Authorization: Bearer {accessToken}`

---

## 1. 查看指定日期的班级早操考勤记录

**接口**: `GET /statistics/class-attendance`

**描述**: 查询指定日期的早操考勤详细记录，包含缺勤学生。签到员只能查看自己班级的数据。

**权限**: 签到员

**请求参数**:
- `date` (必填): 查询日期，格式: `YYYY-MM-DD`

**请求示例**:
```
GET /statistics/class-attendance?date=2025-09-18
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**响应成功**:
```json
{
  "success": true,
  "message": "查询成功",
  "data": [
    {
      "studentId": "2021001",
      "studentName": "张三",
      "className": "计算机1班",
      "checkInTime": "2025-09-18T06:30:00",
      "checkInLocation": "操场",
      "checkOutTime": "2025-09-18T07:30:00",
      "checkOutLocation": "操场",
      "isCheckedOut": true,
      "pointsEarned": 1,
      "checkedByName": "李签到员",
      "checkedOutByName": "王签到员"
    },
    {
      "studentId": "2021002",
      "studentName": "李四",
      "className": "计算机1班",
      "checkInTime": "2025-09-18T06:35:00",
      "checkInLocation": "操场",
      "checkOutTime": null,
      "checkOutLocation": null,
      "isCheckedOut": false,
      "pointsEarned": 0,
      "checkedByName": "李签到员",
      "checkedOutByName": null
    },
    {
      "studentId": "2021003",
      "studentName": "王五",
      "className": "计算机1班",
      "checkInTime": null,
      "checkInLocation": null,
      "checkOutTime": null,
      "checkOutLocation": null,
      "isCheckedOut": false,
      "pointsEarned": 0,
      "checkedByName": "缺勤",
      "checkedOutByName": null
    }
  ]
}
```

**响应失败**:
```json
{
  "success": false,
  "message": "查询失败: 指定日期没有早操活动",
  "data": null
}
```

**特殊说明**:
- 返回的数据包含已签到和缺勤的学生
- 缺勤学生的`checkInTime`为`null`，`checkedByName`为"缺勤"
- 数据按签到状态排序：已签到的在前，缺勤的在后

---

## 2. 获取本班级学生达标情况

**接口**: `GET /statistics/student-compliance`

**描述**: 获取签到员所在班级的学生积分达标情况，对比管理员在`users`表中设置的目标值

**权限**: 签到员

**请求参数**: 无（自动获取签到员所在班级）

**请求示例**:
```
GET /statistics/student-compliance
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**响应成功**:
```json
{
  "success": true,
  "message": "查询成功",
  "data": [
    {
      "studentId": "2021001",
      "studentName": "张三",
      "className": "计算机1班",
      "totalPoints": 120,
      "peActivityPoints": 70,
      "morningExercisePoints": 50,
      "weeklyTarget": 10,
      "monthlyTarget": 40,
      "totalTarget": 100,
      "weeklyComplianceRate": 85.7,
      "monthlyComplianceRate": 92.3,
      "totalComplianceRate": 120.0,
      "averageWeeklyPoints": 8.57,
      "averageMonthlyPoints": 36.92
    },
    {
      "studentId": "2021002",
      "studentName": "李四",
      "className": "计算机1班",
      "totalPoints": 85,
      "peActivityPoints": 45,
      "morningExercisePoints": 40,
      "weeklyTarget": 10,
      "monthlyTarget": 40,
      "totalTarget": 100,
      "weeklyComplianceRate": 60.7,
      "monthlyComplianceRate": 68.5,
      "totalComplianceRate": 85.0,
      "averageWeeklyPoints": 6.07,
      "averageMonthlyPoints": 27.4
    }
  ]
}
```

**响应失败**:
```json
{
  "success": false,
  "message": "查询失败: 用户班级信息缺失，无法查询学生达标情况",
  "data": null
}
```

---

## 3. 获取班级积分排名

**接口**: `GET /statistics/points-ranking`

**描述**: 获取签到员所在班级的学生积分排名

**权限**: 签到员

**请求参数**:
- `rankType` (可选): 排名类型，默认`total`
  - `total`: 总积分排名
  - `activity`: PE活动积分排名
  - `morning`: 早操积分排名

**请求示例**:
```
GET /statistics/points-ranking?rankType=total
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**响应成功**:
```json
{
  "success": true,
  "message": "查询成功",
  "data": [
    {
      "rank": 1,
      "studentId": "2021001",
      "studentName": "张三",
      "className": "计算机1班",
      "totalPoints": 150,
      "peActivityPoints": 80,
      "morningExercisePoints": 70
    },
    {
      "rank": 2,
      "studentId": "2021003",
      "studentName": "王五",
      "className": "计算机1班",
      "totalPoints": 145,
      "peActivityPoints": 85,
      "morningExercisePoints": 60
    },
    {
      "rank": 3,
      "studentId": "2021002",
      "studentName": "李四",
      "className": "计算机1班",
      "totalPoints": 120,
      "peActivityPoints": 70,
      "morningExercisePoints": 50
    }
  ]
}
```

**响应失败**:
```json
{
  "success": false,
  "message": "查询失败: 无效的排名类型，支持: total, activity, morning",
  "data": null
}
```

---

## 数据字段说明

### ClassAttendanceRecordDto (班级考勤记录)
- `studentId`: 学号
- `studentName`: 学生姓名
- `className`: 班级名称
- `checkInTime`: 签到时间（缺勤时为null）
- `checkInLocation`: 签到地点（缺勤时为null）
- `checkOutTime`: 签退时间（未签退或缺勤时为null）
- `checkOutLocation`: 签退地点（未签退或缺勤时为null）
- `isCheckedOut`: 是否已签退
- `pointsEarned`: 获得积分
- `checkedByName`: 签到员姓名（缺勤时为"缺勤"）
- `checkedOutByName`: 签退操作员姓名（未签退或缺勤时为null）

### StudentComplianceDto (学生达标情况)
- `studentId`: 学号
- `studentName`: 学生姓名
- `className`: 班级名称
- `totalPoints`: 总积分
- `peActivityPoints`: PE活动积分
- `morningExercisePoints`: 早操积分
- `weeklyTarget`: 周目标积分（来自管理员设置）
- `monthlyTarget`: 月目标积分（来自管理员设置）
- `totalTarget`: 总目标积分（来自管理员设置）
- `weeklyComplianceRate`: 周达标率（百分比，基于平均周积分）
- `monthlyComplianceRate`: 月达标率（百分比，基于平均月积分）
- `totalComplianceRate`: 总达标率（百分比，基于总积分）
- `averageWeeklyPoints`: 平均周积分（从账户创建日期开始计算）
- `averageMonthlyPoints`: 平均月积分（从账户创建日期开始计算）

### PointsRankingDto (积分排名)
- `rank`: 班级内排名
- `studentId`: 学号
- `studentName`: 学生姓名
- `className`: 班级名称
- `totalPoints`: 总积分
- `peActivityPoints`: PE活动积分
- `morningExercisePoints`: 早操积分

---

## 错误码说明

| 错误类型 | HTTP状态码 | 错误信息 |
|---------|-----------|---------|
| 权限不足 | 400 | 权限不足，仅签到员可以查看... |
| 参数错误 | 400 | 无效的排名类型，支持: total, activity, morning |
| 数据不存在 | 400 | 指定日期没有早操活动 |
| 班级信息缺失 | 400 | 用户班级信息缺失，无法查询... |
| 认证失败 | 400 | 用户未登录 |

---

## 权限控制机制

### 身份验证
1. 所有接口都需要有效的JWT Token
2. Token中包含用户身份和角色信息
3. 系统自动验证用户角色是否为签到员

### 数据权限
1. **班级限制**: 签到员只能查看自己班级(`class_name`字段)的数据
2. **自动过滤**: 后端自动根据签到员的班级信息过滤数据
3. **无需参数**: 不需要在请求中传递班级参数，系统自动获取

### 目标设置来源
1. **管理员目标**: 从`users`表中获取`user_type`为`school_admin`的用户设置
2. **学校匹配**: 通过学校名称匹配对应的管理员目标
3. **默认值**: 如果未找到管理员设置，目标值默认为0

---

## 使用场景示例

### 场景1：查看今日早操考勤
签到员想查看今天本班级的早操考勤情况：
```
GET /statistics/class-attendance?date=2025-09-18
```
返回本班级所有学生的考勤状态，包括已签到、未签退和缺勤的学生。

### 场景2：查看班级学生达标情况
签到员想了解班级学生的积分达标情况：
```
GET /statistics/student-compliance
```
返回本班级每个学生的积分详情和达标状态。

### 场景3：查看班级积分排名
签到员想查看班级总积分排名：
```
GET /statistics/points-ranking?rankType=total
```
返回本班级学生按总积分排序的排名列表。

---

## 达标率计算机制

### 计算方式
1. **起算日期**: 从学生账户创建日期(`created_at`字段)开始计算
2. **平均周积分**: 总积分 ÷ (账户存在天数 ÷ 7)
3. **平均月积分**: 总积分 ÷ (账户存在天数 ÷ 30)
4. **达标率公式**:
   - 周达标率 = (平均周积分 ÷ 周目标积分) × 100%
   - 月达标率 = (平均月积分 ÷ 月目标积分) × 100%
   - 总达标率 = (总积分 ÷ 总目标积分) × 100%

### 特殊处理
- 达标率最高显示100%，不会超过100%
- 如果目标为0，达标率显示为0%
- 最小计算周期为1周/1月，避免除零错误

---

## 注意事项

1. **权限限制**: 签到员无法查看其他班级的数据，确保数据安全
2. **缺勤标记**: 早操考勤会显示所有班级学生，包括缺勤者
3. **实时数据**: 所有统计数据都是实时计算，反映当前最新状态
4. **目标依赖**: 达标率计算依赖管理员在独立系统中设置的目标值
5. **班级信息**: 用户的班级信息(`class_name`)必须正确设置，否则无法使用统计功能
6. **时间基准**: 达标率计算基于学生账户创建时间，确保公平性

---

## 数据库依赖

- **学生数据**: `users1`表（PE校园系统）
- **目标设置**: `users`表（管理系统）
- **考勤记录**: `morning_exercise_attendance`表
- **早操活动**: `morning_exercises`表
