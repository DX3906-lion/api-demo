# DATA_MODEL.md

## 1. 数据模型设计原则

- `Script` 管脚本主信息。
- `ScriptVersion` 管脚本版本，`DRAFT` 可编辑，`PUBLISHED` 只读。
- `StepDefinition` 管脚本步骤。
- `FieldConfig` 管字段结构，是字段结构主数据。
- `ScriptFieldDefault` 管脚本默认字段值。
- `CaseFieldValue` 管用例覆盖字段值。
- `TreeCache` 只做页面展示缓存，可重建。
- `RawMessage` / `RawImportFile` 只做原始导入内容和回溯，不作为执行主依据。
- `StepExecutionSnapshot` 保存执行事实，执行后的解析值不反写脚本或用例。
- 用例绑定 `scriptVersionId`，不自动跟随最新脚本版本。
- 不设计 `CaseEnvFieldValue`。
- 环境差异通过 `${env.xxx}`、环境变量或外部数据集解决。

## 2. 核心实体关系

```text
Script
  └── ScriptVersion
        ├── StepDefinition
        │     ├── StepRequestConfig
        │     ├── SqlStepConfig
        │     ├── StepPayloadContent
        │     ├── FieldConfig
        │     ├── ScriptFieldDefault
        │     ├── StepResponseSample
        │     ├── ExtractorConfig
        │     └── AssertionConfig
        └── TestCase
              └── CaseFieldValue

ExecutionPlan
  ├── ExecutionPlanCase
  └── ExecutionPlanInstance
        └── ExecutionTask
              └── FlowExecutionRecord
                    └── StepExecutionSnapshot

Environment
  ├── EnvironmentConfig
  └── EnvironmentVariable
```

## 3. 状态与枚举

### 3.1 ScriptVersionStatus

| 枚举 | 说明 |
|---|---|
| DRAFT | 草稿，可编辑 |
| PUBLISHED | 已发布，只读，可生成用例 |
| DISABLED | 停用，不允许新建用例 |
| ARCHIVED | 归档，仅查看 |

### 3.2 ScriptSourceType

| 枚举 | 说明 |
|---|---|
| BLANK | 空白新建 |
| HAR_IMPORT | HAR 导入 |
| POSTMAN_IMPORT | Postman 导入 |
| COPY | 复制生成 |
| MIGRATION | 旧数据迁移 |

### 3.3 StepType

| 枚举 | 说明 |
|---|---|
| API_STEP | 接口步骤 |
| SQL_STEP | SQL 步骤 |
| IF_ELSE | 条件判断 |
| FOR_LOOP | 循环 |
| WAIT | 等待 |
| COMPONENT | 组件 |
| PRE_PROCESSOR | 前置处理 |
| POST_PROCESSOR | 后置处理 |

### 3.4 FieldDirection

| 枚举 | 说明 |
|---|---|
| REQUEST | 请求侧字段 |
| RESPONSE | 响应侧字段 |

### 3.5 FieldLocation

| 枚举 | 说明 |
|---|---|
| PATH | URL path 参数 |
| QUERY | URL query 参数 |
| HEADER | Header |
| COOKIE | Cookie |
| BODY | Body |
| FORM | 表单参数 |

### 3.6 ValueMode

| 枚举 | 说明 |
|---|---|
| LITERAL | 普通固定值 |
| VARIABLE | 单变量，如 `${case.amount}` |
| FUNCTION | 函数，如 `${func.uuid()}` |
| MIXED | 混合表达式 |
| EMPTY | 空值 |

## 4. 脚本域

### 4.1 script

| 字段 | 类型 | 说明 |
|---|---|---|
| id | varchar(64) | 主键 |
| system_id | varchar(64) | 所属系统 |
| module_id | varchar(64) | 所属模块 |
| script_code | varchar(128) | 脚本编码 |
| script_name | varchar(255) | 脚本名称 |
| protocol_type | varchar(32) | HTTP / TCP / MQ |
| body_format | varchar(32) | JSON / XML / FORM / KEY_VALUE / TEXT |
| source_type | varchar(32) | BLANK / HAR_IMPORT / POSTMAN_IMPORT / COPY |
| latest_draft_version_id | varchar(64) | 最新草稿版本 ID |
| latest_published_version_id | varchar(64) | 最新发布版本 ID |
| owner_id | varchar(64) | 负责人 |
| tags | text | 标签 JSON |
| description | text | 描述 |
| status | varchar(32) | ENABLED / DISABLED / ARCHIVED |
| created_by | varchar(64) | 创建人 |
| updated_by | varchar(64) | 更新人 |
| created_time | datetime | 创建时间 |
| updated_time | datetime | 更新时间 |

