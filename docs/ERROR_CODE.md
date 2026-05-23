# ERROR_CODE.md

## 1. 统一响应码约定

- 成功响应固定使用：`code = "000000"`，`message = "success"`。
- 错误响应使用分层错误码，且不向前端暴露堆栈。

## 2. 统一错误响应结构

```json
{
  "success": false,
  "code": "400001",
  "message": "参数校验失败",
  "data": null,
  "requestId": "req_xxx",
  "timestamp": "2026-05-23T00:00:00Z"
}
```

## 3. 错误码分层

- `000000`：成功
- `400xxx`：参数校验错误
- `404xxx`：资源不存在
- `409xxx`：业务冲突 / 版本状态冲突
- `422xxx`：导入解析 / 数据结构不合法
- `500xxx`：系统异常
- `600xxx`：执行机执行失败
- `604xxx`：下游超时

## 4. 基础错误码（与 common-core 方向一致）

- `000000`：SUCCESS（success）
- `400001`：PARAM_INVALID（参数校验失败）
- `404001`：RESOURCE_NOT_FOUND（资源不存在）
- `409001`：BUSINESS_CONFLICT（业务冲突）
- `409002`：VERSION_STATUS_INVALID（版本状态冲突）
- `422001`：IMPORT_PARSE_FAILED（导入解析失败）
- `600001`：EXECUTION_FAILED（执行失败）
- `604001`：DOWNSTREAM_TIMEOUT（下游超时）
- `500001`：SYSTEM_ERROR（系统异常）

## 5. 使用约束

- 所有异常必须映射为统一错误码。
- 业务错误与系统错误必须区分。
- 调试信息仅记录在日志，不直接返回堆栈。
