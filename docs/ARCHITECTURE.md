# ARCHITECTURE.md

## 1. 总体架构

系统拆分为两个主要服务和若干公共模块：

```text
new-script-service
  负责脚本、版本、字段、变量、用例、执行计划等配置管理

new-executor-service
  负责执行期变量解析、最终报文组装、请求执行、响应解析、提取、断言和快照

common modules
  message-codec
  variable-engine
  extractor-engine
  assertion-engine
```

## 2. 技术基线

- Spring Boot 2.7.18
- MySQL 8.0
- Maven
- Java 8+ 或 Java 11
- REST API 通信
- 后续可引入消息队列支持执行任务异步调度

## 3. new-script-service 职责

- 脚本列表管理
- HAR / Postman 导入
- 导入过滤和导入预览
- 脚本 DRAFT / PUBLISHED 版本管理
- 步骤编排
- 请求配置管理
- `FieldConfig` 管理
- `ScriptFieldDefault` 管理
- `TreeCache` 管理
- 响应样例管理
- 变量中心
- 变量提取配置
- 断言配置
- SQL 步骤配置
- 环境配置
- 用例管理
- `CaseFieldValue` 管理
- 执行计划管理
- 执行任务生成
- 执行结果汇总展示
- 编排期调试任务发起
- 调试结果展示

## 4. new-executor-service 职责

- 接收单步调试任务
- 接收全流程调试任务
- 接收执行计划任务
- 加载执行快照或执行包
- 创建 `ExecutionContext`
- 解析变量
- 组装最终请求
- 执行 API 请求
- 执行 SQL 步骤
- 执行控制流
- 解析响应
- 执行变量提取
- 执行断言
- 保存 `FlowExecutionRecord`
- 保存 `StepExecutionSnapshot`
- 返回执行结果摘要
- 上报执行任务状态

## 5. 公共模块职责

### 5.1 message-codec

- JSON parse / build
- XML parse / build
- key-value parse / build
- x-www-form-urlencoded parse / build
- Header / Cookie parse
- Raw Body 反写树辅助
- 响应内容解析

### 5.2 variable-engine

- 变量表达式扫描
- 变量作用域解析
- 函数执行
- 变量未定义检测
- 变量解析快照生成

### 5.3 extractor-engine

- JSONPath 提取
- XPath 提取
- Header 提取
- Cookie 提取
- Regex 提取
- SQL_RESULT 提取

### 5.4 assertion-engine

- 状态码断言
- JSONPath 断言
- XPath 断言
- Header 断言
- Cookie 断言
- 响应体包含 / 不包含断言
- 正则断言
- SQL 结果断言
- 变量值断言

## 6. 编排期调试调用关系

```text
用户点击调试
  ↓
new-script-service 保存草稿
  ↓
new-script-service 生成 DebugExecutionPackage
  ↓
new-executor-service 执行调试任务
  ↓
new-executor-service 保存执行快照
  ↓
new-script-service 查询并展示调试结果
  ↓
用户确认后可保存响应为 StepResponseSample
```

## 7. 正式执行调用关系

```text
执行计划触发
  ↓
new-script-service 生成 ExecutionPlanInstance
  ↓
new-script-service 生成 ExecutionTask
  ↓
new-executor-service 执行任务
  ↓
new-executor-service 保存执行明细
  ↓
new-script-service 汇总计划结果
```

## 8. 服务边界约束

- 脚本服务不直接执行真实请求。
- 执行机服务不修改脚本版本、用例配置、字段定义。
- 执行后的最终值只进入执行快照。
- `TreeCache` 只做页面缓存。
- 响应样例必须由用户确认后保存，不自动覆盖。
