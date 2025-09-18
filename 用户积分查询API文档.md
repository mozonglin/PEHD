# 用户积分查询API文档

## 接口概述

该接口用于获取当前登录用户的积分信息，包括总积分、活动积分和早操积分。

## 接口详情

**接口地址**: `GET /v1/user/points`

**接口描述**: 获取用户积分信息

**请求方式**: GET

**请求头**:
- `Authorization: Bearer {accessToken}` (必须)

**请求参数**: 无

## 响应格式

**成功响应**:
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "points": 100,              // 总积分
    "peActivityPoints": 70,     // 活动积分
    "morningExercisePoints": 30, // 早操积分
    "integrityScore": 100       // 诚信度
  }
}
```

**失败响应**:
```json
{
  "success": false,
  "message": "错误信息"
}
```

## 响应字段说明

| 字段名 | 类型 | 描述 |
|--------|------|------|
| points | Integer | 用户总积分 |
| peActivityPoints | Integer | PE活动积分总和 |
| morningExercisePoints | Integer | 早操积分总和 |
| integrityScore | Integer | 用户诚信度(默认100分) |

## 错误码说明

- **400**: 请求参数错误或用户不存在
- **401**: Token无效或已过期
- **403**: 权限不足

## 使用示例

### 请求示例
```bash
curl -X GET "http://localhost:8080/v1/user/points" \
  -H "Authorization: Bearer your_access_token_here"
```

### 响应示例
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "points": 145,
    "peActivityPoints": 120,
    "morningExercisePoints": 25,
    "integrityScore": 100
  }
}
```

## 注意事项

1. 该接口需要用户登录认证，必须在请求头中提供有效的访问令牌
2. 积分数据实时同步，调用该接口获取的是最新的积分信息
3. 接口返回的数据仅包含积分相关信息，不包含其他用户信息
4. 所有积分字段均为整数类型，最小值为0