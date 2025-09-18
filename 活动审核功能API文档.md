# 活动审核功能API文档

## 概述

本文档描述了PE校园应用中活动审核功能的API接口规范。此功能为"我发布的活动"添加了审核状态管理。

**基础URL**: `https://api.pe-campus.com/v1`

---

## 1. 数据模型更新

### 1.1 活动审核状态枚举

```typescript
enum ActivityApprovalStatus {
    PENDING = "PENDING",     // 审核中
    APPROVED = "APPROVED",   // 已审核通过
    REJECTED = "REJECTED",   // 已拒绝
    DRAFT = "DRAFT"         // 草稿（未提交审核）
}
```

### 1.2 PEActivity模型新增字段

```json
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
  "imageUrl": "活动图片URL",
  
  // 新增审核相关字段
  "approvalStatus": "APPROVED",           // 审核状态
  "reviewedBy": "admin_001",              // 审核人ID
  "reviewedAt": "2024-01-01T08:00:00Z",   // 审核时间
  "reviewComment": "活动内容符合要求"        // 审核意见
}
```

---

## 2. 接口更新

### 2.1 创建活动接口更新

**接口**: `POST /activities`

**新增参数**:
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
  "points": 10,
  "isDraft": false  // 新增：是否保存为草稿
}
```

**参数说明**:
- `isDraft`: 是否保存为草稿
  - `false`: 提交审核（默认值），活动状态为`PENDING`
  - `true`: 保存为草稿，活动状态为`DRAFT`

**响应示例**:
```json
{
  "success": true,
  "message": "活动创建成功，等待审核",
  "data": {
    "id": "activity_456",
    "title": "跑步活动",
    "approvalStatus": "PENDING",
    "reviewedBy": "",
    "reviewedAt": null,
    "reviewComment": ""
  }
}
```

### 2.2 更新活动/提交草稿审核接口

**接口**: `PUT /activities/{id}`

**描述**: 更新活动信息或将草稿提交审核（仅活动创建者可操作）

**权限要求**: 仅活动创建者可操作，且活动状态必须为`DRAFT`或`REJECTED`

**请求体**:
```json
{
  "title": "跑步活动（修改版）",
  "description": "晨跑活动，强身健体",
  "location": "操场",
  "maxParticipants": 60,
  "registrationStartTime": "2024-01-02T09:00:00Z",
  "registrationEndTime": "2024-01-06T18:00:00Z",
  "activityStartTime": "2024-01-07T06:30:00Z",
  "activityEndTime": "2024-01-07T07:30:00Z",
  "category": "跑步",
  "points": 12,
  "isDraft": false  // false表示提交审核，true表示继续保存为草稿
}
```

**响应**:
```json
{
  "success": true,
  "message": "活动更新成功，已提交审核",
  "data": {
    "id": "activity_456",
    "title": "跑步活动（修改版）",
    "approvalStatus": "PENDING",
    "reviewedBy": "",
    "reviewedAt": null,
    "reviewComment": ""
  }
}
```

---

## 3. 业务逻辑更新

### 3.1 活动报名验证逻辑

**接口**: `POST /activities/{id}/register`

**新增验证**:
- 活动必须处于`APPROVED`状态才能报名
- 活动必须在报名时间范围内
- 活动报名人数未达到上限

### 3.2 签到签退验证逻辑

**接口**: `POST /activities/validate-checkout-code`

**新增验证**:
- 活动必须处于`APPROVED`状态才能进行签到签退操作

### 3.3 活动列表显示逻辑

**接口**: `GET /activities` 和 `GET /activities/my-activities`

**响应中所有活动对象都包含审核状态字段**，客户端根据审核状态优先显示：

1. **PENDING** → 显示"审核中"（橙色）
2. **REJECTED** → 显示"审核被拒绝"（红色）+ 显示拒绝原因
3. **DRAFT** → 显示"草稿"（灰色）
4. **APPROVED** → 显示基于时间的状态（报名中、进行中、已结束等）

---

## 4. 状态转换流程

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

---

## 5. 错误处理

### 5.1 常见错误响应

**活动状态不允许操作**:
```json
{
  "success": false,
  "message": "活动未通过审核，无法执行此操作",
  "error_code": "ACTIVITY_NOT_APPROVED"
}
```

**权限不足**:
```json
{
  "success": false,
  "message": "只有活动创建者可以修改活动",
  "error_code": "PERMISSION_DENIED"
}
```

**活动状态不允许编辑**:
```json
{
  "success": false,
  "message": "只有草稿或被拒绝的活动可以编辑",
  "error_code": "ACTIVITY_STATUS_NOT_EDITABLE"
}
```

---

## 6. 实现要点

### 6.1 后端实现重点

1. **状态管理**: 确保活动状态转换的完整性和一致性
2. **权限控制**: 只有活动创建者可以编辑自己的活动
3. **审核逻辑**: 管理员审核功能在单独的管理员端实现
4. **业务验证**: 在所有相关接口中添加审核状态验证
5. **数据完整性**: 确保所有返回的活动对象都包含审核状态字段

### 6.2 客户端行为

1. **状态显示**: 优先显示审核状态，审核通过后显示时间状态
2. **功能控制**: 只有审核通过的活动可以使用签到、报名等功能
3. **草稿管理**: 支持保存草稿和提交审核的切换
4. **错误处理**: 根据审核状态给出相应的用户提示

---

## 7. 数据库设计建议

### 7.1 activities表新增字段

```sql
ALTER TABLE activities ADD COLUMN approval_status VARCHAR(20) DEFAULT 'PENDING';
ALTER TABLE activities ADD COLUMN reviewed_by VARCHAR(255);
ALTER TABLE activities ADD COLUMN reviewed_at TIMESTAMP;
ALTER TABLE activities ADD COLUMN review_comment TEXT;
```

### 7.2 索引建议

```sql
CREATE INDEX idx_activities_approval_status ON activities(approval_status);
CREATE INDEX idx_activities_organizer_approval ON activities(organizer_id, approval_status);
```

---

## 8. 测试场景

### 8.1 创建活动测试

- 测试创建草稿活动
- 测试直接提交审核
- 测试字段验证

### 8.2 状态转换测试

- 测试草稿提交审核
- 测试被拒绝活动的修改
- 测试已通过活动的不可编辑性

### 8.3 业务逻辑测试

- 测试未审核活动的报名限制
- 测试未审核活动的签到限制
- 测试权限控制

---

*此文档版本: v1.0*  
*更新时间: 2025-01-09* 