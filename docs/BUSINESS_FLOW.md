# BUSINESS_FLOW.md

## 1. 主流程总览

```text
导入 / 新建脚本
  ↓
生成 DRAFT 脚本版本
  ↓
脚本编排
  ↓
单步调试 / 全流程调试
  ↓
发布脚本版本
  ↓
基于发布版本生成用例
  ↓
用例编辑
  ↓
加入执行计划
  ↓
执行机执行
  ↓
查看执行记录
```

## 2. HAR / Postman 导入创建脚本

```text
用户进入脚本列表
  ↓
点击“导入文件创建脚本”
  ↓
选择 HAR 或 Postman Collection
  ↓
上传文件
  ↓
填写脚本名称、系统、模块、标签
  ↓
系统解析文件
  ↓
展示请求列表
  ↓
用户配置过滤规则
  ↓
用户确认导入范围
  ↓
保存 RawImportFile
  ↓
生成 Script
  ↓
生成 DRAFT ScriptVersion
  ↓
生成 StepDefinition
  ↓
生成 StepRequestConfig
  ↓
生成 FieldConfig
  ↓
生成 ScriptFieldDefault
  ↓
生成 TreeCache
  ↓
进入脚本编排页面
```

## 3. 新建空白脚本

```text
用户进入脚本列表
  ↓
点击“新建空白脚本”
  ↓
填写脚本名称、系统、模块
  ↓
生成 Script
  ↓
生成 DRAFT ScriptVersion
  ↓
进入脚本编排页面
  ↓
手工添加步骤
```

## 4. 脚本编排流程

```text
用户打开 DRAFT 脚本版本
  ↓
新增 / 删除 / 复制 / 拖拽步骤
  ↓
编辑请求配置
  ↓
编辑 FieldConfig
  ↓
编辑 ScriptFieldDefault
  ↓
配置变量提取
  ↓
配置断言
  ↓
配置 SQL 步骤
  ↓
保存草稿
```

规则：

- DRAFT 可编辑。
- PUBLISHED 不可直接编辑。
- 编辑 PUBLISHED 时，必须复制生成新的 DRAFT。

## 5. Raw Body 编辑反写树流程

```text
用户编辑 Raw Body
  ↓
失去焦点或点击检查
  ↓
message-codec 做语法校验
  ↓
解析成功
  ↓
生成临时字段树
  ↓
和 FieldConfig 匹配
  ↓
仅值变化时更新 ScriptFieldDefault 或 CaseFieldValue
  ↓
结构变化时提示用户确认
  ↓
重建 TreeCache
```

规则：

- 脚本编排页更新 `ScriptFieldDefault`。
- 用例编辑页更新 `CaseFieldValue`。
- 语法错误时不更新主数据。
- 新增、删除、改名、类型变化属于结构变化，默认不自动应用。

## 6. 编排期调试流程

```text
用户点击单步调试或全流程调试
  ↓
new-script-service 保存当前草稿
  ↓
new-script-service 生成 executionId 与 DebugExecutionPackage
  ↓
new-executor-service 接收调试任务
  ↓
new-executor-service 解析变量
  ↓
new-executor-service 组装最终请求
  ↓
new-executor-service 执行请求或 SQL
  ↓
new-executor-service 解析响应
  ↓
new-executor-service 执行提取和断言
  ↓
new-executor-service 返回标准 ExecutionResult
  ↓
new-script-service 接收 ExecutionResult 并落库 FlowExecutionRecord / StepExecutionSnapshot
  ↓
new-script-service 展示调试结果
```

规则：

- 调试执行也由执行机服务执行。
- `executionId` 由脚本服务生成并贯穿本次调试，执行机不得自行生成新的业务执行记录 ID。
- 执行机只返回标准 `ExecutionResult`，不直接写入 `FlowExecutionRecord` 或 `StepExecutionSnapshot`。
- 脚本服务负责执行记录查询、执行快照落库和调试结果展示。
- 调试响应不会自动覆盖响应样例。
- 用户点击“保存为响应样例”后，脚本服务才写入 `StepResponseSample`。

