# PE校园器材借阅API文档

## 概述

PE校园器材借阅模块为学生端提供体育器材查看和借阅功能。学生可以浏览器材分类、搜索器材、查看库存、提交借阅申请并管理自己的借阅记录。

## 基础信息

- **服务地址**: `http://localhost:9999`
- **API版本**: v1
- **认证方式**: JWT Bearer Token
- **响应格式**: JSON

## 统一响应格式

所有API接口都返回统一的响应格式：

```json
{
  "success": true,
  "message": "操作成功",
  "data": {
    // 具体业务数据
  }
}
```

### 响应字段说明

| 字段 | 类型 | 说明 |
|------|------|------|
| success | boolean | 操作是否成功 |
| message | string | 响应消息 |
| data | object/array | 业务数据，失败时为null |

---

## API接口列表

### 1. 获取器材分类列表

获取所有器材分类信息，包含每个分类的统计数据。

**请求信息**
- **URL**: `/v1/equipment/categories`
- **方法**: GET
- **认证**: 无需认证

**响应示例**
```json
{
  "success": true,
  "message": "获取分类列表成功",
  "data": [
    {
      "id": "cat001",
      "name": "球类器材",
      "description": "各种体育球类器材",
      "icon": "⚽",
      "sort": 1,
      "createdAt": "2024-01-15 10:00:00",
      "updatedAt": "2024-01-15 10:00:00",
      "equipmentCount": 5,
      "totalQuantity": 100,
      "availableQuantity": 80,
      "borrowedQuantity": 20
    }
  ]
}
```

**响应字段说明**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 分类唯一标识 |
| name | string | 分类名称 |
| description | string | 分类描述 |
| icon | string | 分类图标 |
| sort | integer | 排序字段 |
| equipmentCount | integer | 该分类下器材种类数量 |
| totalQuantity | integer | 该分类下器材总数量 |
| availableQuantity | integer | 该分类下可用数量 |
| borrowedQuantity | integer | 该分类下借出数量 |

---

### 2. 获取器材列表

根据分类获取器材列表，支持分页。

**请求信息**
- **URL**: `/v1/equipment/items`
- **方法**: GET
- **认证**: 无需认证

**请求参数**

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| categoryId | string | 否 | - | 分类ID，不传则获取所有器材 |
| page | integer | 否 | 0 | 页码（从0开始） |
| size | integer | 否 | 10 | 每页数量 |
| sortBy | string | 否 | createdAt | 排序字段 |
| sortDir | string | 否 | desc | 排序方向（asc/desc） |

**请求示例**
```
GET /v1/equipment/items?categoryId=cat001&page=0&size=10&sortBy=availableQuantity&sortDir=desc
```

**响应示例**
```json
{
  "success": true,
  "message": "获取器材列表成功",
  "data": {
    "content": [
      {
        "id": "eq001",
        "categoryId": "cat001",
        "categoryName": "球类器材",
        "name": "标准足球",
        "model": "Nike Premier League",
        "specification": "5号球，FIFA认证标准",
        "totalQuantity": 50,
        "availableQuantity": 45,
        "borrowedQuantity": 5,
        "damagedQuantity": 0,
        "unitPrice": 89.99,
        "purchaseDate": "2024-01-15",
        "warrantyPeriod": 12,
        "storageLocation": "器材室A区1号柜",
        "createdAt": "2024-01-15 10:00:00",
        "updatedAt": "2024-01-15 10:00:00"
      }
    ],
    "pageable": {
      "sort": {
        "sorted": true,
        "unsorted": false
      },
      "pageNumber": 0,
      "pageSize": 10
    },
    "totalElements": 25,
    "totalPages": 3,
    "size": 10,
    "numberOfElements": 10
  }
}
```

---

### 3. 搜索器材

根据关键词搜索器材，支持按分类过滤。

**请求信息**
- **URL**: `/v1/equipment/search`
- **方法**: GET
- **认证**: 无需认证

**请求参数**

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| keyword | string | 否 | - | 搜索关键词（器材名称） |
| categoryId | string | 否 | - | 分类ID |
| page | integer | 否 | 0 | 页码 |
| size | integer | 否 | 10 | 每页数量 |
| sortBy | string | 否 | createdAt | 排序字段 |
| sortDir | string | 否 | desc | 排序方向 |

**请求示例**
```
GET /v1/equipment/search?keyword=足球&categoryId=cat001&page=0&size=10
```

**响应格式**: 与获取器材列表相同