约束：

```sql
UNIQUE KEY uk_script_name (system_id, script_name);
UNIQUE KEY uk_script_code (system_id, script_code);
```

### 4.2 script_version

| 字段 | 类型 | 说明 |
|---|---|---|
| id | varchar(64) | 主键 |
| script_id | varchar(64) | 脚本 ID |
| version_no | int | 版本号 |
| version_name | varchar(64) | 版本名称 |
| status | varchar(32) | DRAFT / PUBLISHED / DISABLED / ARCHIVED |
| source_type | varchar(32) | BLANK / HAR_IMPORT / POSTMAN_IMPORT / COPY |
| source_version_id | varchar(64) | 来源版本 ID |
| raw_import_file_id | varchar(64) | 导入文件 ID |
| field_config_version | int | 字段结构版本 |
| published_time | datetime | 发布时间 |
| published_by | varchar(64) | 发布人 |
| created_by | varchar(64) | 创建人 |
| updated_by | varchar(64) | 更新人 |
| created_time | datetime | 创建时间 |
| updated_time | datetime | 更新时间 |

规则：

- `DRAFT` 可编辑。
- `PUBLISHED` 只读。
- 用例只能绑定 `PUBLISHED` 版本。

### 4.3 step_definition

| 字段 | 类型 | 说明 |
|---|---|---|
| id | varchar(64) | 主键 |
| script_version_id | varchar(64) | 脚本版本 ID |
| parent_step_id | varchar(64) | 父步骤 ID |
| step_type | varchar(32) | 步骤类型 |
| step_name | varchar(255) | 步骤名称 |
| order_no | int | 步骤顺序 |
| enabled | char(1) | 是否启用 |
| failure_strategy | varchar(32) | 失败策略 |
| retry_count | int | 重试次数 |
| timeout_ms | int | 超时时间 |
| description | text | 描述 |
| created_time | datetime | 创建时间 |
| updated_time | datetime | 更新时间 |

## 5. 请求与字段域

### 5.1 step_request_config

| 字段 | 类型 | 说明 |
|---|---|---|
| id | varchar(64) | 主键 |
| step_id | varchar(64) | 步骤 ID |
| method | varchar(16) | GET / POST / PUT / DELETE |
| url_template | text | URL 模板 |
| protocol_type | varchar(32) | HTTP / TCP |
| content_type | varchar(255) | Content-Type |
| body_format | varchar(32) | JSON / XML / TEXT / KEY_VALUE / FORM |
| charset | varchar(32) | 编码 |
| timeout_ms | int | 请求超时 |
| follow_redirect | char(1) | 是否跟随重定向 |
| config_json | text | 扩展配置 |

### 5.2 field_config

字段结构主数据。逻辑上是树，数据库中扁平存储，通过 `parent_id + index_num` 组装树。

| 字段 | 类型 | 说明 |
|---|---|---|
| id | varchar(64) | 主键 fieldId |
| script_version_id | varchar(64) | 脚本版本 ID |
| step_id | varchar(64) | 步骤 ID |
| parent_id | varchar(64) | 父字段 ID |
| stable_field_key | varchar(500) | 稳定字段标识 |
| code | varchar(255) | 字段编码 |
| cname | varchar(255) | 字段中文名 |
| direction | varchar(16) | REQUEST / RESPONSE |
| location | varchar(32) | PATH / QUERY / HEADER / COOKIE / BODY / FORM |
| xpath | varchar(1000) | XML XPath 或逻辑路径 |
| json_pointer | varchar(1000) | JSON Pointer |
| original_path | varchar(1000) | 原始路径 |
| node_type | varchar(32) | 节点类型 |
| data_type | varchar(32) | 数据类型 |
| index_num | int | 同级顺序 |
| is_leaf | char(1) | 是否叶子节点 |
| is_required | char(1) | 是否必填 |
| default_is_send | char(1) | 默认是否上送 |
| allow_add | char(1) | 是否允许新增 |
| empty_node_strategy | varchar(64) | 空节点策略 |
| length | varchar(32) | 长度 |
| scale | varchar(32) | 小数位 |
| remark | text | 字段备注 |
| format_meta_json | text | 格式元数据 |
| status | varchar(32) | ENABLED / DISABLED |

### 5.3 script_field_default

脚本默认字段值。

