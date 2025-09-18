# PE校园统计功能API文档

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
      "weeklyCompliant": true,
      "monthlyCompliant": true,
      "totalCompliant": true,
      "weeklyTarget": 10,
      "monthlyTarget": 40,
      "totalTarget": 100,
      "weeklyGap": 0,
      "monthlyGap": 0,
      "totalGap": 0
    },
    {
      "studentId": "2021002",
      "studentName": "李四",
      "className": "计算机1班",
      "totalPoints": 85,
      "peActivityPoints": 45,
      "morningExercisePoints": 40,
      "weeklyCompliant": true,
      "monthlyCompliant": true,
      "totalCompliant": false,
      "weeklyTarget": 10,
      "monthlyTarget": 40,
      "totalTarget": 100,
      "weeklyGap": 0,
      "monthlyGap": 0,
      "totalGap": 15
    }
  ]
}
```

**响应失败**:
```json
{
  "success": false,
  "message": "查询失败: 未找到该学校的目标设置",
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
      "studentId": "2021001",
      "studentName": "张三",
      "className": "计算机1班",
      "totalPoints": 120,
      "peActivityPoints": 70,
      "morningExercisePoints": 50,
      "weeklyCompliant": true,
      "monthlyCompliant": true,
      "totalCompliant": true,
      "weeklyTarget": 10,
      "monthlyTarget": 40,
      "totalTarget": 100,
      "weeklyGap": 0,
      "monthlyGap": 0,
      "totalGap": 0
    },
    {
      "studentId": "2021002",
      "studentName": "李四",
      "className": "计算机1班",
      "totalPoints": 85,
      "peActivityPoints": 45,
      "morningExercisePoints": 40,
      "weeklyCompliant": true,
      "monthlyCompliant": true,
      "totalCompliant": false,
      "weeklyTarget": 10,
      "monthlyTarget": 40,
      "totalTarget": 100,
      "weeklyGap": 0,
      "monthlyGap": 0,
      "totalGap": 15
    }
  ]
}
```

**响应失败**:
```json
{
  "success": false,
  "message": "查询失败: 未找到该学校的目标设置",
  "data": null
}
```

---

## 4. 获取积分排名

**接口**: `GET /statistics/points-ranking`

**描述**: 获取指定学校的学生积分排名

**权限**: 签到员、管理员

**请求参数**:
- `school` (必填): 学校名称
- `rankType` (可选): 排名类型，默认`total`
  - `total`: 总积分排名
  - `activity`: PE活动积分排名
  - `morning`: 早操积分排名
- `page` (可选): 页码，默认1
- `limit` (可选): 每页数量，默认20

**请求示例**:
```
GET /statistics/points-ranking?school=某大学&rankType=total&page=1&limit=10
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
      "className": "计算机2班",
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
- `checkInTime`: 签到时间
- `checkInLocation`: 签到地点
- `checkOutTime`: 签退时间（可为null）
- `checkOutLocation`: 签退地点（可为null）
- `isCheckedOut`: 是否已签退
- `pointsEarned`: 获得积分
- `checkedByName`: 签到员姓名
- `checkedOutByName`: 签退操作员姓名（可为null）

### ClassComplianceStatsDto (班级达标率统计)
- `className`: 班级名称
- `totalStudents`: 班级总人数
- `weeklyCompliantStudents`: 周目标达标人数
- `monthlyCompliantStudents`: 月目标达标人数
- `totalCompliantStudents`: 总目标达标人数
- `weeklyComplianceRate`: 周达标率（百分比）
- `monthlyComplianceRate`: 月达标率（百分比）
- `totalComplianceRate`: 总达标率（百分比）
- `weeklyTarget`: 周目标积分（来自管理员设置）
- `monthlyTarget`: 月目标积分（来自管理员设置）
- `totalTarget`: 总目标积分（来自管理员设置）

### StudentComplianceDto (学生达标情况)
- `studentId`: 学号
- `studentName`: 学生姓名
- `className`: 班级名称
- `totalPoints`: 总积分
- `peActivityPoints`: PE活动积分
- `morningExercisePoints`: 早操积分
- `weeklyCompliant`: 是否达到周目标
- `monthlyCompliant`: 是否达到月目标
- `totalCompliant`: 是否达到总目标
- `weeklyTarget`: 周目标积分
- `monthlyTarget`: 月目标积分
- `totalTarget`: 总目标积分
- `weeklyGap`: 距离周目标的差距
- `monthlyGap`: 距离月目标的差距
- `totalGap`: 距离总目标的差距

### PointsRankingDto (积分排名)
- `rank`: 排名
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
| 权限不足 | 400 | 权限不足，仅签到员和管理员可以查看... |
| 参数错误 | 400 | 无效的排名类型，支持: total, activity, morning |
| 数据不存在 | 400 | 指定日期没有早操活动 |
| 配置缺失 | 400 | 未找到该学校的目标设置 |
| 认证失败 | 400 | 用户未登录 |

---

## 系统说明

### 目标设置机制
1. 校级管理员在独立的管理后端系统中设置`weekly_target`、`monthly_target`、`total_target`
2. 这些目标保存在`users`表中，`user_type`为`school_admin`
3. 统计接口会自动查询对应学校的管理员目标设置
4. 基于用户当前总积分与目标值比较计算达标情况

### 权限控制
- 所有统计接口仅对签到员(`CHECKER`)、二级管理员(`SUB_CHECKER`)、管理员(`ADMIN`)开放
- 普通学生(`STUDENT`)无法访问统计功能

### 数据源说明
- 学生数据来源：`users1`表（PE校园系统）
- 目标设置来源：`users`表（管理系统）
- 考勤记录来源：`morning_exercise_attendance`表
- 积分数据来源：`users1`表中的积分字段

---

## 注意事项

1. **跨表查询**: 系统需要连接两个数据源（`pe`数据库的`users1`表和管理系统的`users`表）
2. **目标匹配**: 通过学校名称(`school`)匹配管理员设置的目标
3. **达标计算**: 当前基于总积分进行达标判断，可根据业务需求调整为不同积分类型
4. **分页支持**: 排名接口支持分页，避免数据量过大影响性能
5. **实时性**: 统计数据基于当前数据库状态实时计算，无缓存机制
