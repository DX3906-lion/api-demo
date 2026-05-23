# DEV_SETUP.md

## 1. 基础环境

- JDK：Java 8
- Maven：3.8.x（建议 3.8.8）
- MySQL：8.0.x

## 2. 本地数据库建议

- 数据库名：`api_demo`
- 端口：`3306`
- 用户名：`root`
- 密码：`root`（仅本地演示，实际请使用安全密码）

## 3. 本地服务端口建议

- `new-script-service`：`8080`
- `new-executor-service`：`8081`

## 4. 本地启动命令（后续工程骨架完成后使用）

```bash
# 编译
mvn clean package -DskipTests

# 启动脚本服务
mvn -pl new-script-service spring-boot:run

# 启动执行机服务
mvn -pl new-executor-service spring-boot:run
```

## 5. 测试命令（后续工程骨架完成后使用）

```bash
# 全量测试
mvn test

# 指定模块测试
mvn -pl new-script-service test
mvn -pl new-executor-service test
```

## 6. 健康检查地址

- 脚本服务：`http://localhost:8080/health`
- 执行机服务：`http://localhost:8081/health`

## 7. 常见启动问题

1. **端口冲突**
   - 现象：启动报端口占用。
   - 处理：修改 `server.port` 或关闭占用进程。

2. **JDK 版本不一致**
   - 现象：编译失败或字节码版本错误。
   - 处理：确认 `java -version` 为 Java 8。

3. **Maven 仓库依赖下载失败**
   - 现象：`Could not resolve dependencies`。
   - 处理：检查网络、私服配置与 Maven settings。

4. **MySQL 连接失败**
   - 现象：`Communications link failure`。
   - 处理：确认数据库服务、账号密码、端口与数据库名。

5. **字符集问题**
   - 现象：中文乱码或字段比较异常。
   - 处理：统一使用 `utf8mb4`，并确认连接参数。


## 8. Maven 镜像配置（公共示例）

当出现 Maven Central 403 或网络受限时，可在本机 `~/.m2/settings.xml` 配置公开镜像（不要写入私有账号、密码或公司内网仓库）：

```xml
<settings>
  <mirrors>
    <mirror>
      <id>aliyunmaven</id>
      <mirrorOf>central</mirrorOf>
      <name>Aliyun Public Maven</name>
      <url>https://maven.aliyun.com/repository/central</url>
    </mirror>
  </mirrors>
</settings>
```

如需仅临时验证，也可使用：

```bash
mvn -s ~/.m2/settings.xml clean test
```
