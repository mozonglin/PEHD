# PE校园后端系统技术文档

## 项目概述

PE校园是一个体育活动管理应用后端系统，基于Spring Boot 3.5.3 + JDK 22构建，使用MySQL数据库和Redis缓存，实现了用户认证、活动管理、签到系统等核心功能。

---

## 项目结构

```
src/main/java/com/example/pehd/
├── controller/          # 控制器层 - API端点
├── service/            # 服务层 - 业务逻辑
├── repository/         # 数据访问层 - 数据库操作
├── entity/            # 实体层 - 数据模型
├── dto/               # 数据传输对象 - API交互
├── config/            # 配置层 - 系统配置
└── PehdApplication.java # 应用启动类
```

---

## 一、控制器层 (Controller Layer)

### 1. AuthController.java
**功能**: 用户认证相关API控制器
- **职责**: 处理用户登录、注册、Token管理等认证相关请求
- **主要端点**:
  - `POST /v1/auth/login` - 用户登录
  - `POST /v1/auth/register` - 用户注册
  - `POST /v1/auth/send-code` - 发送验证码
  - `POST /v1/auth/refresh` - 刷新Token
  - `POST /v1/auth/logout` - 用户登出
  - `POST /v1/auth/verify-token` - 验证Token
- **特点**: 无需JWT认证的公开接口，处理用户身份验证

### 2. ActivityController.java
**功能**: 活动管理相关API控制器
- **职责**: 处理活动的CRUD操作、报名管理、签到功能
- **主要端点**:
  - `GET /v1/activities` - 获取活动列表（分页、筛选）
  - `POST /v1/activities` - 创建新活动
  - `GET /v1/activities/{id}` - 获取活动详情
  - `POST /v1/activities/{id}/register` - 报名活动
  - `DELETE /v1/activities/{id}/register` - 取消报名
  - `GET /v1/activities/my-activities` - 获取我的活动
  - `GET /v1/activities/{id}/participants` - 获取参与者列表
  - `POST /v1/activities/request-checkin-code` - 请求签到码
  - `POST /v1/activities/validate-checkin-code` - 验证签到码
- **特点**: 需要JWT认证，集成了新版40秒签到系统

### 3. UserController.java
**功能**: 用户管理相关API控制器
- **职责**: 处理用户资料管理、积分系统、权限设置
- **主要端点**:
  - `GET /v1/user/profile` - 获取用户资料
  - `PUT /v1/user/profile` - 更新用户资料
  - `PUT /v1/user/phone` - 修改手机号
  - `GET /v1/user/points/exchanges` - 获取积分兑换记录
  - `POST /v1/user/points/exchange` - 积分兑换
  - `GET /v1/user/integrity/score` - 获取诚信度
  - `GET/PUT /v1/user/settings/permissions` - 权限设置管理
- **特点**: 需要JWT认证，包含积分兑换业务逻辑

---

## 二、服务层 (Service Layer)

### 1. AuthService.java
**功能**: 认证服务核心业务逻辑
- **职责**: 
  - 用户登录验证（姓名+学号+手机号+验证码）
  - 用户注册逻辑
  - JWT Token生成和管理
  - Token黑名单机制
- **核心方法**:
  - `login()` - 用户登录
  - `register()` - 用户注册
  - `refreshToken()` - Token刷新
  - `logout()` - 登出处理
- **特点**: 集成验证码验证，支持Token黑名单

### 2. ActivityService.java
**功能**: 活动管理服务业务逻辑
- **职责**:
  - 活动CRUD操作
  - 报名状态管理
  - 参与者权限控制
  - 活动时间验证
- **核心方法**:
  - `getActivities()` - 分页获取活动列表
  - `createActivity()` - 创建活动（时间验证）
  - `registerActivity()` - 报名逻辑（时间、人数验证）
  - `cancelRegistration()` - 取消报名
  - `getMyActivities()` - 获取用户相关活动
- **特点**: 完整的时间逻辑验证，权限控制

### 3. CheckinService.java
**功能**: 新版签到服务核心逻辑
- **职责**:
  - 40秒有效期签到码生成
  - 签到码验证和使用
  - 签到记录管理
  - 自动积分奖励
- **核心方法**:
  - `requestCheckinCode()` - 生成40秒签到码
  - `validateCheckinCode()` - 验证码并完成签到
  - `cleanupExpiredCodes()` - 清理过期码
- **特点**: 40秒超时机制，防重复签到，自动积分计算

### 4. UserService.java
**功能**: 用户管理服务业务逻辑
- **职责**:
  - 用户资料管理
  - 手机号修改（含验证码）
  - 积分兑换系统
  - 权限设置管理
- **核心方法**:
  - `updateProfile()` - 更新用户资料
  - `updatePhone()` - 修改手机号
  - `exchangePoints()` - 积分兑换学时
- **特点**: 集成验证码验证，积分兑换比例计算

### 5. JwtService.java
**功能**: JWT Token管理服务
- **职责**:
  - Token生成、验证、解析
  - Token黑名单管理
  - 过期处理
