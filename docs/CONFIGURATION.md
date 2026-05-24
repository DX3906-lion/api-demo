# CONFIGURATION.md

## 1. application.yml 配置键规范

- 采用小写短横线风格：`new-script-service.xxx`。
- 分层建议：`server`、`spring`、`management`、`logging`、`app`。
- 禁止硬编码密钥；敏感配置必须通过环境变量注入。

## 2. 数据库配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/api_demo?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC
    username: root
    password: ${DB_PASSWORD:root}
    driver-class-name: com.mysql.cj.jdbc.Driver
```

## 3. 日志配置

- 日志级别默认 `INFO`。
- SQL 日志默认关闭，仅开发环境按需开启。
- 执行链路日志必须可关联 `traceId` / `executionId` / `taskId`。

## 4. HTTP 客户端超时配置

建议配置键：

```yaml
app:
  http-client:
    connect-timeout-ms: 3000
    read-timeout-ms: 10000
    write-timeout-ms: 10000
```

## 5. 执行机任务超时配置

```yaml
app:
  executor:
    task-timeout-ms: 600000
    step-default-timeout-ms: 30000
```

## 6. 执行并发配置

```yaml
app:
  executor:
    worker-threads: 16
    queue-capacity: 1000
```

## 7. 重试配置

```yaml
app:
  retry:
    enabled: true
    max-attempts: 2
    backoff-ms: 300
```

## 8. 失败策略配置

```yaml
app:
  failure-strategy:
    default: STOP_FLOW
    allow-step-override: true
```

建议值：`STOP_FLOW`、`CONTINUE_ON_FAIL`、`RETRY_THEN_STOP`。

## 9. 环境变量配置

- 环境级变量由脚本服务统一管理。
- 执行机只接收执行包中的环境变量快照。
- 变量名建议大写下划线（如 `API_HOST`）。

## 10. 敏感字段脱敏与加密存储原则

- 敏感字段：密码、token、secret、accessKey、privateKey。
- 存储原则：
  - 配置库中敏感值需加密存储。
  - 日志输出必须脱敏（仅保留前后少量字符）。
- 传输原则：
  - 服务间只传输最小必要敏感信息。
  - 禁止将完整密钥写入业务日志。

## 11. 配置优先级

执行期值覆盖优先级统一为：

`runtime > case > environment > script default > system default`

说明：
- `runtime`：执行时动态注入值。
- `case`：用例覆盖值（`CaseFieldValue`）。
- `environment`：环境变量值。
- `script default`：脚本默认值（`ScriptFieldDefault`）。
- `system default`：系统配置默认值。
