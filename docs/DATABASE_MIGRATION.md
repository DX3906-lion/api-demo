# DATABASE_MIGRATION.md

## 1. 当前 SQL 文件路径

当前阶段统一放在：

- `db/mysql/V1__init_schema.sql`
- `db/mysql/V2__seed_demo_data.sql`

## 2. 文件命名规则

- 版本脚本：`V{序号}__{description}.sql`
- 当前采用顺序版本：`V1`、`V2`。
- 后续新增按版本递增：`V3`、`V4`...

## 3. 是否使用 Flyway / Liquibase

- 当前阶段 **不引入 Flyway / Liquibase 依赖**。
- 先采用人工 SQL 脚本方式，保证可读、可审查、可手工执行。

## 4. 初始化执行方式

按顺序执行：

1. `V1__init_schema.sql`（建表）
2. `V2__seed_demo_data.sql`（演示数据）

示例：

```bash
mysql -h127.0.0.1 -P3306 -uroot -proot api_demo < db/mysql/V1__init_schema.sql
mysql -h127.0.0.1 -P3306 -uroot -proot api_demo < db/mysql/V2__seed_demo_data.sql
```

## 5. 数据库字段类型约定

- 主键：`id varchar(64)`。
- 时间字段：`created_time datetime not null`、`updated_time datetime not null`。
- 操作人字段：`created_by varchar(64)`、`updated_by varchar(64)`。
- 逻辑删除：`deleted tinyint not null default 0`。
- 枚举语义字段：`varchar`（不使用 MySQL enum）。
- JSON / 报文 / 快照字段：`longtext`（不强依赖 MySQL json）。


- Java 字段命名建议使用驼峰：`createdTime`、`updatedTime`、`createdBy`、`updatedBy`。
- 数据库字段使用下划线：`created_time`、`updated_time`、`created_by`、`updated_by`。
- 后续 Mapper / SQL 需做驼峰与下划线映射。

## 6. 索引命名规则

- 普通索引：`idx_{table}_{column}`。
- 唯一索引：`uk_{table}_{biz_key}`。
- 索引命名保持语义清晰，便于排查执行计划。

## 7. 不使用外键的原因

- 跨服务、跨生命周期数据需要更灵活的演进与迁移策略。
- 避免强外键导致上线变更和历史数据修复成本过高。
- 通过逻辑关联字段 + 索引保障查询性能与关联可追踪性。

## 8. longtext 存 JSON / 报文 / 快照的原因

- 避免过度依赖特定数据库 JSON 能力，降低迁移成本。
- 快照、原始报文、扩展配置结构变化频繁，`longtext` 更稳定。
- 在第一阶段优先保障可落地与可维护，后续再评估结构化优化。