- **特点**: 支持Access Token和Refresh Token

### 6. VerificationService.java
**功能**: 验证码服务
- **职责**:
  - 短信验证码生成和发送
  - 验证码验证
  - Redis缓存管理
- **特点**: 5分钟有效期，防刷机制

---

## 三、数据传输对象层 (DTO Layer)

### 请求类 (Request DTOs)

#### 1. LoginRequest.java
- **功能**: 用户登录请求数据
- **字段**: 姓名、学号、手机号、验证码
- **验证**: 必填字段验证、手机号格式验证

#### 2. RegisterRequest.java
- **功能**: 用户注册请求数据
- **字段**: 姓名、学号、手机号、验证码、学校、学院
- **验证**: 完整的字段验证和格式检查

#### 3. CreateActivityRequest.java
- **功能**: 创建活动请求数据
- **字段**: 标题、描述、地点、人数限制、时间安排、类别、积分
- **验证**: 时间逻辑验证、人数限制验证

#### 4. CheckinCodeRequest.java
- **功能**: 请求签到码数据
- **字段**: 活动ID、活动名称、学生姓名、时间戳
- **特点**: 用于40秒签到系统

#### 5. ValidateCodeRequest.java
- **功能**: 验证签到码请求
- **字段**: 唯一签到码
- **特点**: 签到验证专用

#### 6. UpdateProfileRequest.java
- **功能**: 更新用户资料请求
- **字段**: 姓名、学校、学院、头像
- **验证**: 必填字段验证

#### 7. UpdatePhoneRequest.java
- **功能**: 修改手机号请求
- **字段**: 新手机号、验证码
- **验证**: 手机号格式验证

#### 8. PointExchangeRequest.java
- **功能**: 积分兑换请求
- **字段**: 兑换类型、积分数量
- **验证**: 积分数量验证

### 响应类 (Response DTOs)

#### 1. LoginResponse.java
- **功能**: 登录响应数据
- **字段**: 用户信息、AccessToken、RefreshToken、过期时间
- **特点**: 完整的认证响应

#### 2. CheckinCodeResponse.java
- **功能**: 签到码响应数据
- **字段**: 唯一码、活动信息、学生信息、时间戳、过期时间
- **特点**: 40秒有效期信息

#### 3. ActivityDto.java
- **功能**: 活动信息传输
- **字段**: 完整的活动信息、报名状态、参与人数
- **特点**: 包含用户相关状态

#### 4. UserDto.java
- **功能**: 用户信息传输
- **字段**: 完整用户信息（不含密码）
- **特点**: 安全的用户数据传输

#### 5. AttendanceRecordDto.java
- **功能**: 签到记录传输
- **字段**: 签到ID、用户信息、时间、地点
- **特点**: 签到历史查询专用

### 其他DTOs

#### 1. ApiResponse.java
- **功能**: 统一API响应格式
- **字段**: 成功标志、消息、数据
- **特点**: 全局统一响应格式

#### 2. UserPermissionsDto.java
- **功能**: 用户权限设置
- **字段**: 各种权限开关（摄像头、位置、存储等）
- **特点**: 权限管理专用

#### 3. WebSocketMessage.java
- **功能**: WebSocket消息格式
- **字段**: 消息类型、数据内容
- **特点**: 实时推送消息格式

---

## 四、数据访问层 (Repository Layer)

### 1. UserRepository.java
**功能**: 用户数据访问接口
- **继承**: `JpaRepository<User, String>`
- **核心方法**:
  - `findByStudentId()` - 按学号查找
  - `findByPhoneNumber()` - 按手机号查找
  - `findByNameAndStudentIdAndPhoneNumber()` - 登录查询
  - `existsByStudentId()` - 学号存在性检查
- **特点**: 支持多条件查询，ID列表查询

### 2. ActivityRepository.java
**功能**: 活动数据访问接口
- **继承**: `JpaRepository<Activity, String>`
- **核心方法**:
  - `findByCategory()` - 按类别分页查询
  - `findByOrganizerId()` - 按组织者查询
  - `findAvailableForRegistration()` - 可报名活动
  - `findOngoingActivities()` - 进行中活动
- **特点**: 丰富的查询方法，支持分页和条件筛选

### 3. ActivityRegistrationRepository.java
**功能**: 活动报名数据访问接口
- **继承**: `JpaRepository<ActivityRegistration, String>`
- **核心方法**:
  - `findByUserIdAndActivityId()` - 查找报名记录
  - `existsByUserIdAndActivityId()` - 报名状态检查
  - `findByUserIdAndStatus()` - 按状态查询
  - `countRegisteredByActivityId()` - 统计报名人数
- **特点**: 支持状态查询，统计功能

### 4. AttendanceRecordRepository.java
**功能**: 签到记录数据访问接口
- **继承**: `JpaRepository<AttendanceRecord, String>`
- **核心方法**:
  - `findByActivityId()` - 按活动查找签到记录
  - `findByUserId()` - 按用户查找签到记录
  - `existsByActivityIdAndUserId()` - 签到状态检查
- **特点**: 防重复签到检查

