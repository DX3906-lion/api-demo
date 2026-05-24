# db 目录说明

## 1. 目录结构

- `db/mysql/V1__init_schema.sql`：第一版初始化建表脚本。
- `db/mysql/V2__seed_demo_data.sql`：第一版本地演示数据脚本。

## 2. 执行顺序

1. 先执行 `V1__init_schema.sql`
2. 再执行 `V2__seed_demo_data.sql`

## 3. 本地执行方式

示例（请替换为本机数据库连接信息）：

```bash
mysql -h127.0.0.1 -P3306 -uroot -proot api_demo < db/mysql/V1__init_schema.sql
mysql -h127.0.0.1 -P3306 -uroot -proot api_demo < db/mysql/V2__seed_demo_data.sql
```

## 4. 回滚方式

当前阶段不提供自动回滚脚本。若需回滚，请人工 `DROP TABLE` 后重新执行建表脚本。

## 5. SQL 约定

- 不使用外键，使用逻辑关联字段 + 索引。
- 逻辑删除统一 `deleted tinyint not null default 0`。
- JSON / 报文 / 快照字段统一使用 `longtext`。

## 6. 后续演进

后续可以迁移到 Flyway / Liquibase 管理迁移版本；本轮不引入相关依赖。
