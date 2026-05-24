# IMPLEMENTATION_PLAN.md

## 1. 开发目标

基于 Spring Boot 2.7.18 + MySQL 8.0 + Java 8，在单仓库 Maven 多模块工程下实现新脚本服务与新执行机服务的第一阶段主链路：

```text
导入 / 新建脚本
  ↓
DRAFT 脚本版本
  ↓
编排
  ↓
调试
  ↓
发布
  ↓
生成用例
  ↓
执行计划
  ↓
执行机执行
  ↓
执行记录
```

## 2. 开发规范

- 每个 interface 方法必须添加 JavaDoc。
- 每个实体类字段必须添加注释。
- DTO 字段必须添加注释。
- 枚举值必须添加说明。
- 复杂业务代码必须添加必要注释。
- Controller 不写复杂业务。
- 核心业务写在 Service。
- 数据访问写在 Repository / Mapper。
- 公共解析逻辑放入 common 模块。

## 3. 阶段 1：工程基础

### 目标

搭建单仓库 Maven 多模块基础工程结构。

### 任务

- 创建父级 Maven 工程。
- 创建 `new-script-service`。
- 创建 `new-executor-service`。
- 创建 `common-message-codec`。
- 创建 `common-variable-engine`。
- 创建 `common-extractor-engine`。
- 创建 `common-assertion-engine`。
- 配置 Spring Boot 2.7.18。
- 配置 MySQL 8.0 驱动。
- 配置统一响应结构。
- 配置统一异常处理。
- 配置基础日志。
- 创建数据库初始化目录。

### 验收

- 服务可启动。
- 健康检查接口可访问。
- MySQL 连接正常。
- 基础单元测试通过。

## 4. 阶段 2：脚本与版本模型

### 任务

- 实现 `Script`。
- 实现 `ScriptVersion`。
- 实现 `StepDefinition`。
- 实现 `StepRequestConfig`。
- 实现基础 CRUD。
- 实现 DRAFT / PUBLISHED 状态校验。
- 实现发布版本只读校验。

### 验收

- 可新建空白脚本。
- 可生成 DRAFT 版本。
- 可发布脚本版本。
- 已发布版本不可修改。

## 5. 阶段 3：导入能力

### 任务

- 实现 HAR 导入预览。
- 实现 Postman Collection 导入预览。
- 实现导入过滤配置。
- 实现导入确认。
- 保存 `RawImportFile`。
- 生成 `Script + DRAFT ScriptVersion`。
- 生成 `StepDefinition`。
- 生成 `StepRequestConfig`。
- 生成导入日志。

### 验收

- 可导入 HAR。
- 可导入 Postman Collection。
- 可过滤 OPTIONS、静态资源、第三方域名。
- 可手动勾选请求。
- 确认导入后进入脚本编排。

## 6. 阶段 4：字段模型与树缓存

### 任务

- 实现 `FieldConfig`。
- 实现 `ScriptFieldDefault`。
- 实现 `TreeCache`。
- 实现 `StepPayloadContent`。
- 实现 JSON 请求体解析。
- 实现 XML 请求体解析。
- 实现 key-value 解析。
- 实现 Header / Query / Cookie 字段解析。
- 实现字段树构建。
- 实现 TreeCache 重建。

### 验收

- 请求参数、请求头、Cookie、请求体可生成字段结构。
- 字段默认值进入 `ScriptFieldDefault`。
- 前端可获取树形数据。
- TreeCache 可删除重建。

## 7. 阶段 5：Raw Body 编辑与反写

### 任务

- 实现 JSON 语法校验。
- 实现 XML 语法校验。
- 实现 key-value 语法校验。
- 实现 Raw Body 解析为临时字段树。
- 实现字段匹配。
- 实现值变化识别。
- 实现变量识别。
- 实现结构变化检测。
- 实现反写 `ScriptFieldDefault`。

### 验收

- 修改已有字段值可同步树。
- 变量表达式可识别。
- 结构变化会提示，不自动破坏字段定义。
- 语法错误不会更新主数据。

## 8. 阶段 6：用例模型

### 任务

- 实现 `TestCase`。
- 实现 `CaseFieldValue`。
- 实现基于 PUBLISHED 脚本版本生成用例。
- 实现用例字段覆盖。
- 实现恢复默认值。
- 实现用例树缓存。
- 实现用例不自动跟随最新版本。

### 验收

- 用例绑定 `scriptVersionId`。
- 用例默认继承脚本默认值。
- 修改字段后生成 `CaseFieldValue`。
- 恢复默认值可删除覆盖值。

## 9. 阶段 7：变量引擎

### 任务

- 实现 `${env.xxx}`。
- 实现 `${case.xxx}`。
- 实现 `${runtime.xxx}`。
- 实现 `${extract.xxx}`。
- 实现 `${global.xxx}`。
- 实现 `${func.xxx()}`。
- 实现混合表达式解析。
- 实现变量未定义报错。
- 实现变量解析快照。

### 验收

- 变量可在执行期解析。
- 解析前和解析后值可记录。
- 未定义变量会报错。

## 10. 阶段 8：执行机单步调试

### 任务

- 实现执行机调试任务接收。
- 实现 `ExecutionContext`。
- 实现请求组装。
- 实现 HTTP 请求执行。
- 实现响应解析。
- 实现标准 `ExecutionResult` / `StepExecutionResult` 返回。
- 实现脚本服务接收执行结果并落库 `FlowExecutionRecord` / `StepExecutionSnapshot` 的最小闭环。

### 验收

- 单步调试可执行。
- 最终请求和响应可查看。
- 调试记录可查询。
- 执行机不直接写入脚本服务执行记录表。

## 11. 阶段 9：全流程执行、提取和断言

### 任务

- 实现多步骤顺序执行。
- 实现变量提取。
- 实现后续步骤引用提取变量。
- 实现断言执行。
- 完善 `FlowExecutionRecord` 变量快照、最终变量快照和结果摘要。
- 实现执行失败策略。

### 验收

- 登录 token 可提取并用于后续步骤。
- 断言结果可保存。
- 执行快照完整保存。

## 12. 阶段 10：执行计划

### 任务

- 实现 `ExecutionPlan`。
- 实现 `ExecutionPlanCase`。
- 实现 `ExecutionPlanInstance`。
- 实现 `ExecutionTask`。
- 实现手动触发。
- 实现串行执行。
- 实现并行执行。
- 实现任务下发执行机。
- 实现结果汇总。

### 验收

- 可创建执行计划。
- 可将用例加入计划。
- 可串行执行。
- 可并行执行。
- 可查看计划实例结果。

## 13. 阶段 11：版本升级

### 任务

- 实现脚本版本对比。
- 实现字段映射。
- 实现单用例升级预览。
- 实现单用例升级。
- 实现批量升级预览。
- 实现批量升级任务。
- 实现升级日志。

### 验收

- 旧版本用例不会自动升级。
- 用户可手动升级。
- 批量升级前有预检查。
- 冲突可识别。

## 14. 阶段 12：增强能力

### 任务

- SQL 步骤调试增强。
- 响应样例保存。
- 基于响应生成提取器。
- 基于响应生成断言。
- 外部数据集。
- 组件复制 / 引用。
- AI 助手预留接口。

## 15. 不纳入第一阶段

- `CaseEnvFieldValue`
- 用例完整版本管理
- 用例自动跟随最新脚本版本
- 二进制 TCP 原文编辑
- 8583 bitmap / BCD / MAC 字节级编辑
- multipart/form-data 完整 boundary 原文编辑
