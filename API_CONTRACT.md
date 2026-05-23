# API_CONTRACT.md

## 1. 通用约定

## 1.1 技术约定

- 使用 Spring Boot 2.7.18。
- 使用 MySQL 8.0。
- API 使用 REST 风格。
- 请求和响应使用 JSON。
- 所有接口返回统一响应结构。
- 所有 interface 方法必须添加 JavaDoc 注释。
- DTO 字段必须添加注释。
- Controller 不写复杂业务逻辑。

## 1.2 统一响应结构

```json
{
  "success": true,
  "code": "0",
  "message": "success",
  "data": {}
}
```

## 2. 脚本服务接口

### 2.1 脚本管理

```http
POST /api/scripts
GET  /api/scripts
GET  /api/scripts/{scriptId}
PUT  /api/scripts/{scriptId}
DELETE /api/scripts/{scriptId}
```

### 2.2 导入

```http
POST /api/scripts/import/preview
POST /api/scripts/import/confirm
GET  /api/scripts/import/{importFileId}/logs
```

说明：

- 支持 HAR。
- 支持 Postman Collection。
- `preview` 只解析和过滤预览，不落正式脚本数据。
- `confirm` 确认后生成 `Script + DRAFT ScriptVersion`。

### 2.3 脚本版本

```http
GET  /api/scripts/{scriptId}/versions
GET  /api/script-versions/{versionId}
POST /api/script-versions/{versionId}/publish
POST /api/script-versions/{versionId}/copy-as-draft
POST /api/script-versions/{versionId}/disable
POST /api/script-versions/{versionId}/archive
GET  /api/script-versions/{sourceVersionId}/diff/{targetVersionId}
```

### 2.4 步骤管理

```http
GET    /api/script-versions/{versionId}/steps
POST   /api/script-versions/{versionId}/steps
PUT    /api/steps/{stepId}
DELETE /api/steps/{stepId}
POST   /api/steps/{stepId}/copy
PUT    /api/script-versions/{versionId}/steps/reorder
```

### 2.5 请求配置

```http
GET /api/steps/{stepId}/request-config
PUT /api/steps/{stepId}/request-config
GET /api/steps/{stepId}/payload
PUT /api/steps/{stepId}/payload
POST /api/steps/{stepId}/raw-body/check
```

### 2.6 字段配置

```http
GET /api/steps/{stepId}/fields
PUT /api/steps/{stepId}/fields
GET /api/steps/{stepId}/tree
POST /api/steps/{stepId}/tree/rebuild
PUT /api/fields/{fieldId}/default-value
PUT /api/fields/{fieldId}/remark
```

### 2.7 响应样例

```http
GET  /api/steps/{stepId}/response-samples
POST /api/steps/{stepId}/response-samples
POST /api/executions/{executionId}/steps/{stepId}/save-response-sample
```

### 2.8 变量、提取器、断言

```http
GET  /api/script-versions/{versionId}/variables
POST /api/script-versions/{versionId}/variables
PUT  /api/variables/{variableId}
DELETE /api/variables/{variableId}

GET  /api/steps/{stepId}/extractors
POST /api/steps/{stepId}/extractors
PUT  /api/extractors/{extractorId}
DELETE /api/extractors/{extractorId}

GET  /api/steps/{stepId}/assertions
POST /api/steps/{stepId}/assertions
PUT  /api/assertions/{assertionId}
DELETE /api/assertions/{assertionId}
```

### 2.9 用例管理

```http
POST /api/script-versions/{versionId}/cases
GET  /api/cases
GET  /api/cases/{caseId}
PUT  /api/cases/{caseId}
DELETE /api/cases/{caseId}
POST /api/cases/{caseId}/copy
GET  /api/cases/{caseId}/field-values
PUT  /api/cases/{caseId}/field-values
POST /api/cases/{caseId}/restore-default
```

### 2.10 用例升级

```http
POST /api/cases/{caseId}/upgrade-preview
POST /api/cases/{caseId}/upgrade
POST /api/cases/batch-upgrade-preview
POST /api/cases/batch-upgrade
GET  /api/case-upgrade-tasks/{taskId}
```

### 2.11 环境配置

```http
GET  /api/environments
POST /api/environments
PUT  /api/environments/{envId}
DELETE /api/environments/{envId}

GET  /api/environments/{envId}/variables
POST /api/environments/{envId}/variables
PUT  /api/environment-variables/{variableId}
DELETE /api/environment-variables/{variableId}
```

### 2.12 执行计划

```http
GET  /api/execution-plans
POST /api/execution-plans
GET  /api/execution-plans/{planId}
PUT  /api/execution-plans/{planId}
DELETE /api/execution-plans/{planId}

POST /api/execution-plans/{planId}/cases
PUT  /api/execution-plans/{planId}/cases/reorder
DELETE /api/execution-plans/{planId}/cases/{caseId}

POST /api/execution-plans/{planId}/run
GET  /api/execution-plans/{planId}/instances
GET  /api/execution-plan-instances/{instanceId}
```

### 2.13 调试

```http
POST /api/debug/steps/{stepId}
POST /api/debug/script-versions/{versionId}
GET  /api/debug/executions/{executionId}
```

说明：

- 脚本服务生成调试快照。
- 脚本服务调用执行机服务。
- 执行机返回 `executionId`。
- 脚本服务根据 `executionId` 展示结果。

## 3. 执行机服务接口

### 3.1 调试执行

```http
POST /executor/debug/step
POST /executor/debug/flow
```

### 3.2 正式任务执行

```http
POST /executor/tasks
GET  /executor/tasks/{taskId}
POST /executor/tasks/{taskId}/cancel
```

### 3.3 执行记录查询

```http
GET /executor/records/{executionId}
GET /executor/records/{executionId}/steps
GET /executor/records/{executionId}/steps/{stepSnapshotId}
```

## 4. 执行机任务请求核心字段

```json
{
  "taskId": "task_001",
  "executionType": "PLAN",
  "caseId": "case_001",
  "scriptVersionId": "version_001",
  "envId": "sit",
  "executionPackage": {}
}
```

## 5. Codex 接口实现约束

- 每个 API 对应的 service interface 方法必须添加 JavaDoc。
- 每个 Request / Response DTO 字段必须添加注释。
- Controller 只负责协议层，不写核心业务。
- 执行期 API 不得修改脚本配置表。
- 调试 API 也必须经过执行机服务。