---

### 4. 获取器材详情

获取指定器材的详细信息。

**请求信息**
- **URL**: `/v1/equipment/items/{equipmentId}`
- **方法**: GET
- **认证**: 无需认证

**路径参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| equipmentId | string | 是 | 器材ID |

**请求示例**
```
GET /v1/equipment/items/eq001
```

**响应示例**
```json
{
  "success": true,
  "message": "获取器材详情成功",
  "data": {
    "id": "eq001",
    "categoryId": "cat001",
    "categoryName": "球类器材",
    "name": "标准足球",
    "model": "Nike Premier League",
    "specification": "5号球，FIFA认证标准",
    "totalQuantity": 50,
    "availableQuantity": 45,
    "borrowedQuantity": 5,
    "damagedQuantity": 0,
    "unitPrice": 89.99,
    "purchaseDate": "2024-01-15",
    "warrantyPeriod": 12,
    "storageLocation": "器材室A区1号柜",
    "createdBy": "admin",
    "createdAt": "2024-01-15 10:00:00",
    "updatedAt": "2024-01-15 10:00:00",
    "borrowCount": 15,
    "totalBorrowedQty": 120
  }
}
```

---

### 5. 获取热门器材排行

获取借用次数最多的器材排行榜。

**请求信息**
- **URL**: `/v1/equipment/popular`
- **方法**: GET
- **认证**: 无需认证

**请求参数**

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | integer | 否 | 0 | 页码 |
| size | integer | 否 | 20 | 每页数量 |

**请求示例**
```
GET /v1/equipment/popular?page=0&size=10
```

**响应格式**: 与获取器材列表相同，数据按借用次数排序

---

### 6. 检查器材可用性

检查指定器材是否有足够的库存供借用。

**请求信息**
- **URL**: `/v1/equipment/items/{equipmentId}/availability`
- **方法**: GET
- **认证**: 无需认证

**路径参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| equipmentId | string | 是 | 器材ID |

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| quantity | integer | 是 | 需要借用的数量 |

**请求示例**
```
GET /v1/equipment/items/eq001/availability?quantity=3
```

**响应示例**
```json
{
  "success": true,
  "message": "器材可借用",
  "data": true
}
```

---

### 7. 提交借用申请

学生提交器材借用申请。

**请求信息**
- **URL**: `/v1/equipment/applications`
- **方法**: POST
- **认证**: 需要JWT Token

