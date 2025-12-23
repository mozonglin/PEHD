# 课后作业成绩API文档

## API接口列表

### 1. 上传课后作业成绩
**请求方式**：`POST`

**接口路径**：`/homework-scores/upload`

**权限要求**：需要登录

**请求头**：
```
Authorization: Bearer {access_token}
Content-Type: application/json
```

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| studentId | String | 是 | 学号 |
| exerciseType | String | 是 | 项目类型：SQUAT/SIT_UP/PUSH_UP/PULL_UP/JUMP_ROPE |
| count | Int | 是 | 次数 |
| timestamp | String | 是 | 提交时间（ISO 8601格式，如：2024-12-23T10:30:00+08:00） |

**请求示例**：
```json
{
  "studentId": "2021001",
  "exerciseType": "SQUAT",
  "count": 50,
  "timestamp": "2024-12-23T10:30:00+08:00"
}
```

**响应示例**：
```json
{
  "success": true,
  "message": "上传成功",
  "data": {
    "id": "score_12345",
    "studentId": "2021001",
    "exerciseType": "SQUAT",
    "count": 50,
    "timestamp": "2024-12-23T10:30:00+08:00"
  }
}
```

---

### 2. 获取我的作业成绩列表
**请求方式**：`GET`

**接口路径**：`/homework-scores/my-scores`

**权限要求**：需要登录

**请求头**：
```
Authorization: Bearer {access_token}
```

**查询参数**：
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| exerciseType | String | 否 | null | 项目类型筛选：SQUAT/SIT_UP/PUSH_UP/PULL_UP/JUMP_ROPE |
| page | Int | 否 | 0 | 页码（从0开始） |
| size | Int | 否 | 20 | 每页记录数 |

**请求示例**：
```
GET /homework-scores/my-scores?exerciseType=SQUAT&page=0&size=20
```

**响应示例**：
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "total": 15,
    "scores": [
      {
        "id": "score_12345",
        "studentId": "2021001",
        "exerciseType": "SQUAT",
        "count": 50,
        "timestamp": "2024-12-23T10:30:00+08:00"
      },
      {
        "id": "score_12346",
        "studentId": "2021001",
        "exerciseType": "SQUAT",
        "count": 48,
        "timestamp": "2024-12-22T09:15:00+08:00"
      }
    ]
  }
}
```

---

### 3. 获取我的作业成绩统计
**请求方式**：`GET`

**接口路径**：`/homework-scores/my-stats`

**权限要求**：需要登录

**请求头**：
```
Authorization: Bearer {access_token}
```

**响应示例**：
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "totalSubmissions": 25,
    "squat": {
      "count": 5,
      "bestCount": 50,
      "lastCount": 48,
      "lastTime": "2024-12-23T10:30:00+08:00"
    },
    "sitUp": {
      "count": 5,
      "bestCount": 60,
      "lastCount": 58,
      "lastTime": "2024-12-22T14:20:00+08:00"
    },
    "pushUp": {
      "count": 5,
      "bestCount": 40,
      "lastCount": 38,
      "lastTime": "2024-12-21T16:10:00+08:00"
    },
    "pullUp": {
      "count": 5,
      "bestCount": 15,
      "lastCount": 14,
      "lastTime": "2024-12-20T11:00:00+08:00"
    },
    "jumpRope": {
      "count": 5,
      "bestCount": 200,
      "lastCount": 190,
      "lastTime": "2024-12-19T15:30:00+08:00"
    }
  }
}
```

---

### 4. 获取最佳成绩
**请求方式**：`GET`

**接口路径**：`/homework-scores/best`

**权限要求**：需要登录

**请求头**：
```
Authorization: Bearer {access_token}
```

**查询参数**：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| exerciseType | String | 是 | 项目类型：SQUAT/SIT_UP/PUSH_UP/PULL_UP/JUMP_ROPE |

**请求示例**：
```
GET /homework-scores/best?exerciseType=SQUAT
```

