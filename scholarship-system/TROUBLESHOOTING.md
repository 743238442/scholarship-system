# 故障排除指南

## 数据库连接问题

### 问题描述
应用程序启动时出现数据库连接错误：
```
Caused by: org.postgresql.util.PSQLException: Connection to localhost:5432 refused. 
Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.
```

### 问题原因
1. PostgreSQL数据库服务未启动
2. 数据库连接配置错误
3. 防火墙阻止连接
4. 数据库用户权限问题

### 解决方案

#### 1. 启动PostgreSQL数据库服务

**Windows系统：**
```cmd
# 作为Windows服务启动
net start postgresql-x64-13

# 或者通过服务管理器启动
services.msc
# 找到"PostgreSQL"服务并启动
```

**Linux系统：**
```bash
# 使用systemctl启动
sudo systemctl start postgresql

# 或者使用service命令
sudo service postgresql start
```

**macOS系统：**
```bash
# 使用brew启动
brew services start postgresql
```

#### 2. 验证数据库连接

使用psql客户端测试连接：
```cmd
psql -h localhost -p 5432 -U postgres -d scholarship_db
```

#### 3. 创建数据库（如果不存在）

```sql
-- 连接到postgres数据库
psql -U postgres

-- 创建数据库
CREATE DATABASE scholarship_db;

-- 验证创建成功
\l scholarship_db
```

#### 4. 检查application.yml配置

确保配置正确：
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/scholarship_db
    username: postgres
    password: 123456  # 确保密码正确
    driver-class-name: org.postgresql.Driver
```

#### 5. 检查防火墙设置

**Windows防火墙：**
```cmd
# 允许PostgreSQL通过防火墙
netsh advfirewall firewall add rule name="PostgreSQL" dir=in action=allow protocol=TCP localport=5432
```

**Linux防火墙：**
```bash
# 允许5432端口
sudo ufw allow 5432
sudo firewall-cmd --permanent --add-port=5432/tcp
sudo firewall-cmd --reload
```

#### 6. 检查pg_hba.conf配置

PostgreSQL配置文件 `pg_hba.conf` 中确保有：
```
# 允许本地连接
host    all             all             127.0.0.1/32            md5
host    all             all             ::1/128                 md5
host    all             all             localhost               md5
```

### 快速修复脚本

创建一个批处理文件 `start-database.bat`：

```batch
@echo off
echo 启动PostgreSQL数据库服务...
net start postgresql-x64-13
echo 等待服务启动...
timeout /t 5
echo 正在启动奖学金管理系统...
mvn spring-boot:run
pause
```

### 测试数据库连接

创建一个简单的测试类来验证数据库连接：

```java
@Component
public class DatabaseTest {
    
    @Autowired
    private DataSource dataSource;
    
    @PostConstruct
    public void testConnection() {
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("数据库连接成功！");
            System.out.println("连接URL: " + conn.getMetaData().getURL());
            System.out.println("数据库用户名: " + conn.getMetaData().getUserName());
        } catch (Exception e) {
            System.err.println("数据库连接失败: " + e.getMessage());
        }
    }
}
```

### 常见问题解决

1. **端口被占用**
   ```cmd
   netstat -ano | findstr :5432
   # 找到占用端口的进程并结束
   ```

2. **密码错误**
   - 确认PostgreSQL用户密码
   - 重置密码：
     ```sql
     ALTER USER postgres PASSWORD '123456';
     ```

3. **权限不足**
   ```sql
   GRANT ALL PRIVILEGES ON DATABASE scholarship_db TO postgres;
   ```

### 应用程序启动状态

当前应用程序配置：
- ✅ Spring Boot框架正常
- ✅ Maven依赖完整
- ✅ 应用程序配置正确
- ❌ 数据库连接失败（需启动PostgreSQL服务）

修复数据库连接问题后，应用程序将可以正常启动和运行。