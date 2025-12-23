# 阳光跑API文档

## 概述
阳光跑系统提供跑步数据上传、个人记录查询和班级管理功能。系统继承原有的角色权限体系，支持普通用户和班级管理员两种角色。

---

## 角色说明

### 1. 普通用户（STUDENT）
- 上传个人阳光跑记录
- 查看个人历史记录
- 查看个人统计数据

### 2. 班级管理员（CHECKER/SUB_CHECKER）
- 查看本班级所有学生的阳光跑记录
- 查看班级跑步排行榜
- 继承普通用户的所有权限

---

## API接口列表

### 1. 上传阳光跑记录
**接口说明**：用户完成跑步后上传跑步数据

**请求方式**：`POST`

**接口路径**：`/sunshine-run/upload`

**权限要求**：需要登录（所有用户）

**请求头**：
```
Authorization: Bearer {access_token}
Content-Type: application/json
```

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| startTime | Long | 是 | 开始时间戳（毫秒） |
| endTime | Long | 是 | 结束时间戳（毫秒） |
| totalDistance | Double | 是 | 总距离（米） |
| totalDuration | Long | 是 | 总时长（毫秒） |
| avgPace | Double | 是 | 平均配速（分钟/公里） |
| calories | Int | 是 | 消耗卡路里 |
| checkPointsCount | Int | 是 | 完成打卡点数量 |
| totalCheckPoints | Int | 是 | 总打卡点数量 |
| pathPoints | Array | 是 | 跑步轨迹点数组 |

**pathPoints数组元素结构**：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| latitude | Double | 是 | 纬度 |
| longitude | Double | 是 | 经度 |

**请求示例**：
```json
{
  "startTime": 1699430400000,
  "endTime": 1699432200000,
  "totalDistance": 2150.5,
  "totalDuration": 1800000,
  "avgPace": 8.37,
  "calories": 180,
  "checkPointsCount": 3,
  "totalCheckPoints": 3,
  "pathPoints": [
    {
      "latitude": 39.9042,
      "longitude": 116.4074
    },
    {
      "latitude": 39.9045,
      "longitude": 116.4078
    }
  ]
}
```

**响应示例**：
```json
{
  "success": true,
  "message": "上传成功",
  "data": {
    "id": "run_12345",
    "userName": "张三",
    "studentId": "2021001",
    "startTime": 1699430400000,
    "endTime": 1699432200000,
    "totalDistance": 2150.5,
    "totalDuration": 1800000,
    "avgPace": 8.37,
    "calories": 180,
    "checkPointsCount": 3,
    "totalCheckPoints": 3,
    "createdAt": 1699432300000
  }
}
```

---

### 2. 获取个人阳光跑记录列表
**接口说明**：获取当前用户的历史跑步记录

**请求方式**：`GET`

**接口路径**：`/sunshine-run/my-records`

**权限要求**：需要登录（所有用户）

**请求头**：
```
Authorization: Bearer {access_token}
```

**查询参数**：
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | Int | 否 | 0 | 页码（从0开始） |
| size | Int | 否 | 20 | 每页记录数 |

**请求示例**：
```
GET /sunshine-run/my-records?page=0&size=20
```

**响应示例**：
```json
{
  "success": true,
  "message": "获取成功",
  "data": [
    {
      "id": "run_12345",
      "userName": "张三",
      "studentId": "2021001",
      "startTime": 1699430400000,
      "endTime": 1699432200000,
      "totalDistance": 2150.5,
      "totalDuration": 1800000,
      "avgPace": 8.37,
      "calories": 180,
      "checkPointsCount": 3,
      "totalCheckPoints": 3,
      "createdAt": 1699432300000
    }
  ]
}
```

---

### 3. 获取个人阳光跑统计信息
**接口说明**：获取当前用户的跑步统计数据

**请求方式**：`GET`

**接口路径**：`/sunshine-run/my-stats`

