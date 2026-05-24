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
  - `CaseDataSet` 是 T05 兼容模型，正式用例主模型以 `TestCase` / `test_case` 为准，后续任务不得继续扩展 `case_data_set` 新业务能力。
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
  - 确认导入后创建 `step_definition`、`field_config`、`script_field_default`，不创建用例数据。
  - `raw_import_file`、`import_log` 已纳入数据模型和 SQL 基线；当前 Java 实现尚未落地对应 Entity / Mapper / Service。
  - 已覆盖 HAR/Postman 解析、DRAFT 导入确认、PUBLISHED 导入拒绝与 Controller 上传/确认主流程测试。

## T07 - 请求配置与 Payload 内容模型落地

- 前置条件：`db/mysql/V1__init_schema.sql` 与 H2 测试 schema 已存在 `step_request_config`、`step_payload_content`。
- 目标：将请求配置从 `step_definition` 的兼容字段逐步迁移到独立 `StepRequestConfig` 与 `StepPayloadContent` 模型。
- 涉及模块：`new-script-service`、`common-message-codec`（仅在解析 Raw 内容时按需使用）。
- 允许修改范围：`StepRequestConfig`、`StepPayloadContent` 的 Entity、Mapper/XML、Service、DTO、Controller；导入确认写入请求配置与 Payload 内容的最小调整；必要测试。
- 兼容要求：
  - 现有 `step_definition.request_method`、`request_url`、`request_config` 作为 T04/T06 兼容字段保留，不在本任务删除。
  - 新增或导入步骤时，应优先写入 `step_request_config`；兼容字段可同步写入以避免旧接口失效。
- 禁止事项：
  - 不实现 Raw Body 反写树。
  - 不实现 TreeCache。
  - 不实现变量解析、执行机联调、真实 HTTP 调用。
- 验收标准：
  - 可按 `stepId` 查询和保存请求配置。
  - 可保存请求/响应方向的 Body/Header/Cookie/Form 等 Payload 内容。
  - HAR / Postman 确认导入后生成 `StepDefinition + StepRequestConfig + StepPayloadContent + FieldConfig + ScriptFieldDefault`。
  - 已发布版本只读校验仍生效。
- 测试命令：
  - `mvn -pl new-script-service -am test`

## T08 - RawImportFile 与导入日志持久化

- 前置条件：T07 已完成，导入确认可写入独立请求配置和 Payload 内容。
- 目标：补齐导入回溯能力，保存原始导入文件和导入过程日志。
- 涉及模块：`new-script-service`。
- 允许修改范围：`RawImportFile`、`ImportLog` 的 Entity、Mapper/XML、Service、DTO；导入预览/确认服务的日志与原文持久化；必要测试。
- 口径说明：
  - `preview` 仍不得创建 `Script`、`ScriptVersion`、`StepDefinition`、`FieldConfig` 等正式脚本数据。
  - 如 `preview` 为了回溯保存 `raw_import_file`，必须返回 `importFileId`，并明确它不是正式脚本数据。
  - `confirm` 必须关联 `raw_import_file_id`，并写入确认阶段日志。
- 禁止事项：
  - 不实现导入任务异步化。
  - 不实现旧数据迁移。
  - 不实现执行机联调。
- 验收标准：
  - 上传预览可追踪原始文件或返回可用于确认的 `importFileId`。
  - 确认导入后 `script_version.raw_import_file_id` 可关联原始文件。
  - 可查询导入日志，包含解析告警和确认结果。
- 测试命令：
  - `mvn -pl new-script-service -am test`

## T09 - TestCase 正式用例模型与 CaseDataSet 兼容收口

- 前置条件：`test_case` 与扩展后的 `case_field_value.case_id` 已存在于 SQL 基线。
- 目标：引入正式 `TestCase` 用例模型，并将后续用例能力主口径从 `CaseDataSet` 收口到 `TestCase`。
- 涉及模块：`new-script-service`。
- 允许修改范围：`TestCase` Entity、Mapper/XML、Service、Controller、DTO；`CaseFieldValue` 对 `case_id` 的保存与查询；兼容期 `CaseDataSet` 说明和必要适配；必要测试。
- 兼容要求：
  - 保留现有 `/case-data-sets` 接口，避免破坏 T05 已实现测试。
  - 新增正式用例接口使用 `caseId = test_case.id`。
  - 新增执行计划、执行记录、变量 `${case.xxx}` 等后续能力只能引用 `test_case`。