| 字段 | 类型 | 说明 |
|---|---|---|
| id | varchar(64) | 主键 |
| script_version_id | varchar(64) | 脚本版本 ID |
| step_id | varchar(64) | 步骤 ID |
| field_id | varchar(64) | FieldConfig ID |
| raw_input | text | 用户输入原始值或变量表达式 |
| value | text | 当前值，第一阶段可等于 raw_input |
| value_mode | varchar(32) | LITERAL / VARIABLE / FUNCTION / MIXED / EMPTY |
| value_source | varchar(32) | 值来源 |
| is_parameterization | char(1) | 是否参数化 |
| variables_json | text | 变量列表 |
| is_send | char(1) | 是否上送 |
| original_value | text | 原始导入值 |
| created_time | datetime | 创建时间 |
| updated_time | datetime | 更新时间 |

约束：

```sql
UNIQUE KEY uk_script_default_field (script_version_id, step_id, field_id);
```

### 5.4 tree_cache

页面展示缓存。

| 字段 | 类型 | 说明 |
|---|---|---|
| id | varchar(64) | 主键 |
| owner_type | varchar(32) | SCRIPT_VERSION / CASE |
| owner_id | varchar(64) | scriptVersionId 或 caseId |
| script_version_id | varchar(64) | 脚本版本 ID |
| step_id | varchar(64) | 步骤 ID |
| direction | varchar(16) | REQUEST / RESPONSE |
| location | varchar(32) | BODY / HEADER / QUERY 等 |
| field_config_version | int | 字段结构版本 |
| tree_json | longtext | 树 JSON |
| tree_hash | varchar(128) | 树 Hash |
| created_time | datetime | 创建时间 |
| updated_time | datetime | 更新时间 |

规则：

- 可删除重建。
- 不作为执行依据。

## 6. 用例域

### 6.1 test_case

| 字段 | 类型 | 说明 |
|---|---|---|
| id | varchar(64) | 主键 |
| system_id | varchar(64) | 所属系统 |
| script_id | varchar(64) | 脚本 ID |
| script_version_id | varchar(64) | 绑定脚本版本 ID |
| case_code | varchar(128) | 用例编码 |
| case_name | varchar(255) | 用例名称 |
| module_id | varchar(64) | 所属模块 |
| description | text | 描述 |
| status | varchar(32) | DRAFT / ENABLED / DISABLED / ARCHIVED |
| tags | text | 标签 JSON |
| owner_id | varchar(64) | 负责人 |
| created_by | varchar(64) | 创建人 |
| updated_by | varchar(64) | 更新人 |
| created_time | datetime | 创建时间 |
| updated_time | datetime | 更新时间 |

### 6.2 case_field_value

用例字段覆盖值。

| 字段 | 类型 | 说明 |
|---|---|---|
| id | varchar(64) | 主键 |
| case_id | varchar(64) | 用例 ID |
| script_version_id | varchar(64) | 脚本版本 ID |
| step_id | varchar(64) | 步骤 ID |
| field_id | varchar(64) | FieldConfig ID |
| field_code_snapshot | varchar(255) | 字段编码快照 |
| field_path_snapshot | varchar(1000) | 字段路径快照 |
| raw_input | text | 用户输入原始值 |
| value | text | 当前值 |
| original_value | text | 修改前值 |
| value_mode | varchar(32) | LITERAL / VARIABLE / FUNCTION / MIXED / EMPTY |
| value_source | varchar(32) | 值来源 |
| is_parameterization | char(1) | 是否参数化 |
| variables_json | text | 变量列表 |
| is_send | char(1) | 是否上送 |
| version | int | 乐观锁 |
| created_time | datetime | 创建时间 |
| updated_time | datetime | 更新时间 |

约束：

```sql
UNIQUE KEY uk_case_field (case_id, step_id, field_id);
```

## 7. 执行计划域

### 7.1 execution_plan

| 字段 | 类型 | 说明 |
|---|---|---|
| id | varchar(64) | 主键 |
| system_id | varchar(64) | 所属系统 |
| plan_name | varchar(255) | 计划名称 |
| env_id | varchar(64) | 默认执行环境 |
| engine_version | varchar(16) | V1 / V2 |
| run_mode | varchar(32) | MANUAL / SCHEDULED |
| schedule_cron | varchar(128) | 定时表达式 |
| case_run_mode | varchar(32) | SERIAL / PARALLEL |
| max_concurrency | int | 最大并发数 |
| failure_strategy | varchar(32) | 失败策略 |
| retry_count | int | 重试次数 |
| status | varchar(32) | ENABLED / DISABLED |
| created_by | varchar(64) | 创建人 |
| updated_by | varchar(64) | 更新人 |
| created_time | datetime | 创建时间 |
| updated_time | datetime | 更新时间 |

### 7.2 execution_task

