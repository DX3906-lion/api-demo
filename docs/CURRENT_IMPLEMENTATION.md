# CURRENT_IMPLEMENTATION.md

## 1. 盘点范围

生成日期：2026-05-24

本文件仅盘点当前仓库实现现状，不修改业务代码、不新增表、不调整接口。

已扫描范围：

- 根目录文档与工程文件：`README.md`、`AGENTS.md`、`pom.xml`
- 设计基线文档：`docs/*.md`
  - `docs/API_CONTRACT.md`
  - `docs/ARCHITECTURE.md`
  - `docs/BUSINESS_FLOW.md`
  - `docs/CODEX_TASKS.md`
  - `docs/CONFIGURATION.md`
  - `docs/DATA_MODEL.md`
  - `docs/DATABASE_MIGRATION.md`
  - `docs/DEPENDENCY_MATRIX.md`
  - `docs/DEV_SETUP.md`
  - `docs/ERROR_CODE.md`
  - `docs/IMPLEMENTATION_PLAN.md`
- Maven 模块与配置：根 `pom.xml`、各子模块 `pom.xml`
- 主业务源码：`common-core/src/main/java`、`new-script-service/src/main/java`、`new-executor-service/src/main/java`
- 测试源码与测试资源：`common-core/src/test/java`、`new-script-service/src/test/java`、`new-script-service/src/test/resources`、`new-executor-service/src/test/java`
- MyBatis XML：`new-script-service/src/main/resources/mapper/*.xml`
- SQL 脚本：`db/README.md`、`db/mysql/*.sql`、`new-script-service/src/test/resources/db/schema-h2.sql`
- 服务配置：`new-script-service/src/main/resources/application.yml`、`new-executor-service/src/main/resources/application.yml`、`new-script-service/src/test/resources/application-test.yml`

未纳入盘点范围：

- `.git`、`.idea`
- 编译输出目录（当前未扫描 `target` 类输出）

## 2. 当前项目结构

| 路径 | 当前内容概览 |
|---|---|
| `common-core` | 公共响应、异常、错误码、分页、基础实体、校验错误与 ID 工具 |
| `message-codec` | 当前仅有模块 `pom.xml` |
| `variable-engine` | 当前仅有模块 `pom.xml` |
| `extractor-engine` | 当前仅有模块 `pom.xml` |
| `assertion-engine` | 当前仅有模块 `pom.xml` |
| `new-script-service` | 脚本服务启动类、Controller、DTO、Entity、Mapper、Service、导入解析器、Mapper XML、测试 |
| `new-executor-service` | 执行机服务启动类、健康检查 Controller、测试 |
| `db/mysql` | MySQL 初始化脚本与演示数据脚本 |
| `docs` | 设计基线文档与本次当前实现盘点文档 |

## 3. 当前 Entity

### 3.1 公共基础模型

| 类 | 路径 | 说明 |
|---|---|---|
| `BaseEntity` | `common-core/src/main/java/com/apidemo/common/model/BaseEntity.java` | 公共基础字段模型 |

### 3.2 new-script-service 业务实体

| Entity | 路径 | 对应当前表 |
|---|---|---|
| `ScriptEntity` | `new-script-service/src/main/java/com/apidemo/script/entity/ScriptEntity.java` | `script` |
| `ScriptVersionEntity` | `new-script-service/src/main/java/com/apidemo/script/entity/ScriptVersionEntity.java` | `script_version` |
| `StepDefinitionEntity` | `new-script-service/src/main/java/com/apidemo/script/entity/StepDefinitionEntity.java` | `step_definition` |
| `FieldConfigEntity` | `new-script-service/src/main/java/com/apidemo/script/entity/FieldConfigEntity.java` | `field_config` |
| `ScriptFieldDefaultEntity` | `new-script-service/src/main/java/com/apidemo/script/entity/ScriptFieldDefaultEntity.java` | `script_field_default` |
| `CaseDataSetEntity` | `new-script-service/src/main/java/com/apidemo/script/entity/CaseDataSetEntity.java` | `case_data_set` |
| `CaseFieldValueEntity` | `new-script-service/src/main/java/com/apidemo/script/entity/CaseFieldValueEntity.java` | `case_field_value` |

当前未发现以下设计基线实体的 Java Entity：`TreeCache`、`RawImportFile`、`StepRequestConfig`、`StepPayloadContent`、`StepResponseSample`、`ExtractorConfig`、`AssertionConfig`、`TestCase`、`ExecutionPlan`、`ExecutionPlanCase`、`ExecutionPlanInstance`、`ExecutionTask`、`FlowExecutionRecord`、`StepExecutionSnapshot`、`Environment`、`EnvironmentConfig`、`EnvironmentVariable`。

## 4. 当前 Mapper

### 4.1 Java Mapper Interface