- 禁止事项：
  - 不实现用例完整版本管理。
  - 不实现用例自动跟随最新脚本版本。
  - 不实现用例升级。
  - 不删除 `case_data_set` 表和既有接口。
- 验收标准：
  - 只能基于 `PUBLISHED` 脚本版本创建正式 `TestCase`。
  - `TestCase` 固定绑定 `scriptVersionId`，不自动跟随最新版本。
  - 正式用例字段覆盖值写入 `case_field_value.case_id`。
  - 旧 `CaseDataSet` 测试继续通过。
- 测试命令：
  - `mvn -pl new-script-service -am test`

## T10 - TreeCache 展示缓存与重建能力

- 前置条件：T07 和 T09 已完成，字段、请求配置、Payload、正式用例字段覆盖值均有稳定来源。
- 目标：实现页面展示树缓存，支持脚本版本和正式用例的请求/响应树查询与重建。
- 涉及模块：`new-script-service`、`common-message-codec`（按需解析 JSON / XML / FORM / KEY_VALUE）。
- 允许修改范围：`TreeCache` Entity、Mapper/XML、Service、Controller、DTO；字段树构建和重建服务；必要测试。
- 口径说明：
  - `TreeCache` 只做页面缓存，可删除重建。
  - 执行包和执行机不得以 `TreeCache` 作为唯一执行依据。
  - `owner_type=CASE_DATA_SET` 仅用于兼容旧数据；正式用例使用 `owner_type=TEST_CASE`。
- 禁止事项：
  - 不实现执行机真实执行。
  - 不实现 Raw Body 结构变更自动应用。
  - 不把执行结果反写到脚本默认值或用例覆盖值。
- 验收标准：
  - 可按脚本版本、步骤、方向、位置查询展示树。
  - 可删除并重建脚本版本树缓存。
  - 可基于 `TestCase + CaseFieldValue` 合并生成正式用例树缓存。
  - TreeCache 缺失时可从主数据重建。
- 测试命令：
  - `mvn -pl new-script-service -am test`

## T11 - Raw Body 校验、反写与树缓存联动

- 前置条件：T07、T09、T10 已完成。
- 目标：实现 Raw Body 校验、临时字段树解析、字段匹配、值变化反写和 TreeCache 重建闭环。
- 涉及模块：`new-script-service`、`common-message-codec`。
- 允许修改范围：Raw Body 校验接口、字段匹配服务、`ScriptFieldDefault` / `CaseFieldValue` 值反写、TreeCache 重建联动、必要测试。
- 禁止事项：
  - 不实现二进制 TCP 原文编辑。
  - 不实现 8583 bitmap / BCD / MAC 字节级原文编辑。
  - 不实现 multipart/form-data 完整 boundary 原文编辑。
  - 结构变化不得默认自动应用到 `FieldConfig`。
- 验收标准：
  - JSON / XML / key-value 语法校验可返回错误位置或错误摘要。
  - 仅值变化时可按页面上下文更新 `ScriptFieldDefault` 或 `CaseFieldValue`。
  - 结构变化时返回待确认差异，不自动破坏字段定义。
  - 成功反写后重建对应 TreeCache。
- 测试命令：
  - `mvn -pl new-script-service -am test`

## T12 - 变量引擎基础能力

- 前置条件：T09 已完成，`${case.xxx}` 以正式 `TestCase` / `case_field_value.case_id` 为主口径。
- 目标：实现变量表达式扫描、作用域解析、函数扩展点与未定义变量报错机制。
- 涉及模块：`variable-engine`、`new-executor-service`（仅做最小类型接入）。
- 允许修改范围：变量解析核心能力、变量上下文模型、函数注册骨架、最小单元测试。
- 禁止事项：
  - 不实现执行机完整流程。
  - 不实现真实 HTTP 调用。
  - 不实现提取器和断言业务逻辑。
- 验收标准：
  - 支持 `${env.xxx}`、`${case.xxx}`、`${runtime.xxx}`、`${extract.xxx}`、`${global.xxx}`、`${func.xxx()}` 扫描和解析。
  - 支持混合表达式解析。
  - 未定义变量返回明确错误，不静默替换为空。
  - 可生成解析前/解析后快照数据。
- 测试命令：
  - `mvn clean test`

