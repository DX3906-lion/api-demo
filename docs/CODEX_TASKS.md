# CODEX_TASKS.md

> 规则：每轮仅执行一个任务；不得跨任务提前实现。

## T01 - Maven 多模块工程骨架（首个编码任务）

- 目标：建立单仓库 Maven 多模块工程基础结构。
- 涉及模块：根工程、`new-script-service`、`new-executor-service`、`common-message-codec`、`common-variable-engine`、`common-extractor-engine`、`common-assertion-engine`。
- 允许修改范围：工程目录结构、父子模块配置、基础启动类、健康检查依赖、统一响应与异常处理基础骨架。
- 禁止事项：
  - 不实现导入能力。
  - 不实现执行机真实执行流程。
  - 不实现变量引擎、提取器、断言业务逻辑。
  - 不实现数据库业务表与业务接口。
- 验收标准：
  - 模块结构齐全。
  - 两个服务可独立启动。
  - 健康检查可访问。
  - `mvn clean test` 可执行并可验证（允许空测试骨架）。
- 测试命令：
  - `mvn clean test`
  - `mvn -pl new-script-service spring-boot:run`
  - `mvn -pl new-executor-service spring-boot:run`
- 若 Codex 运行环境出现 Maven Central 403 或网络限制，必须在输出中说明：失败命令、失败摘要、问题归类（代码/依赖下载/环境网络），并给出本地复测命令。

## T02 - common-core 基础能力（统一响应/异常/错误码/分页/基础工具）

- 目标：在 `common-core` 建立后续业务复用的基础能力骨架。
- 涉及模块：`common-core`（必要时允许服务模块最小接入验证）。
- 允许修改范围：基础响应模型、基础异常模型、错误码枚举、分页对象、基础工具类骨架。
- 禁止事项：
  - 不实现 Script/Version/Step 业务实体与 CRUD。
  - 不实现导入、执行、变量、提取、断言业务逻辑。
- 验收标准：
  - `common-core` 提供可复用的最小公共类型。
  - 相关模块编译与测试可通过。
- 测试命令：
  - `mvn clean test`

## T03 - 数据库初始化脚本与持久化约定（当前任务）

- 目标：落地数据库初始化目录、迁移命名规范与持久化约定。
- 涉及模块：仓库根目录数据库脚本目录、`new-script-service` 持久化约定文档与配置。
- 允许修改范围：数据库初始化目录、迁移规范文档、持久化基础约定。
- 禁止事项：
  - 不实现业务 CRUD。
  - 不实现导入预览与确认。
- 验收标准：
  - 持久化约定清晰可执行。
  - 初始化目录与命名规则明确。
- 测试命令：
  - `mvn clean test`

## T04 - Script / ScriptVersion / StepDefinition 基础模型与 CRUD

- 前置条件：开始 T04 前必须确认 `docs/DATA_MODEL.md` 与 `db/mysql/V1__init_schema.sql` 字段命名一致，尤其 `created_time` / `updated_time`。
- 目标：实现脚本、版本、步骤的最小闭环管理。
- 涉及模块：`new-script-service`。
- 允许修改范围：实体、Repository/Mapper、Service、Controller、基础 DTO。
- 禁止事项：
  - 不实现导入解析。
  - 不实现执行机交互。
- 验收标准：
  - 可新建脚本并生成 DRAFT 版本。
  - 可新增与编辑步骤。
  - PUBLISHED 版本只读校验生效。
- T04 验收说明：
  - 已实现 `Script`、`ScriptVersion`、`StepDefinition` 基础 CRUD 闭环。
  - 创建脚本同步创建默认 `DRAFT` 版本。
  - 发布版本后步骤新增、更新、删除会返回 `VERSION_STATUS_INVALID`。
  - 测试覆盖 Service 事务规则与 Controller 主流程。
- 测试命令：
  - `mvn -pl new-script-service test`

## T05 - 字段配置、字段默认值、用例数据模型

