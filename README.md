# PE校园 Spring Boot 后端

这是一个基于Spring Boot框架开发的PE校园体育活动管理系统后端API服务。

## 功能特性

- 用户认证（JWT Token）
- 短信验证码登录/注册
- 体育活动管理
- 活动报名与签到
- 二维码签到系统
- 积分与学时管理
- 早操考勤系统
- 实时WebSocket通信
- Redis缓存支持

## 技术栈

- **框架**: Spring Boot 3.5.3
- **数据库**: MySQL 8.0
- **缓存**: Redis
- **认证**: JWT (JSON Web Token)
- **ORM**: Spring Data JPA + Hibernate
- **安全**: Spring Security
- **构建工具**: Gradle
- **Java版本**: JDK 22

## 项目结构

```
src/main/java/com/example/pehd/
├── config/          # 配置类
├── controller/      # 控制器层
├── dto/            # 数据传输对象
├── entity/         # 实体类
├── repository/     # 数据访问层
├── service/        # 业务逻辑层
└── PehdApplication.java  # 启动类
```

## 环境要求

- JDK 22+
- MySQL 8.0+
- Redis 6.0+
- Gradle 8.0+

## 安装步骤

### 1. 克隆项目

```bash
git clone <repository-url>
cd PEHD
```

### 2. 配置数据库

1. 创建MySQL数据库：
```sql
CREATE DATABASE pe CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 导入数据库结构：
```bash
mysql -u root -p pe < pe_database.sql
```

3. 修改 `application.properties` 中的数据库配置：
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pe?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. 配置Redis

确保Redis服务已启动，并修改Redis配置（如果需要）：
```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

### 4. 构建和运行

```bash
# 使用Gradle构建
./gradlew build

# 运行应用
./gradlew bootRun
```

或者直接运行JAR文件：
```bash
java -jar build/libs/PEHD-0.0.1-SNAPSHOT.jar
```

## API文档

应用启动后，访问以下地址查看API文档：
- Swagger UI: `http://localhost:9999/swagger-ui.html`
- API文档: 参考 `API_DOCUMENTATION.md`

## 主要API端点

### 认证相关
- `POST /auth/login` - 用户登录
- `POST /auth/register` - 用户注册
- `POST /auth/send-code` - 发送验证码
- `POST /auth/refresh` - 刷新Token
- `POST /auth/logout` - 用户登出

### 用户管理
- `GET /user/profile` - 获取用户资料
- `PUT /user/profile` - 更新用户资料

### 活动管理
- `GET /activities` - 获取活动列表
- `POST /activities` - 创建活动
- `POST /activities/{id}/register` - 报名活动
- `DELETE /activities/{id}/register` - 取消报名

### 签到管理
- `POST /activities/request-checkin-code` - 获取签到码
- `POST /activities/validate-checkin-code` - 验证签到码

## 配置说明

### JWT配置
```properties
jwt.secret=your-secret-key
jwt.expiration=3600                    # 访问Token有效期（秒）
jwt.refresh-token.expiration=604800    # 刷新Token有效期（秒）
```

### 数据库配置
```properties
spring.jpa.hibernate.ddl-auto=update   # 开发环境可用update，生产环境建议使用validate
spring.jpa.show-sql=true              # 显示SQL语句
```

## 开发指南

### 添加新的API端点

1. 在相应的Controller中添加新方法
2. 在Service层实现业务逻辑
3. 如需要，在Repository层添加数据访问方法
4. 更新API文档

### 数据库迁移

1. 修改实体类
2. 运行应用，Hibernate会自动更新表结构（仅限开发环境）
3. 生产环境建议手动编写SQL脚本

### 添加新的验证码类型

在`VerificationService`中的`simulateSendSMS`方法中添加新的类型处理。

## 测试

运行测试：
```bash
./gradlew test
```

## 部署

### 生产环境配置

1. 修改`application.properties`：
```properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
logging.level.com.example.pehd=INFO
```

2. 使用环境变量配置敏感信息：
```bash
export SPRING_DATASOURCE_PASSWORD=your_password
export JWT_SECRET=your_production_secret
```

### Docker部署

创建`Dockerfile`：
```dockerfile
FROM openjdk:22-jdk-slim
COPY build/libs/PEHD-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9999
ENTRYPOINT ["java","-jar","/app.jar"]
```

构建和运行：
```bash
docker build -t pe-campus-backend .
docker run -p 9999:9999 pe-campus-backend
```

## 故障排除

### 常见问题

1. **数据库连接失败**
   - 检查MySQL服务是否启动
   - 验证数据库连接信息
   - 确认防火墙设置

2. **Redis连接失败**
   - 检查Redis服务是否启动
   - 验证Redis连接配置

3. **JWT Token无效**
   - 检查Token是否过期
   - 验证JWT密钥配置

## 贡献

1. Fork 项目
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建Pull Request

## 许可证

[MIT License](LICENSE)

## 联系方式

如有问题或建议，请联系开发团队。 