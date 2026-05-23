# AGENTS.md

本文件用于约束 Codex / AI 编码行为。所有实现必须遵守以下规则。

## 1. 技术栈规则

- 使用 Spring Boot 2.7.18。
- 使用 MySQL 8.0。
- 默认使用 Maven 管理依赖。
- Java 版本优先使用 Java 8 或 Java 11，具体以后端项目基线为准。
- 数据库脚本需兼容 MySQL 8.0。
- 不引入未经确认的重量级框架。
- 不在业务代码中硬编码环境地址、账号、密码、token、密钥。

## 2. 注释规则

- 每个 interface 的每个方法必须添加 JavaDoc 注释。
- 每个实体类字段必须添加注释，建议使用 JavaDoc 或清晰的行内注释。
- Controller、Service、Repository 的公共方法必须说明用途、关键参数和返回值。
- 业务逻辑复杂、存在状态流转、字段优先级、变量解析、版本升级、执行调度时，必须添加必要注释。
- 不允许使用无意义注释，例如“设置值”“获取值”这类重复代码含义的注释。
- 枚举值必须添加含义说明。

## 3. 架构边界规则

- `new-script-service` 负责脚本、版本、步骤、字段、变量、提取器、断言、用例、执行计划等配置管理。
- `new-executor-service` 负责执行、变量解析、最终报文组装、请求发送、响应解析、变量提取、断言执行和执行快照。
- 执行期逻辑不得写入脚本服务。
- 脚本服务不得直接执行真实 API / SQL 请求，调试也必须通过执行机服务。
- 公共解析能力放入 `message-codec`。
- 变量解析能力放入 `variable-engine`。
- 提取能力放入 `extractor-engine`。
- 断言能力放入 `assertion-engine`。

## 4. 数据规则

- `FieldConfig` 是字段结构主数据。
- `ScriptFieldDefault` 是脚本默认字段值。
- `CaseFieldValue` 是用例覆盖字段值。
- `TreeCache` 是页面展示缓存，可删除重建，不能作为唯一执行依据。
- `RawMessage` / `RawImportFile` 用于原始导入文件和回溯，不作为执行主依据。
- `StepExecutionSnapshot` 保存执行事实。
- 执行后的解析值不得反写 `ScriptFieldDefault` 或 `CaseFieldValue`。
- 不实现 `CaseEnvFieldValue`。
- 用例必须绑定 `scriptVersionId`。
- 用例不自动跟随最新脚本版本。

## 5. 版本规则

- `ScriptVersion` 状态包括：`DRAFT`、`PUBLISHED`、`DISABLED`、`ARCHIVED`。
- `DRAFT` 可编辑。
- `PUBLISHED` 只读。
- 编辑已发布版本时，必须复制生成新的 `DRAFT`。
- 用例只能基于 `PUBLISHED` 脚本版本创建。
- 批量升级用例必须先做预检查和字段映射。

## 6. 变量规则

- 支持变量格式：
  - `${env.xxx}`
  - `${case.xxx}`
  - `${runtime.xxx}`
  - `${extract.xxx}`
  - `${global.xxx}`
  - `${func.xxx()}`
- 变量只在执行期解析。
- 变量未定义必须报错，不允许静默替换为空。
- 前置步骤提取变量写入 `ExecutionContext.extract`。
- 执行记录必须保存变量解析前和解析后的快照。

## 7. 代码规则

- Controller 只做参数接收、参数校验和结果返回。
- Service 负责业务编排。
- Repository / Mapper 只负责数据访问。
- 禁止在 Controller 中直接写复杂业务逻辑。
- 禁止重复实现 JSON / XML / key-value 解析逻辑。
- 每个模块应有清晰包结构。
- 新增核心功能必须补充单元测试或集成测试。
- 所有新增接口必须返回统一响应结构。
- 所有新增异常必须转换为统一错误码或错误响应。

## 8. 禁止事项

- 不实现用例完整版本管理。
- 不实现用例自动跟随最新脚本版本。
- 不实现二进制 TCP 原文编辑。
- 不实现 8583 bitmap / BCD / MAC 字节级原文编辑。
- 不实现 multipart/form-data 完整 boundary 原文编辑。
- 不把执行后的最终值写回用例。