- 目标：实现 `FieldConfig`、`ScriptFieldDefault`、`CaseFieldValue` 与基础展示模型。
- 涉及模块：`new-script-service`、`common-message-codec`（必要范围）。
- 允许修改范围：字段模型、字段默认值模型、用例覆盖值模型、基础查询接口。
- 禁止事项：
  - 不实现导入预览与确认。
  - 不实现执行机真实执行。
- 验收标准：
  - 字段结构与默认值可维护。
  - 用例覆盖值可维护。
- T05 验收说明：
  - 已实现 `FieldConfig`、`ScriptFieldDefault`、`CaseDataSet`、`CaseFieldValue` 最小维护接口。
  - `field_config` 与 `script_field_default` 仅允许在 `DRAFT` 版本下维护。
  - `case_data_set` 与 `case_field_value` 允许在 `DRAFT` 或 `PUBLISHED` 版本下维护。
  - `case_field_value` 保存时校验字段配置必须属于同一 `script_version_id`。
  - 测试覆盖 Service 版本状态规则、upsert 规则、跨版本字段引用拦截与 Controller 主流程。
- 测试命令：
  - `mvn -pl new-script-service test`

## T06 - 导入预览与导入确认

- 目标：实现导入创建脚本的预览与确认闭环。
- 涉及模块：`new-script-service`、`common-message-codec`（必要范围）。
- 允许修改范围：导入接口、导入服务、导入日志、RawImportFile 持久化。
- 禁止事项：
  - 不实现执行机联调。
  - 不实现复杂变量与断言逻辑。
- 验收标准：
  - 支持预览、过滤、确认导入。
  - 确认后生成 Script + DRAFT ScriptVersion + StepDefinition + StepRequestConfig。
- 测试命令：
  - `mvn -pl new-script-service test`
- T06 验收说明：
  - 已实现 HAR / Postman JSON 文件导入预览，预览仅解析并返回 steps、fields、warnings，不落库。
  - 已实现导入确认到指定 `scriptId + versionId` 的 `DRAFT` 版本。
  - 确认导入后创建 `step_definition`、`field_config`、`script_field_default`，不创建用例数据和导入任务表。
  - 已覆盖 HAR/Postman 解析、DRAFT 导入确认、PUBLISHED 导入拒绝与 Controller 上传/确认主流程测试。

## T07 - 变量引擎基础能力

- 目标：实现变量表达式扫描、作用域解析与未定义变量报错机制。
- 涉及模块：`variable-engine`、`new-executor-service`（最小接入）。
- 允许修改范围：变量解析核心能力与最小接入。
- 禁止事项：
  - 不实现执行机完整流程。
- 验收标准：
  - 支持 `${env}` `${case}` `${runtime}` `${extract}` `${global}` `${func}` 基础解析。
- 测试命令：
  - `mvn clean test`

## T08 - 提取器与断言引擎基础能力

- 目标：实现提取器与断言引擎最小可用能力。
- 涉及模块：`extractor-engine`、`assertion-engine`、`new-executor-service`（最小接入）。
- 允许修改范围：基础提取规则、基础断言规则、最小引擎接入。
- 禁止事项：
  - 不实现执行计划。
- 验收标准：
  - 最小提取与断言链路可用。
- 测试命令：
  - `mvn clean test`

## T09 - 执行机最小闭环

- 目标：实现执行机从接收任务到生成标准执行结果的最小闭环。
- 涉及模块：`new-executor-service`、`new-script-service`（结果接收展示最小接入）。
- 允许修改范围：任务接收、上下文组装、最小执行结果输出。
- 禁止事项：
  - 不实现执行计划全能力。
- 验收标准：
  - 可完成最小调试执行闭环并返回标准结果。
- 测试命令：
  - `mvn clean test`

## T10 - 执行计划基础能力

- 目标：实现执行计划基础配置、触发与结果汇总最小能力。
- 涉及模块：`new-script-service`、`new-executor-service`。
- 允许修改范围：计划基础模型、计划触发、结果汇总。
- 禁止事项：
  - 不实现增强能力与高级调度。
- 验收标准：
  - 可创建计划并触发最小执行。
- 测试命令：
  - `mvn clean test`