### 5. ActivityCheckinCodeRepository.java
**功能**: 签到码数据访问接口
- **继承**: `JpaRepository<ActivityCheckinCode, String>`
- **核心方法**:
  - `findByUniqueCode()` - 按唯一码查找
  - `findValidByUniqueCode()` - 查找有效签到码
  - `invalidateByActivityIdAndStudentId()` - 使签到码失效
  - `deleteByExpiresAtBefore()` - 删除过期码
- **特点**: 支持40秒有效期管理，自动清理

---

## 五、实体层 (Entity Layer)

### 1. User.java
**功能**: 用户实体模型
- **表名**: `users`
- **核心字段**: ID、姓名、学号、手机号、积分、学时、诚信度
- **特点**: 实现UserDetails接口，支持Spring Security

### 2. Activity.java
**功能**: 活动实体模型
- **表名**: `activities`
- **核心字段**: 标题、描述、地点、时间安排、参与人数、组织者
- **特点**: 完整的活动信息模型

### 3. ActivityRegistration.java
**功能**: 活动报名实体模型
- **表名**: `activity_registrations`
- **核心字段**: 用户ID、活动ID、状态、报名时间
- **特点**: 关联用户和活动的中间表

### 4. AttendanceRecord.java
**功能**: 签到记录实体模型
- **表名**: `attendance_records`
- **核心字段**: 活动ID、用户ID、签到时间、地点、二维码数据
- **特点**: 记录完整的签到信息

### 5. ActivityCheckinCode.java
**功能**: 签到码实体模型
- **表名**: `activity_checkin_codes`
- **核心字段**: 唯一码、活动信息、学生信息、时间戳、过期时间
- **特点**: 支持40秒有效期机制

---

## 六、配置层 (Config Layer)

### 1. SecurityConfig.java
**功能**: Spring Security安全配置
- **职责**:
  - JWT认证过滤器配置
  - 公开接口白名单
  - CORS跨域配置
  - 密码编码器配置
- **特点**: 基于JWT的无状态认证

### 2. RedisConfig.java
**功能**: Redis缓存配置
- **职责**:
  - Redis连接配置
  - 序列化配置
  - 缓存模板配置
- **特点**: 支持验证码缓存

---

## 七、应用配置文件

### 1. application.properties
**功能**: 应用主配置文件
- **包含配置**:
  - 数据库连接（MySQL）
  - Redis连接配置
  - JWT密钥和过期时间
  - JPA配置
  - 日志配置
- **特点**: 开发环境配置

### 2. build.gradle
**功能**: Gradle构建配置
- **包含依赖**:
  - Spring Boot Starter包
  - Spring Security
  - JWT库
  - MySQL驱动
  - Redis连接器
- **特点**: 完整的依赖管理

---

## 八、数据库和文档

### 1. pe_database.sql
**功能**: 数据库初始化脚本
- **包含内容**:
  - 12个数据表结构
  - 索引和约束
  - 初始化数据
  - 示例数据
- **特点**: 完整的数据库方案

### 2. README.md
**功能**: 项目说明文档
- **包含内容**:
  - 项目介绍
  - 部署指南
  - API使用说明
  - 功能特性
- **特点**: 详细的使用指南

### 3. API_DOCUMENTATION.md
**功能**: API接口文档
- **包含内容**:
  - 完整的API规范
  - 请求响应示例
  - 错误码说明
  - 业务逻辑说明
- **特点**: 1486行详细API文档

### 4. IMPLEMENTATION_SUMMARY.md
**功能**: 实现总结文档
- **包含内容**:
  - 功能实现总结
  - 技术特点分析
  - 部署说明
  - 扩展建议
- **特点**: 开发总结和指南

### 5. TECHNICAL_DOCUMENTATION.md
**功能**: 技术文档（本文档）
- **包含内容**:
  - 每个文件的详细功能说明
  - 项目架构分析
  - 技术实现细节
- **特点**: 完整的技术文档

---

## 九、启动类

### PehdApplication.java
**功能**: Spring Boot应用启动类
- **职责**: 应用程序入口点
- **注解**: `@SpringBootApplication`
- **特点**: 标准的Spring Boot启动类

---

## 技术架构总结

### 分层架构
1. **表现层**: Controller - 处理HTTP请求
2. **业务层**: Service - 核心业务逻辑
3. **数据层**: Repository - 数据访问
4. **实体层**: Entity - 数据模型
5. **传输层**: DTO - 数据传输

### 核心技术栈
- **框架**: Spring Boot 3.5.3
- **语言**: Java 22
- **数据库**: MySQL 8.0
- **缓存**: Redis
- **认证**: JWT + Spring Security
- **构建**: Gradle

### 设计特点
- **安全性**: JWT认证 + 40秒签到超时
- **可靠性**: 事务管理 + 数据验证
- **性能**: 分页查询 + Redis缓存
- **扩展性**: 模块化设计 + 统一格式

---

**文档版本**: 1.0
**创建日期**: 2025年1月
**技术栈**: Spring Boot 3.5.3 + JDK 22 + MySQL + Redis 