**权限要求**：需要登录（所有用户）

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
    "totalRuns": 25,
    "totalDistance": 50250.0,
    "totalDuration": 225000000,
    "totalCalories": 4200
  }
}
```

**统计数据说明**：
| 字段名 | 类型 | 说明 |
|--------|------|------|
| totalRuns | Int | 总跑步次数 |
| totalDistance | Double | 总距离（米） |
| totalDuration | Long | 总时长（毫秒） |
| totalCalories | Int | 总消耗卡路里 |

---

### 4. 获取班级阳光跑记录（班级管理员）
**接口说明**：班级管理员查看本班级所有学生的跑步记录

**请求方式**：`GET`

**接口路径**：`/sunshine-run/class-records`

**权限要求**：需要登录，且为班级管理员（CHECKER/SUB_CHECKER）

**请求头**：
```
Authorization: Bearer {access_token}
```

**查询参数**：
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | Int | 否 | 0 | 页码（从0开始） |
| size | Int | 否 | 20 | 每页记录数 |

**请求示例**：
```
GET /sunshine-run/class-records?page=0&size=20
```

**响应示例**：
```json
{
  "success": true,
  "message": "获取成功",
  "data": [
    {
      "id": "run_12345",
      "userName": "张三",
      "studentId": "2021001",
      "startTime": 1699430400000,
      "endTime": 1699432200000,
      "totalDistance": 2150.5,
      "totalDuration": 1800000,
      "avgPace": 8.37,
      "calories": 180,
      "checkPointsCount": 3,
      "totalCheckPoints": 3,
      "createdAt": 1699432300000
    },
    {
      "id": "run_12346",
      "userName": "李四",
      "studentId": "2021002",
      "startTime": 1699440400000,
      "endTime": 1699442100000,
      "totalDistance": 1980.0,
      "totalDuration": 1700000,
      "avgPace": 8.59,
      "calories": 165,
      "checkPointsCount": 3,
      "totalCheckPoints": 3,
      "createdAt": 1699442200000
    }
  ]
}
```

---

### 5. 获取单条阳光跑记录详情
**接口说明**：获取指定跑步记录的详细信息

**请求方式**：`GET`

**接口路径**：`/sunshine-run/records/{id}`

**权限要求**：需要登录（只能查看自己的记录，或班级管理员查看本班学生记录）

**请求头**：
```
Authorization: Bearer {access_token}
```

**路径参数**：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | String | 是 | 跑步记录ID |

**请求示例**：
```
GET /sunshine-run/records/run_12345
```

**响应示例**：
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "id": "run_12345",
    "userName": "张三",
    "studentId": "2021001",
    "startTime": 1699430400000,
    "endTime": 1699432200000,
    "totalDistance": 2150.5,
    "totalDuration": 1800000,
    "avgPace": 8.37,
    "calories": 180,
    "checkPointsCount": 3,
    "totalCheckPoints": 3,
    "createdAt": 1699432300000
  }
}
```

---

### 6. 删除阳光跑记录
**接口说明**：删除指定的跑步记录

**请求方式**：`DELETE`

**接口路径**：`/sunshine-run/records/{id}`

**权限要求**：需要登录（只能删除自己的记录）

**请求头**：
```
Authorization: Bearer {access_token}
```

**路径参数**：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | String | 是 | 跑步记录ID |

**请求示例**：
```
DELETE /sunshine-run/records/run_12345
```

**响应示例**：
```json
{
  "success": true,
  "message": "删除成功",
  "data": null
}
```

---

### 7. 获取班级跑步排行榜（班级管理员）
**接口说明**：班级管理员查看本班级学生的跑步排行榜，按总距离降序排列

**请求方式**：`GET`

**接口路径**：`/sunshine-run/class-ranking`

**权限要求**：需要登录，且为班级管理员（CHECKER/SUB_CHECKER）

**请求头**：
```
Authorization: Bearer {access_token}
```

**请求示例**：
```
GET /sunshine-run/class-ranking
```

**响应示例**：
```json
{
  "success": true,
  "message": "获取成功",
  "data": [
    {
      "userName": "张三",
      "studentId": "2021001",
      "totalDistance": 15250.5,
      "totalRuns": 8,
      "totalDuration": 7200000,
      "rank": 1
    },
    {
      "userName": "李四",
      "studentId": "2021002",
      "totalDistance": 12800.0,
      "totalRuns": 6,
      "totalDuration": 6300000,
      "rank": 2
    },
    {
      "userName": "王五",
      "studentId": "2021003",
      "totalDistance": 10500.0,
      "totalRuns": 5,
      "totalDuration": 5400000,
      "rank": 3
    }
  ]
}
```

**排行榜数据说明**：
| 字段名 | 类型 | 说明 |
|--------|------|------|
| userName | String | 用户姓名 |
| studentId | String | 学号 |
| totalDistance | Double | 总距离（米） |
| totalRuns | Int | 总跑步次数 |
| totalDuration | Long | 总时长（毫秒） |
| rank | Int | 排名（按总距离降序） |

**注意事项**：
- 排行榜按 `totalDistance` 降序排列
- 只返回有跑步记录的学生
- 排名从 1 开始

---

### 8. 查询指定操场坐标
**接口说明**：根据学校和操场名称查询具体坐标位置

**请求方式**：`GET`

**接口路径**：`/sunshine-run/playground-coordinate`

**权限要求**：需要登录（所有用户）

**请求头**：
```
Authorization: Bearer {access_token}
```

**查询参数**：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| school | String | 是 | 学校名称 |
| playgroundName | String | 是 | 操场名称 |

**请求示例**：
```
GET /sunshine-run/playground-coordinate?school=清华大学&playgroundName=东操场
```

**响应示例**：
```json
{
  "success": true,
  "message": "查询成功",
  "data": {
    "school": "清华大学",
    "playgroundName": "东操场",
    "longitude1": 116.326520,
    "latitude1": 40.003690,
    "longitude2": 116.327820,
    "latitude2": 40.003990
  }
}
```

---

### 9. 获取学校和操场下拉列表
**接口说明**：获取所有学校名称以及每个学校包含的操场列表，用于下拉选择

**请求方式**：`GET`

**接口路径**：`/sunshine-run/playground-dropdown`

**权限要求**：需要登录（所有用户）

**请求头**：
```
Authorization: Bearer {access_token}
```