| Mapper | 路径 |
|---|---|
| `ScriptMapper` | `new-script-service/src/main/java/com/apidemo/script/mapper/ScriptMapper.java` |
| `ScriptVersionMapper` | `new-script-service/src/main/java/com/apidemo/script/mapper/ScriptVersionMapper.java` |
| `StepDefinitionMapper` | `new-script-service/src/main/java/com/apidemo/script/mapper/StepDefinitionMapper.java` |
| `FieldConfigMapper` | `new-script-service/src/main/java/com/apidemo/script/mapper/FieldConfigMapper.java` |
| `ScriptFieldDefaultMapper` | `new-script-service/src/main/java/com/apidemo/script/mapper/ScriptFieldDefaultMapper.java` |
| `CaseDataSetMapper` | `new-script-service/src/main/java/com/apidemo/script/mapper/CaseDataSetMapper.java` |
| `CaseFieldValueMapper` | `new-script-service/src/main/java/com/apidemo/script/mapper/CaseFieldValueMapper.java` |

### 4.2 MyBatis XML Mapper

| XML | 路径 |
|---|---|
| `ScriptMapper.xml` | `new-script-service/src/main/resources/mapper/ScriptMapper.xml` |
| `ScriptVersionMapper.xml` | `new-script-service/src/main/resources/mapper/ScriptVersionMapper.xml` |
| `StepDefinitionMapper.xml` | `new-script-service/src/main/resources/mapper/StepDefinitionMapper.xml` |
| `FieldConfigMapper.xml` | `new-script-service/src/main/resources/mapper/FieldConfigMapper.xml` |
| `ScriptFieldDefaultMapper.xml` | `new-script-service/src/main/resources/mapper/ScriptFieldDefaultMapper.xml` |
| `CaseDataSetMapper.xml` | `new-script-service/src/main/resources/mapper/CaseDataSetMapper.xml` |
| `CaseFieldValueMapper.xml` | `new-script-service/src/main/resources/mapper/CaseFieldValueMapper.xml` |

## 5. 当前 Service

### 5.1 Service Interface

| Service | 路径 |
|---|---|
| `ScriptService` | `new-script-service/src/main/java/com/apidemo/script/service/ScriptService.java` |
| `ScriptVersionService` | `new-script-service/src/main/java/com/apidemo/script/service/ScriptVersionService.java` |
| `StepDefinitionService` | `new-script-service/src/main/java/com/apidemo/script/service/StepDefinitionService.java` |
| `FieldConfigService` | `new-script-service/src/main/java/com/apidemo/script/service/FieldConfigService.java` |
| `CaseDataSetService` | `new-script-service/src/main/java/com/apidemo/script/service/CaseDataSetService.java` |
| `ImportService` | `new-script-service/src/main/java/com/apidemo/script/service/ImportService.java` |

### 5.2 Service Implementation

| Service Impl | 路径 |
|---|---|
| `ScriptServiceImpl` | `new-script-service/src/main/java/com/apidemo/script/service/impl/ScriptServiceImpl.java` |
| `ScriptVersionServiceImpl` | `new-script-service/src/main/java/com/apidemo/script/service/impl/ScriptVersionServiceImpl.java` |
| `StepDefinitionServiceImpl` | `new-script-service/src/main/java/com/apidemo/script/service/impl/StepDefinitionServiceImpl.java` |
| `FieldConfigServiceImpl` | `new-script-service/src/main/java/com/apidemo/script/service/impl/FieldConfigServiceImpl.java` |
| `CaseDataSetServiceImpl` | `new-script-service/src/main/java/com/apidemo/script/service/impl/CaseDataSetServiceImpl.java` |
| `ImportServiceImpl` | `new-script-service/src/main/java/com/apidemo/script/service/impl/ImportServiceImpl.java` |

当前 `new-executor-service` 未发现业务 Service。

## 6. 当前 Controller

| Controller | 路径 | 当前路由范围 |
|---|---|---|
| `HealthController` | `new-script-service/src/main/java/com/apidemo/script/controller/HealthController.java` | `GET /health` |
| `ScriptController` | `new-script-service/src/main/java/com/apidemo/script/controller/ScriptController.java` | `/api/scripts` |
| `ScriptVersionController` | `new-script-service/src/main/java/com/apidemo/script/controller/ScriptVersionController.java` | `/api/scripts/{scriptId}/versions` |
| `StepDefinitionController` | `new-script-service/src/main/java/com/apidemo/script/controller/StepDefinitionController.java` | `/api/scripts/{scriptId}/versions/{versionId}/steps` |
| `FieldConfigController` | `new-script-service/src/main/java/com/apidemo/script/controller/FieldConfigController.java` | `/api/scripts/{scriptId}/versions/{versionId}/fields` |
| `CaseDataSetController` | `new-script-service/src/main/java/com/apidemo/script/controller/CaseDataSetController.java` | `/api/scripts/{scriptId}/versions/{versionId}/case-data-sets` |
| `ImportController` | `new-script-service/src/main/java/com/apidemo/script/controller/ImportController.java` | `POST /api/imports/preview`、`POST /api/scripts/{scriptId}/versions/{versionId}/imports/confirm` |
| `HealthController` | `new-executor-service/src/main/java/com/apidemo/executor/controller/HealthController.java` | `GET /health` |

## 7. 当前导入解析相关类