**请求头**
```
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

**请求体**
```json
{
  "equipmentId": "eq001",
  "quantity": 2,
  "purpose": "体育课使用",
  "borrowDate": "2024-01-20 09:00:00",
  "expectedReturnDate": "2024-01-20 17:00:00",
  "remark": "下午体育课需要使用"
}
```

**请求字段说明**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| equipmentId | string | 是 | 器材ID |
| quantity | integer | 是 | 借用数量（1-100） |
| purpose | string | 是 | 借用目的（最多200字符） |
| borrowDate | string | 是 | 借用开始时间（yyyy-MM-dd HH:mm:ss） |
| expectedReturnDate | string | 是 | 预期归还时间（yyyy-MM-dd HH:mm:ss） |
| remark | string | 否 | 备注（最多200字符） |

**响应示例**
```json
{
  "success": true,
  "message": "借用申请提交成功",
  "data": "app123456"
}
```

**错误示例**
```json
{
  "success": false,
  "message": "库存不足，当前可用数量：3",
  "data": null
}
```

---

### 8. 获取我的借用申请列表

获取当前用户的所有借用申请记录。

**请求信息**
- **URL**: `/v1/equipment/my-applications`
- **方法**: GET
- **认证**: 需要JWT Token

**请求头**
```
Authorization: Bearer <JWT_TOKEN>
```

**请求参数**

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | integer | 否 | 0 | 页码 |
| size | integer | 否 | 10 | 每页数量 |

**请求示例**
```
GET /v1/equipment/my-applications?page=0&size=10
```

**响应示例**
```json
{
  "success": true,
  "message": "获取申请列表成功",
  "data": {
    "content": [
      {
        "id": "app123456",
        "equipmentId": "eq001",
        "equipmentName": "标准足球",
        "equipmentModel": "Nike Premier League",
        "categoryName": "球类器材",
        "quantity": 2,
        "purpose": "体育课使用",
        "borrowDate": "2024-01-20 09:00:00",
        "expectedReturnDate": "2024-01-20 17:00:00",
        "actualReturnDate": null,
        "status": "PENDING",
        "remark": "下午体育课需要使用",
        "approvedAt": null,
        "actualQuantity": null,
        "returnCondition": null,
        "createdAt": "2024-01-19 15:30:00",
        "updatedAt": "2024-01-19 15:30:00"
      }
    ],
    "totalElements": 5,
    "totalPages": 1,
    "size": 10,
    "numberOfElements": 5
  }
}
```

**申请状态说明**

| 状态 | 中文名称 | 说明 |
|------|----------|------|
| pending | 待审批 | 申请已提交，等待管理员审批 |
| approved | 已批准 | 申请已批准，可以借用器材 |
| rejected | 已拒绝 | 申请被拒绝 |
| returned | 已归还 | 器材已归还 |

---

### 9. 获取申请详情

获取指定借用申请的详细信息。

**请求信息**
- **URL**: `/v1/equipment/applications/{applicationId}`
- **方法**: GET
- **认证**: 需要JWT Token

**请求头**
```
Authorization: Bearer <JWT_TOKEN>
```

**路径参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| applicationId | string | 是 | 申请ID |

**请求示例**
```
GET /v1/equipment/applications/app123456
```

**响应示例**
```json
{
  "success": true,
  "message": "获取申请详情成功",
  "data": {
    "id": "app123456",
    "equipmentId": "eq001",
    "equipmentName": "标准足球",
    "equipmentModel": "Nike Premier League",
    "categoryName": "球类器材",
    "borrowerId": "user123",
    "borrowerName": "张三",
    "borrowerStudentId": "2024001",
    "borrowerPhone": "13812345678",
    "quantity": 2,
    "purpose": "体育课使用",
    "borrowDate": "2024-01-20 09:00:00",
    "expectedReturnDate": "2024-01-20 17:00:00",
    "actualReturnDate": "2024-01-20 16:30:00",
    "status": "RETURNED",
    "remark": "下午体育课需要使用",
    "approvedBy": "admin",
    "approverName": "管理员",
    "approvedAt": "2024-01-19 16:00:00",
    "actualQuantity": 2,
    "returnCondition": "GOOD",
    "returnedBy": "admin",
    "returnerName": "管理员",
    "createdAt": "2024-01-19 15:30:00",
    "updatedAt": "2024-01-20 16:30:00"
  }
}
```

**归还状态说明**

| 状态 | 中文名称 | 说明 |
|------|----------|------|
| good | 完好 | 器材完好无损 |
| damaged | 损坏 | 器材有损坏 |
| lost | 丢失 | 器材丢失 |

---

### 10. 获取超期提醒

获取当前用户的超期未归还申请。

**请求信息**
- **URL**: `/v1/equipment/my-overdue`
- **方法**: GET
- **认证**: 需要JWT Token

**请求头**
```
Authorization: Bearer <JWT_TOKEN>
```

**请求示例**
```
GET /v1/equipment/my-overdue
```

**响应示例**
```json
{
  "success": true,
  "message": "获取超期提醒成功",
  "data": [
    {
      "id": "app123457",
      "equipmentName": "羽毛球拍",
      "quantity": 1,
      "expectedReturnDate": "2024-01-18 17:00:00",
      "status": "APPROVED",
      "purpose": "羽毛球比赛"
    }
  ]
}
```

---

### 11. 健康检查

检查器材借阅服务运行状态。

**请求信息**
- **URL**: `/v1/equipment/health`
- **方法**: GET
- **认证**: 无需认证

**请求示例**
```
GET /v1/equipment/health
```

**响应示例**
```json
{
  "success": true,
  "message": "器材借阅服务运行正常",
  "data": "OK"
}
```

---

## 错误码说明

### HTTP状态码

| 状态码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未授权（需要登录） |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

### 业务错误信息

| 错误信息 | 说明 | 解决方案 |
|----------|------|----------|
| 器材不存在或已删除 | 请求的器材ID无效 | 检查器材ID是否正确 |
| 库存不足，当前可用数量：X | 器材库存不足 | 减少借用数量或选择其他器材 |
| 您还有该器材的未归还借用，请先归还后再申请 | 同一器材重复借用 | 先归还现有借用再申请 |
| 用户不存在 | JWT Token中的用户ID无效 | 重新登录获取有效Token |
| 预期归还时间必须在借用开始时间之后 | 时间参数错误 | 检查时间参数设置 |
| 借用开始时间不能早于当前时间 | 时间参数错误 | 设置合理的借用时间 |
| 无权查看此申请 | 尝试查看他人的申请 | 只能查看自己的申请 |

---

## 使用示例

### 前端集成示例

```javascript
// 1. 获取器材分类
async function getCategories() {
  const response = await fetch('/v1/equipment/categories');
  const result = await response.json();
  
  if (result.success) {
    console.log('分类列表:', result.data);
  } else {
    console.error('获取失败:', result.message);
  }
}

