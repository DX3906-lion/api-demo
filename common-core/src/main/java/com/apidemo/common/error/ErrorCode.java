package com.apidemo.common.error;

public enum ErrorCode {

    SUCCESS("000000", "success"),
    PARAM_INVALID("400001", "参数校验失败"),
    RESOURCE_NOT_FOUND("404001", "资源不存在"),
    BUSINESS_CONFLICT("409001", "业务冲突"),
    VERSION_STATUS_INVALID("409002", "版本状态冲突"),
    IMPORT_PARSE_FAILED("422001", "导入解析失败"),
    EXECUTION_FAILED("600001", "执行失败"),
    DOWNSTREAM_TIMEOUT("604001", "下游超时"),
    SYSTEM_ERROR("500001", "系统异常");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