| 类 | 路径 | 说明 |
|---|---|---|
| `ImportFileParser` | `new-script-service/src/main/java/com/apidemo/script/parser/ImportFileParser.java` | 导入解析接口 |
| `HarImportParser` | `new-script-service/src/main/java/com/apidemo/script/parser/HarImportParser.java` | HAR JSON 解析 |
| `PostmanImportParser` | `new-script-service/src/main/java/com/apidemo/script/parser/PostmanImportParser.java` | Postman Collection JSON 解析 |
| `ImportFieldParserSupport` | `new-script-service/src/main/java/com/apidemo/script/parser/ImportFieldParserSupport.java` | 导入字段解析辅助 |

## 8. 当前 SQL 迁移脚本

### 8.1 MySQL 脚本

| 脚本 | 路径 | 当前内容 |
|---|---|---|
| `V1__init_schema.sql` | `db/mysql/V1__init_schema.sql` | 建表脚本 |
| `V2__seed_demo_data.sql` | `db/mysql/V2__seed_demo_data.sql` | 本地演示数据 |

`V1__init_schema.sql` 当前建表清单：

- `script`
- `script_version`
- `step_definition`
- `field_config`
- `script_field_default`
- `case_data_set`
- `case_field_value`
- `execution_plan`
- `execution_plan_case`
- `execution_plan_instance`
- `execution_task`
- `flow_execution_record`
- `step_execution_snapshot`

`V2__seed_demo_data.sql` 当前写入演示数据：

- `script`
- `script_version`
- `step_definition`
- `field_config`
- `script_field_default`

### 8.2 测试 SQL

| 脚本 | 路径 | 当前内容 |
|---|---|---|
| `schema-h2.sql` | `new-script-service/src/test/resources/db/schema-h2.sql` | H2 测试建表脚本，覆盖 `script`、`script_version`、`step_definition`、`field_config`、`script_field_default`、`case_data_set`、`case_field_value`、`execution_plan`、`execution_plan_case`、`execution_plan_instance`、`execution_task`、`flow_execution_record`、`step_execution_snapshot` |

## 9. 当前测试文件

| 测试类 | 路径 |
|---|---|
| `CommonCoreTests` | `common-core/src/test/java/com/apidemo/common/CommonCoreTests.java` |
| `NewExecutorServiceApplicationTests` | `new-executor-service/src/test/java/com/apidemo/executor/NewExecutorServiceApplicationTests.java` |
| `NewScriptServiceApplicationTests` | `new-script-service/src/test/java/com/apidemo/script/NewScriptServiceApplicationTests.java` |
| `ScriptCrudServiceTests` | `new-script-service/src/test/java/com/apidemo/script/service/ScriptCrudServiceTests.java` |
| `FieldCaseDataServiceTests` | `new-script-service/src/test/java/com/apidemo/script/service/FieldCaseDataServiceTests.java` |
| `ImportParserTests` | `new-script-service/src/test/java/com/apidemo/script/service/ImportParserTests.java` |
| `ImportConfirmServiceTests` | `new-script-service/src/test/java/com/apidemo/script/service/ImportConfirmServiceTests.java` |
| `ScriptCrudControllerTests` | `new-script-service/src/test/java/com/apidemo/script/controller/ScriptCrudControllerTests.java` |
| `FieldCaseDataControllerTests` | `new-script-service/src/test/java/com/apidemo/script/controller/FieldCaseDataControllerTests.java` |
| `ImportControllerTests` | `new-script-service/src/test/java/com/apidemo/script/controller/ImportControllerTests.java` |

测试资源：

- `new-script-service/src/test/resources/application-test.yml`
- `new-script-service/src/test/resources/sample.har`
- `new-script-service/src/test/resources/sample-postman-collection.json`
- `new-script-service/src/test/resources/db/schema-h2.sql`

## 10. 与最新设计基线的差异提示

以下为盘点发现的实现现状差异，只记录，不在本轮修正：

- 当前 `db/mysql/V1__init_schema.sql` 的部分字段名与 `docs/DATA_MODEL.md` 的最新设计字段不同，例如 `script.name` / `current_version_id`、`script_version.version_status` / `published_at`、`step_definition.name` / `sort_no` 等。
- 当前代码使用 `CaseDataSet` / `case_data_set` 作为用例数据模型，最新设计基线中核心用例模型命名为 `TestCase` / `test_case`，并要求用例绑定 `scriptVersionId`。
- 当前 SQL 已存在 `execution_plan`、`execution_plan_case`、`execution_plan_instance`、`execution_task`、`flow_execution_record`、`step_execution_snapshot` 表，但未发现对应 Java Entity、Mapper、Service、Controller。
- 当前导入确认会写入已有步骤、字段与默认值模型；未发现 `RawImportFile`、导入日志表或对应 Entity。
- 当前 `message-codec`、`variable-engine`、`extractor-engine`、`assertion-engine` 仍为空模块骨架，未发现主源码实现。
- 当前 `new-executor-service` 只有启动类与健康检查接口，未发现执行任务接收、变量解析、请求发送、响应解析、提取、断言等业务实现。

## 11. 工作区状态提示

盘点开始时检测到仓库已有未提交和未跟踪文件，本轮仅新增本文件，不回退、不覆盖既有改动。
