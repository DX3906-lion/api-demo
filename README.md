# api-demo

自动化测试平台新脚本服务与新执行机服务开发前设计基线。

## 文档目录

- `AGENTS.md`：Codex / AI 编码规则与项目约束。
- `docs/ARCHITECTURE.md`：服务边界、模块划分、调用关系。
- `docs/DATA_MODEL.md`：核心数据模型、字段、状态、约束。
- `docs/BUSINESS_FLOW.md`：导入、编排、发布、用例、执行、升级流程。
- `docs/API_CONTRACT.md`：脚本服务和执行机服务接口契约草案。
- `docs/IMPLEMENTATION_PLAN.md`：分阶段开发计划。
- `docs/DEV_SETUP.md`：本地开发环境与启动测试说明。
- `docs/CONFIGURATION.md`：统一配置项规范。
- `docs/CODEX_TASKS.md`：按轮次执行的最小任务拆分。
- `docs/ERROR_CODE.md`：统一错误码规范。
- `docs/DATABASE_MIGRATION.md`：数据库迁移与脚本规范。
- `docs/DEPENDENCY_MATRIX.md`：依赖版本矩阵与选型约束。

## 技术基线

- Java 8
- Spring Boot 2.7.18
- MySQL 8.0
- 单仓库 Maven 多模块工程

## 核心原则

- 脚本服务负责脚本、版本、字段、用例、执行计划等配置管理。
- 执行机服务负责执行期变量解析、报文组装、请求执行、响应解析、提取、断言。
- 执行快照由脚本服务在接收执行结果后落库，执行机服务不直接依赖脚本服务业务表。
- `FieldConfig` 是字段结构主数据，`TreeCache` 只是展示缓存。
- 脚本发布版本只读，用例固定绑定脚本版本。


## 数据库脚本

- `db/mysql/V1__init_schema.sql`：第一版初始化建表脚本。
- `db/mysql/V2__seed_demo_data.sql`：第一版本地演示数据。
- `db/README.md`：数据库脚本执行顺序与约定说明。