**请求示例**：
```
GET /sunshine-run/playground-dropdown
```

**响应示例**：
```json
{
  "success": true,
  "message": "查询成功",
  "data": [
    {
      "school": "北京大学",
      "playgrounds": [
        {
          "playgroundName": "第一体育场"
        },
        {
          "playgroundName": "五四操场"
        }
      ]
    },
    {
      "school": "清华大学",
      "playgrounds": [
        {
          "playgroundName": "东操场"
        },
        {
          "playgroundName": "西操场"
        }
      ]
    }
  ]
}
```

---

## 错误码说明

| 错误码 | 说明 | 处理建议 |
|--------|------|----------|
| 401 | 未登录或Token过期 | 重新登录获取Token |
| 403 | 权限不足 | 检查用户角色权限 |
| 404 | 记录不存在 | 检查记录ID是否正确 |
| 400 | 请求参数错误 | 检查请求参数格式和必填项 |
| 500 | 服务器内部错误 | 联系技术支持 |

**错误响应格式**：
```json
{
  "success": false,
  "message": "错误描述信息",
  "data": null
}
```

---

## 数据模型说明

### SunshineRunRecord（阳光跑记录）
```typescript
{
  id: string,                    // 记录ID
  userName: string,              // 用户姓名
  studentId: string,             // 学号
  startTime: number,             // 开始时间戳（毫秒）
  endTime: number,               // 结束时间戳（毫秒）
  totalDistance: number,         // 总距离（米）
  totalDuration: number,         // 总时长（毫秒）
  avgPace: number,               // 平均配速（分钟/公里）
  calories: number,              // 消耗卡路里
  checkPointsCount: number,      // 完成打卡点数量
  totalCheckPoints: number,      // 总打卡点数量
  createdAt: number              // 创建时间戳（毫秒）
}
```

### PlaygroundCoordinate（操场坐标）
```typescript
{
  school: string,                // 学校名称
  playgroundName: string,        // 操场名称
  longitude1: number,           // 第一个打卡点经度
  latitude1: number,            // 第一个打卡点纬度
  longitude2: number,           // 第二个打卡点经度
  latitude2: number             // 第二个打卡点纬度
}
```

### PlaygroundDropdown（学校操场下拉列表）
```typescript
{
  school: string,                           // 学校名称
  playgrounds: PlaygroundInfo[]             // 该学校的操场列表
}

interface PlaygroundInfo {
  playgroundName: string        // 操场名称
}
```

---

## 使用场景示例

### 场景1：学生完成跑步后上传数据
1. 学生在APP中完成跑步
2. APP调用上传接口（POST /sunshine-run/upload）
3. 服务器保存数据并返回记录详情
4. APP提示用户上传成功

### 场景2：学生查看个人跑步历史
1. 学生打开"我的跑步记录"页面
2. APP调用个人记录接口（GET /sunshine-run/my-records）
3. 服务器返回记录列表
4. APP展示历史记录列表

### 场景3：班级管理员查看班级数据
1. 班级管理员打开"班级跑步数据"页面
2. APP调用班级记录接口（GET /sunshine-run/class-records）
3. APP调用班级统计接口（GET /sunshine-run/class-stats）
4. 服务器返回班级所有学生的记录和统计数据
5. APP展示班级整体情况和排名

### 场景4：学生查询操场坐标
1. 学生选择学校和操场名称
2. APP调用坐标查询接口（GET /sunshine-run/playground-coordinate）
3. 服务器返回操场的两个打卡点坐标
4. APP在地图上标记两个打卡点位置

---

## 注意事项

1. **权限控制**
   - 普通用户只能访问自己的数据
   - 班级管理员可以访问本班级所有学生的数据
   - 需要在后端验证用户角色和所属班级

2. **数据验证**
   - 时间戳必须合理（startTime < endTime）
   - 距离和时长必须大于0
   - 打卡点数量不能超过总打卡点数

3. **性能优化**
   - 轨迹点数据可能较大，建议限制上传点数（如最多1000个点）
   - 列表查询支持分页，避免一次返回过多数据
   - 建议对班级统计数据进行缓存

4. **数据安全**
   - 所有接口都需要Token认证
   - 敏感数据传输使用HTTPS
   - 删除操作需要二次确认

---

## 技术实现建议

### 后端实现要点
1. 数据库设计：建议使用索引优化查询性能（classId、createdAt等字段）
2. 权限验证：使用拦截器统一验证Token和角色权限
3. 数据统计：定时任务计算班级统计数据，![img.png](img.png)提高查询效率
4. 轨迹数据：考虑使用MongoDB存储轨迹点，或压缩后存储

### 前端集成说明
Android客户端已完成以下集成：
- 数据模型定义（SunshineRunModels.kt）
- API接口定义（ApiService.kt）
- Repository层实现（PERepository.kt）
- ViewModel层方法（MainViewModel.kt）
- UI上传功能（RunResultDialog.kt）

---

## 版本历史

| 版本 | 日期 | 说明 |
|------|------|------|
| 1.0.0 | 2024-11 | 初始版本，实现基础功能 |

---

## 联系方式
如有问题，请联系技术支持团队。