## 7. 脚本发布流程

```text
用户点击发布
  ↓
系统校验 DRAFT 完整性
  ↓
生成版本对比
  ↓
用户确认发布
  ↓
ScriptVersion 状态改为 PUBLISHED
  ↓
更新 Script.latestPublishedVersionId
  ↓
发布版本只读
```

## 8. 用例生成流程

```text
用户选择 PUBLISHED 脚本版本
  ↓
点击“生成用例”
  ↓
填写用例名称、模块、标签、描述
  ↓
生成 TestCase
  ↓
TestCase 绑定 scriptVersionId
  ↓
默认继承 ScriptFieldDefault
```

规则：

- 创建用例时不复制全部字段值。
- 用户修改字段后才生成 `CaseFieldValue`。

## 9. 用例编辑流程

```text
用户打开用例
  ↓
系统加载 scriptVersionId
  ↓
加载 FieldConfig
  ↓
加载 ScriptFieldDefault
  ↓
加载 CaseFieldValue
  ↓
合并生成展示树
  ↓
用户修改字段
  ↓
新增或更新 CaseFieldValue
  ↓
重建 TreeCache
```

规则：

- 用例值覆盖脚本默认值。
- 恢复默认值时可删除对应 `CaseFieldValue`。
- 用例不自动跟随最新脚本版本。

## 10. 执行计划流程

```text
用户创建执行计划
  ↓
选择用例
  ↓
选择环境
  ↓
配置串行 / 并行
  ↓
手动或定时触发
  ↓
生成 ExecutionPlanInstance
  ↓
生成 ExecutionTask
  ↓
为每个 ExecutionTask 生成 executionId 与执行包
  ↓
下发 new-executor-service
  ↓
执行机执行任务并返回标准 ExecutionResult
  ↓
脚本服务接收 ExecutionResult 并落库 FlowExecutionRecord / StepExecutionSnapshot
  ↓
脚本服务汇总计划结果
```

规则：

- `ExecutionPlanInstance` 表示一次计划触发批次。
- `ExecutionTask` 表示计划批次中的单个用例执行任务。
- `FlowExecutionRecord` 表示一次调试、手工用例或计划任务的执行记录，主键即 `executionId`。
- `StepExecutionSnapshot` 只归属于 `FlowExecutionRecord`，保存步骤级执行事实。
- 执行机不得直接依赖或写入脚本服务执行记录表。

## 11. 执行机执行流程

```text
执行机接收任务
  ↓
加载脚本服务下发的执行包快照
  ↓
创建 ExecutionContext
  ↓
按步骤执行
  ↓
解析变量
  ↓
组装最终请求
  ↓
发送请求 / 执行 SQL
  ↓
解析响应
  ↓
提取变量
  ↓
执行断言
  ↓
生成标准 ExecutionResult
  ↓
返回给 new-script-service
```

规则：

- 执行包必须包含脚本版本、用例覆盖值、环境变量、字段、提取器、断言等执行所需快照。
- 执行机只负责执行期变量解析、请求组装、请求发送、响应解析、变量提取和断言执行。
- 执行机返回的 `ExecutionResult` 必须包含步骤级解析前快照、解析后快照、最终请求、响应、提取结果、断言结果、耗时和错误信息。
- 执行记录保存由 `new-script-service` 统一完成。

## 12. 用例升级脚本版本流程

```text
选择源脚本版本和目标脚本版本
  ↓
生成字段映射
  ↓
预检查 CaseFieldValue 可迁移性
  ↓
展示冲突
  ↓
用户确认
  ↓
迁移 CaseFieldValue
  ↓
更新 TestCase.scriptVersionId
  ↓
重建 TreeCache
  ↓
保存升级日志
```

## 13. 字段值流向

```text
ScriptFieldDefault
  ↓
CaseFieldValue
  ↓
ExecutionContext 变量解析
  ↓
最终请求报文
  ↓
StepExecutionSnapshot
```

执行后的最终值不反写 `ScriptFieldDefault` 或 `CaseFieldValue`。