| 字段 | 类型 | 说明 |
|---|---|---|
| id | varchar(64) | 主键 |
| plan_instance_id | varchar(64) | 计划实例 ID |
| plan_id | varchar(64) | 计划 ID |
| case_id | varchar(64) | 用例 ID |
| script_version_id | varchar(64) | 脚本版本 ID |
| env_id | varchar(64) | 环境 ID |
| engine_version | varchar(16) | V1 / V2 |
| status | varchar(32) | WAITING / DISPATCHED / RUNNING / SUCCESS / FAILED |
| order_no | int | 顺序 |
| retry_count | int | 当前重试次数 |
| executor_node_id | varchar(64) | 执行机节点 ID |
| dispatch_time | datetime | 下发时间 |
| start_time | datetime | 开始时间 |
| end_time | datetime | 结束时间 |
| error_message | text | 错误信息 |

## 8. 执行记录域

### 8.1 flow_execution_record

| 字段 | 类型 | 说明 |
|---|---|---|
| id | varchar(64) | 主键 executionId |
| task_id | varchar(64) | 执行任务 ID |
| plan_instance_id | varchar(64) | 计划实例 ID |
| case_id | varchar(64) | 用例 ID |
| script_id | varchar(64) | 脚本 ID |
| script_version_id | varchar(64) | 脚本版本 ID |
| env_id | varchar(64) | 环境 ID |
| execution_type | varchar(32) | DEBUG_STEP / DEBUG_FLOW / PLAN / MANUAL_CASE |
| status | varchar(32) | RUNNING / SUCCESS / FAILED / PARTIAL_SUCCESS |
| duration_ms | bigint | 耗时 |
| env_snapshot_json | text | 环境快照 |
| variable_snapshot_json | text | 初始变量快照 |
| triggered_by | varchar(64) | 触发人 |
| start_time | datetime | 开始时间 |
| end_time | datetime | 结束时间 |

### 8.2 step_execution_snapshot

| 字段 | 类型 | 说明 |
|---|---|---|
| id | varchar(64) | 主键 |
| execution_id | varchar(64) | 执行记录 ID |
| step_id | varchar(64) | 步骤 ID |
| step_order_no | int | 步骤顺序 |
| step_name | varchar(255) | 步骤名称快照 |
| step_type | varchar(32) | 步骤类型 |
| status | varchar(32) | SUCCESS / FAILED / SKIPPED |
| resolved_method | varchar(16) | 最终 Method |
| resolved_url | text | 最终 URL |
| resolved_query_json | text | 最终 Query |
| resolved_request_headers_json | text | 最终请求头 |
| resolved_request_cookies_json | text | 最终 Cookie |
| resolved_request_body | longtext | 最终请求体 |
| response_status_code | int | 响应状态码 |
| response_headers_json | text | 响应头 |
| response_cookies_json | text | 响应 Cookie |
| response_body | longtext | 响应体 |
| response_content_type | varchar(255) | 响应 Content-Type |
| response_time_ms | bigint | 响应耗时 |
| raw_input_snapshot_json | text | 字段解析前表达式 |
| resolved_field_value_json | text | 字段解析后值 |
| extracted_variables_json | text | 提取变量结果 |
| assert_result_json | text | 断言结果 |
| execution_log | longtext | 执行日志 |
| error_message | text | 错误信息 |
| start_time | datetime | 开始时间 |
| end_time | datetime | 结束时间 |

## 9. 关键规则

### 9.1 字段值取值优先级

```text
执行临时覆盖值
  > CaseFieldValue.rawInput
  > ScriptFieldDefault.rawInput
  > FieldConfig.defaultValue
  > 空值
```

### 9.2 是否上送优先级

```text
CaseFieldValue.isSend
  > ScriptFieldDefault.isSend
  > FieldConfig.defaultIsSend
  > 1
```

### 9.3 变量解析规则

```text
${env.xxx}        从环境变量取值
${case.xxx}       从用例变量或 CaseFieldValue 取值
${runtime.xxx}    执行时生成
${extract.xxx}    前置步骤提取结果
${global.xxx}     用户全局变量
${func.xxx()}     平台函数
```

## 10. 不纳入本版的数据模型

- `CaseEnvFieldValue`
- `CaseVersion`
- 用例自动跟随最新脚本版本
- 二进制 TCP 原文编辑模型
- 8583 bitmap / BCD / MAC 字节级字段模型
- multipart/form-data 完整 boundary 原文编辑模型
- 执行后字段值反写模型

## 11. Java 实体注释要求

- 每个实体类必须有类级 JavaDoc。
- 每个实体字段必须有 JavaDoc 或清晰注释。
- 枚举类必须说明每个枚举值含义。
- 涉及字段优先级、版本状态、执行状态的字段必须注明业务含义。