## T13 - 提取器与断言引擎基础能力

- 前置条件：T12 已完成。
- 目标：实现提取器与断言引擎最小可用能力，为执行机生成标准步骤结果做准备。
- 涉及模块：`extractor-engine`、`assertion-engine`、`new-executor-service`（仅做最小类型接入）。
- 允许修改范围：基础提取规则、基础断言规则、引擎输入输出模型、必要单元测试。
- 禁止事项：
  - 不实现执行计划。
  - 不实现真实 HTTP 调用。
  - 不写入脚本服务业务表。
- 验收标准：
  - JSONPath / XPath / Header / Cookie / Regex / SQL_RESULT 提取最小能力可用。
  - 状态码、响应体包含、JSONPath、XPath、Header、变量值断言最小能力可用。
  - 提取结果和断言结果可序列化为 `StepExecutionResult` 所需结构。
- 测试命令：
  - `mvn clean test`

## T14 - 执行记录模型与 ExecutionResult 落库

- 前置条件：`flow_execution_record`、`step_execution_snapshot` 已存在于 SQL 基线，T12/T13 已定义结果结构来源。
- 目标：在脚本服务落地执行记录查询与标准 `ExecutionResult` 接收落库能力。
- 涉及模块：`new-script-service`、必要公共 DTO。
- 允许修改范围：`FlowExecutionRecord`、`StepExecutionSnapshot` Entity、Mapper/XML、Service、Controller、DTO；`ExecutionResult` / `StepExecutionResult` 接收模型；必要测试。
- 禁止事项：
  - 不实现执行机真实执行。
  - 不实现执行计划全能力。
  - 执行机不得直接写入 `flow_execution_record`、`step_execution_snapshot` 或其他脚本服务业务表。
- 验收标准：
  - `executionId` 由脚本服务生成或接收方校验为脚本服务生成。
  - 可按 `executionId` 查询执行记录摘要。
  - 可按 `executionId` 查询步骤快照列表和单步详情。
  - 可根据标准 `ExecutionResult` 落库 `FlowExecutionRecord` 与 `StepExecutionSnapshot`。
  - 执行记录包含变量解析前快照、解析后快照、最终请求、响应、提取结果、断言结果和错误信息。
- 测试命令：
  - `mvn -pl new-script-service -am test`

## T15 - 执行机最小调试闭环

- 前置条件：T12、T13、T14 已完成。
- 目标：实现执行机从接收调试执行包到生成标准 `ExecutionResult` 的最小闭环，并由脚本服务接收落库。
- 涉及模块：`new-executor-service`、`new-script-service`、`variable-engine`、`extractor-engine`、`assertion-engine`。
- 允许修改范围：执行包 DTO、调试接口、`ExecutionContext`、变量解析接入、请求组装、标准结果返回、脚本服务调试发起和结果接收；必要测试。
- 禁止事项：
  - 不实现执行计划全能力。
  - 不实现高级调度。
  - 执行机不得直接依赖脚本服务业务表。
- 验收标准：
  - 可完成单步调试闭环。
  - `executionId` 由脚本服务生成，执行机原样返回。
  - 脚本服务根据 `ExecutionResult` 落库执行记录和步骤快照。
  - 最终请求、响应、变量快照、提取结果、断言结果可查看。
- 测试命令：
  - `mvn clean test`

## T16 - 执行计划基础能力

- 前置条件：T09、T14、T15 已完成。
- 目标：实现执行计划基础配置、计划用例绑定、计划实例、执行任务触发与结果汇总最小能力。
- 涉及模块：`new-script-service`、`new-executor-service`。
- 允许修改范围：`ExecutionPlan`、`ExecutionPlanCase`、`ExecutionPlanInstance`、`ExecutionTask` 的 Entity、Mapper/XML、Service、Controller、DTO；计划触发；结果汇总；必要测试。
- 禁止事项：
  - 不实现增强能力与高级调度。
  - 不实现用例自动升级。
  - 不实现用例自动跟随最新脚本版本。
- 验收标准：
  - 可创建执行计划。
  - 可将正式 `TestCase` 加入计划并排序。
  - 手动触发后生成 `ExecutionPlanInstance` 与 `ExecutionTask`。
  - 可串行触发最小执行并汇总结果。
  - 执行计划历史可查询到实例、任务和关联 `executionId`。
- 测试命令：
  - `mvn clean test`
