# 高校奖学金管理系统

基于Spring Boot 3.2开发的高校奖学金管理系统，采用现代化的技术栈和架构设计。

## 系统概述

高校奖学金管理系统是一个完整的Web应用，旨在帮助高校高效管理奖学金评定、发放和公示流程。系统支持学生、教师和管理员三种角色，提供全方位的奖学金管理功能。

## 技术架构

### 后端技术栈
- **JDK 17**: Java开发环境
- **Spring Boot 3.2**: 应用框架
- **Spring Security 6.x**: 安全认证框架
- **Spring Data JPA**: 数据访问层
- **PostgreSQL 13+**: 主要数据库
- **Thymeleaf**: 模板引擎
- **Maven**: 项目构建工具

### 前端技术栈
- **Bootstrap 5**: UI框架
- **Bootstrap Icons**: 图标库
- **Vanilla JavaScript**: 前端脚本
- **HTML5/CSS3**: 前端技术

## 项目结构

```
scholarship-system/
├── src/main/java/com/example/scholarship/
│   ├── config/           # 配置类
│   ├── controller/       # 控制器层
│   ├── entity/          # 实体类
│   ├── repository/      # 数据访问层
│   ├── service/         # 业务逻辑层
│   └── ScholarshipSystemApplication.java
├── src/main/resources/
│   ├── static/          # 静态资源
│   │   ├── css/
│   │   ├── js/
│   │   └── images/
│   ├── templates/       # 模板文件
│   └── application.properties
└── pom.xml
```

## 核心功能模块

### 1. 用户管理模块
- 用户注册与登录
- 角色权限管理
- 用户信息维护

### 2. 学生信息管理模块
- 学生基本信息管理
- 学生信息查询和统计
- 学生档案管理

### 3. 奖学金评定模块
- 奖学金类型管理
- 申请流程管理
- 评审流程管理

### 4. 公示管理模块
- 公示信息发布
- 公示内容查看
- 公示历史记录

### 5. 系统管理模块
- 系统配置管理
- 操作日志记录
- 数据统计分析

## 实体模型

### 核心实体
- **User**: 用户实体
- **Student**: 学生实体
- **Role**: 角色实体
- **Permission**: 权限实体
- **BaseEntity**: 基础实体类（包含公共字段）

## 数据库设计

### 主要数据表
- `tbl_user`: 用户表
- `tbl_student`: 学生表
- `tbl_role`: 角色表
- `tbl_permission`: 权限表
- `tbl_user_role`: 用户角色关联表
- `tbl_role_permission`: 角色权限关联表

## 快速开始

### 环境要求
- JDK 17+
- Maven 3.8+
- PostgreSQL 13+
- 现代浏览器

### 安装步骤

1. **克隆项目**
```bash
# 项目已创建在本地，无需克隆
```

2. **配置数据库**
```sql
-- 创建数据库
CREATE DATABASE scholarship_management;

-- 创建用户（可选）
CREATE USER scholarship_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE scholarship_management TO scholarship_user;
```

3. **修改配置文件**
编辑 `src/main/resources/application.properties`:
```properties
# 数据库配置
spring.datasource.url=jdbc:postgresql://localhost:5432/scholarship_management
spring.datasource.username=postgres
spring.datasource.password=your_password
```

4. **构建项目**
```bash
cd c:\Users\ASUS\Desktop\1\scholarship-system
mvn clean compile
```

5. **运行项目**
```bash
mvn spring-boot:run
```

### 访问应用

- **应用地址**: http://localhost:8080
- **系统信息**: http://localhost:8080/api/system/info
- **健康检查**: http://localhost:8080/api/system/health

## 默认用户

系统启动时会创建默认管理员用户：
- **用户名**: admin
- **密码**: admin123
- **角色**: ADMIN

## 开发说明

### 代码规范
- 遵循阿里巴巴Java开发手册
- 使用Lombok简化代码
- 采用RESTful API设计规范
- 前端采用响应式设计

### 安全特性
- Spring Security身份认证
- BCrypt密码加密
- 基于角色的访问控制(RBAC)
- SQL注入防护

### 性能优化
- 数据库索引优化
- 分页查询支持
- 缓存机制设计
- 前端资源压缩

## 测试

### 单元测试
```bash
mvn test
```

### 集成测试
```bash
mvn integration-test
```

## 部署

### 开发环境
直接使用Spring Boot Maven插件运行

### 生产环境
```bash
mvn clean package
java -jar target/scholarship-system-1.0.0.jar
```

### Docker部署
```dockerfile
# 可选：创建Dockerfile进行容器化部署
FROM openjdk:17-jdk-alpine
COPY target/scholarship-system-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 配置说明

### application.properties主要配置项
- 数据库连接配置
- JPA/Hibernate配置
- Thymeleaf模板配置
- 安全配置
- 文件上传配置
- 日志配置

## API文档

### 系统管理API
- `GET /api/system/info` - 系统信息
- `GET /api/system/health` - 健康检查
- `GET /api/system/version` - 版本信息

## 故障排除

### 常见问题

1. **数据库连接失败**
   - 检查PostgreSQL服务是否启动
   - 验证数据库连接信息
   - 确认防火墙设置

2. **端口被占用**
   - 修改application.properties中的server.port
   - 或停止占用8080端口的其他应用

3. **权限问题**
   - 确保数据库用户有足够权限
   - 检查Spring Security配置

## 版本历史

### v1.0.0 (2024-01-01)
- 初始版本发布
- 基础框架搭建
- 核心功能模块实现
- 用户界面设计

## 贡献指南

1. Fork项目
2. 创建功能分支
3. 提交代码更改
4. 推送到分支
5. 创建Pull Request

## 许可证

本项目采用MIT许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 联系方式

- 作者: System
- 邮箱: [联系邮箱]
- 项目地址: [项目地址]

## 致谢

感谢所有为项目做出贡献的开发者和用户。