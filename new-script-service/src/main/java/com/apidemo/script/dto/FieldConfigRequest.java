package com.apidemo.script.dto;

import javax.validation.constraints.NotBlank;

/**
 * Request body for creating or updating a field configuration.
 */
public class FieldConfigRequest {

    /** Owning step id, required and must belong to the script version. */
    @NotBlank(message = "步骤ID不能为空")
    private String stepId;

    /** Field scope, required. */
    @NotBlank(message = "字段范围不能为空")
    private String fieldScope;

    /** Field path, required. */
    @NotBlank(message = "字段路径不能为空")
    private String fieldPath;

    /** Field key, optional. */
    private String fieldKey;

    /** Stable field key, optional but unique in one script version when present. */
    private String stableFieldKey;

    /** User-facing field name. */
    private String fieldName;

    /** Field data type, required. */
    @NotBlank(message = "字段数据类型不能为空")
    private String dataType;

    /** Required flag, defaults to 0. */
    private Integer required;

    /** Array flag, defaults to 0. */
    private Integer arrayFlag;

    /** Sensitive flag, defaults to 0. */
    private Integer sensitive;

    /** Field description. */
    private String description;

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getFieldScope() {
        return fieldScope;
    }

    public void setFieldScope(String fieldScope) {
        this.fieldScope = fieldScope;
    }

    public String getFieldPath() {
        return fieldPath;
    }

    public void setFieldPath(String fieldPath) {
        this.fieldPath = fieldPath;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getStableFieldKey() {
        return stableFieldKey;
    }

    public void setStableFieldKey(String stableFieldKey) {
        this.stableFieldKey = stableFieldKey;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Integer getRequired() {
        return required;
    }

    public void setRequired(Integer required) {
        this.required = required;
    }

    public Integer getArrayFlag() {
        return arrayFlag;
    }

    public void setArrayFlag(Integer arrayFlag) {
        this.arrayFlag = arrayFlag;
    }

    public Integer getSensitive() {
        return sensitive;
    }

    public void setSensitive(Integer sensitive) {
        this.sensitive = sensitive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
