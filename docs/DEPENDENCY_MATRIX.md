# DEPENDENCY_MATRIX.md

## 1. 基础版本矩阵

| 项 | 版本/状态 | 说明 |
|---|---|---|
| Spring Boot | 2.7.18 | 固定 |
| Java | 8 | 固定（本阶段） |
| Maven | 3.8.x（建议 3.8.8） | 固定范围 |
| MySQL | 8.0.x | 固定范围 |

## 2. 数据访问层选型

| 项 | 状态 | 说明 |
|---|---|---|
| MyBatis | 采用 | 数据访问层默认方案 |
| MyBatis-Plus | 暂不采用 | 如后续引入需专项评审 |
| JPA/Hibernate | 暂不采用 | 如后续引入需专项评审 |

## 3. HTTP Client 选型

| 项 | 状态 | 说明 |
|---|---|---|
| OkHttp | 采用 | 执行机 HTTP 客户端默认方案 |
| Apache HttpClient | 候选 | 备选方案，暂不默认 |
| Spring WebClient | 候选 | 备选方案（需评估与 Java 8/SB2.7 适配） |

## 4. 解析与断言相关依赖建议

| 能力 | 依赖建议 | 状态 |
|---|---|---|
| JSONPath | `com.jayway.jsonpath:json-path` | 候选1（推荐） |
| XPath | `javax.xml.xpath`（JDK 内建） | 候选1（推荐） |
| Regex | JDK `java.util.regex` | 建议采用 |

## 5. 依赖管理规则

- 统一在父工程 `dependencyManagement` 锁版本。
- 禁止子模块私自漂移核心依赖版本。
- 禁止随意引入重量级依赖。
- 新增依赖必须在本文件登记：用途、版本、影响范围、替代方案评估。

## 6. 最终决策与候选清单

- 已确定：
  - 数据访问层采用 **MyBatis**。
  - HTTP 客户端采用 **OkHttp**。
- JSONPath 依赖最终候选：
  1. `com.jayway.jsonpath:json-path`（推荐，能力完整、社区成熟）
  2. `io.rest-assured:json-path`（仅作为备选，避免引入测试生态耦合）
- XPath 依赖最终候选：
  1. `javax.xml.xpath`（JDK 内建，推荐优先）
  2. `org.jaxen:jaxen`（复杂 XPath 场景备选）


## 7. Task 01 依赖落地约束

- Task 01 阶段允许在父 POM `dependencyManagement` 锁定 MyBatis、OkHttp、JSONPath 版本。
- Task 01 阶段各模块**不应实际引入未使用依赖**；仅保留最小启动依赖（如 Spring Boot Web / Test）。
- MyBatis、OkHttp、JSONPath 在后续对应任务中再按需引入到具体模块。


## 8. common-core 依赖口径

- `common-core` 当前允许使用 `spring-web`，用于承载 `GlobalExceptionHandler`、`MethodArgumentNotValidException` 等 Web 异常处理基建。
- `common-core` 当前允许使用 `validation-api`，用于处理 `ConstraintViolationException` 等校验异常。
- 当前阶段上述依赖绑定在 `common-core` 可接受；后续如需进一步解耦，可评估拆分 `common-web` 模块（本阶段不拆分）。
- `common-core` 不允许引入：Lombok、Guava、Apache Commons、MyBatis、JPA。


## 9. 数据迁移依赖约束（T03）

- 当前阶段不引入 Flyway / Liquibase 依赖。
- 数据库初始化与演示数据采用人工 SQL 脚本管理。


## 10. T04 数据访问依赖落地

- `new-script-service` 引入 `org.mybatis.spring.boot:mybatis-spring-boot-starter`，用于 `Script`、`ScriptVersion`、`StepDefinition` 的 Mapper/XML 数据访问。
- `new-script-service` 引入 `com.mysql:mysql-connector-j`（runtime），用于连接 MySQL 8.0。
- `new-script-service` 引入 `spring-boot-starter-validation`，用于 Controller 请求参数校验。
- `new-script-service` 测试范围引入 `com.h2database:h2`，仅用于 T04 CRUD 集成测试，不作为生产数据库方案。
- 本轮未引入 MyBatis-Plus、JPA/Hibernate、Flyway、Liquibase、Lombok、Guava、Apache Commons、MapStruct。


## 11. T05 依赖变更说明

- T05 沿用 T04 已落地的 MyBatis + Mapper XML、MySQL 驱动、Validation 与 H2 测试依赖。
- 本轮没有新增 Maven 依赖。
- 本轮未引入 MyBatis-Plus、JPA/Hibernate、Flyway、Liquibase、Lombok、Guava、Apache Commons、MapStruct。