**响应示例**：
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "studentId": "2021001",
    "exerciseType": "SQUAT",
    "bestCount": 50,
    "timestamp": "2024-12-23T10:30:00+08:00"
  }
}
```
## 4. 获取班级所有人课后作业次数

**接口**: `GET /statistics/homework-scores`

**描述**: 获取签到员所在班级所有学生的课后作业次数记录，用于前端进行项目排名统计。返回所有项目的所有记录，前端根据日期和项目进行筛选和排名计算。

**权限**: 签到员

**请求参数**:
- `date` (可选): 查询日期，格式: `YYYY-MM-DD`。如果不传或传null，返回所有日期的记录

**请求示例**:
```
GET /statistics/homework-scores
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

```
GET /statistics/homework-scores?date=2024-12-23
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**响应成功**:
```json
{
  "success": true,
  "message": "查询成功",
  "data": [
    {
      "id": "score_12345",
      "studentId": "2021001",
      "studentName": "张三",
      "className": "计算机1班",
      "exerciseType": "SQUAT",
      "count": 50,
      "timestamp": "2024-12-23T10:30:00+08:00"
    },
    {
      "id": "score_12346",
      "studentId": "2021001",
      "studentName": "张三",
      "className": "计算机1班",
      "exerciseType": "SIT_UP",
      "count": 60,
      "timestamp": "2024-12-23T14:20:00+08:00"
    },
    {
      "id": "score_12347",
      "studentId": "2021002",
      "studentName": "李四",
      "className": "计算机1班",
      "exerciseType": "SQUAT",
      "count": 48,
      "timestamp": "2024-12-23T09:15:00+08:00"
    },
    {
      "id": "score_12348",
      "studentId": "2021002",
      "studentName": "李四",
      "className": "计算机1班",
      "exerciseType": "PUSH_UP",
      "count": 40,
      "timestamp": "2024-12-22T16:10:00+08:00"
    }
  ]
}
```

**响应失败**:
```json
{
  "success": false,
  "message": "查询失败: 权限不足，仅签到员可以查看",
  "data": null
}
```

**说明**:
- 返回的数据包含签到员所在班级所有学生的所有项目记录
- 前端需要根据`date`参数和`exerciseType`进行筛选
- 前端需要按项目分组，计算每个学生的总次数，然后进行排名
- `exerciseType`可选值：`SQUAT`（深蹲）、`SIT_UP`（仰卧起坐）、`PUSH_UP`（俯卧撑）、`PULL_UP`（引体向上）、`JUMP_ROPE`（跳绳）


---

## 数据模型说明

### HomeworkScore（作业成绩记录）
```json
{
  "id": "string",           // 记录ID
  "studentId": "string",     // 学号
  "exerciseType": "string",  // 项目类型：SQUAT/SIT_UP/PUSH_UP/PULL_UP/JUMP_ROPE
  "count": 0,                // 次数
  "timestamp": "string"      // 提交时间（ISO 8601格式）
}
```

### ExerciseStats（单项运动统计）
```json
{
  "count": 0,                // 提交次数
  "bestCount": 0,            // 最佳次数
  "lastCount": 0,            // 最近一次次数
  "lastTime": "string"       // 最近一次时间（ISO 8601格式，可为null）
}
```

### HomeworkScoreStats（作业成绩统计）
```json
{
  "totalSubmissions": 0,     // 总提交次数
  "squat": {},               // 深蹲统计（ExerciseStats）
  "sitUp": {},               // 仰卧起坐统计（ExerciseStats）
  "pushUp": {},              // 俯卧撑统计（ExerciseStats）
  "pullUp": {},              // 引体向上统计（ExerciseStats）
  "jumpRope": {}             // 跳绳统计（ExerciseStats）
}
```

---

## 错误响应格式

```json
{
  "success": false,
  "message": "错误描述信息",
  "data": null
}
```