// 2. 搜索器材
async function searchEquipments(keyword, categoryId) {
  const params = new URLSearchParams({
    keyword: keyword || '',
    categoryId: categoryId || '',
    page: 0,
    size: 10
  });
  
  const response = await fetch(`/v1/equipment/search?${params}`);
  const result = await response.json();
  
  return result;
}

// 3. 提交借用申请
async function createApplication(applicationData, token) {
  const response = await fetch('/v1/equipment/applications', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(applicationData)
  });
  
  const result = await response.json();
  
  if (result.success) {
    console.log('申请成功，ID:', result.data);
  } else {
    console.error('申请失败:', result.message);
  }
  
  return result;
}

// 4. 获取我的申请列表
async function getMyApplications(token, page = 0) {
  const response = await fetch(`/v1/equipment/my-applications?page=${page}&size=10`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  
  return await response.json();
}
```

---

## 数据库表结构

### 器材分类表 (equipment_categories)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | VARCHAR(36) | 主键 |
| name | VARCHAR(50) | 分类名称 |
| description | VARCHAR(200) | 分类描述 |
| icon | VARCHAR(10) | 分类图标 |
| sort | INT | 排序字段 |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |

### 器材库存表 (equipment_items)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | VARCHAR(36) | 主键 |
| category_id | VARCHAR(36) | 分类ID |
| name | VARCHAR(50) | 器材名称 |
| model | VARCHAR(30) | 型号 |
| specification | VARCHAR(200) | 规格描述 |
| total_quantity | INT | 总数量 |
| available_quantity | INT | 可用数量 |
| borrowed_quantity | INT | 借出数量 |
| damaged_quantity | INT | 损坏数量 |
| unit_price | DECIMAL(10,2) | 单价 |
| purchase_date | DATE | 购买日期 |
| warranty_period | INT | 保修期（月） |
| storage_location | VARCHAR(50) | 存放位置 |
| created_by | VARCHAR(36) | 创建人ID |
| is_deleted | BOOLEAN | 是否删除 |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |

### 器材借用申请表 (equipment_applications)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | VARCHAR(36) | 主键 |
| equipment_id | VARCHAR(36) | 器材ID |
| borrower_id | VARCHAR(36) | 借用人ID |
| quantity | INT | 借用数量 |
| purpose | VARCHAR(200) | 借用目的 |
| borrow_date | TIMESTAMP | 借用开始时间 |
| expected_return_date | TIMESTAMP | 预期归还时间 |
| actual_return_date | TIMESTAMP | 实际归还时间 |
| status | ENUM | 申请状态 |
| remark | VARCHAR(200) | 备注 |
| approved_by | VARCHAR(36) | 审批人ID |
| approved_at | TIMESTAMP | 审批时间 |
| actual_quantity | INT | 实际归还数量 |
| return_condition | ENUM | 归还状态 |
| returned_by | VARCHAR(36) | 归还处理人ID |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |

---

## 部署说明

### 环境要求

- JDK 22+
- MySQL 8.0+
- Redis 6.0+
- Spring Boot 3.5.3

### 配置步骤

1. **数据库初始化**
   ```sql
   -- 在现有的pexitong3数据库中执行器材管理表创建脚本
   mysql -u root -p pexitong3 < equipment_management_basic.sql
   ```

2. **配置文件**
   ```properties
   # 器材管理配置
   equipment.low-stock-threshold=5
   equipment.max-borrow-quantity=10
   equipment.max-borrow-days=30
   ```

3. **启动服务**
   ```bash
   ./gradlew bootRun
   ```

### 测试验证

访问健康检查接口验证服务状态：
```bash
curl http://localhost:9999/v1/equipment/health
```

---

## 版本历史

- **v1.0** (2025-01-XX)
  - 初始版本
  - 支持器材分类浏览
  - 支持器材搜索和查看
  - 支持借用申请提交和管理
  - 支持热门器材排行
  - 支持超期提醒功能

---

## 联系方式

如有问题或建议，请联系开发团队。

**文档更新时间**: 2025年1月
