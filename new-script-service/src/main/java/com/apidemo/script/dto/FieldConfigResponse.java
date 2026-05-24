package com.apidemo.script.dto;

import java.time.LocalDateTime;

/**
 * Response body for field configuration details.
 */
public class FieldConfigResponse {

    /** Field configuration id. */
    private String id;

    /** Owning script id. */
    private String scriptId;

    /** Owning script version id. */
    private String scriptVersionId;

    /** Owning step id. */
    private String stepId;

    /** Field scope. */
    private String fieldScope;

    /** Field path. */
    private String fieldPath;

    /** Field key. */
    private String fieldKey;

    /** Stable field key. */
    private String stableFieldKey;

    /** User-facing field name. */
    private String fieldName;

    /** Field data type. */
    private String dataType;

    /** Required flag. */
    private Integer required;

    /** Array flag. */
    private Integer arrayFlag;

    /** Sensitive flag. */
    private Integer sensitive;

    /** Field description. */
    private String description;

    /** Created time. */
    private LocalDateTime createdTime;

    /** Updated time. */
    private LocalDateTime updatedTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScriptId() {
        return scriptId;
    }

    public void setScriptId(String scriptId) {
        this.scriptId = scriptId;
    }

    public String getScriptVersionId() {
        return scriptVersionId;
    }

    public void setScriptVersionId(String scriptVersionId) {
        this.scriptVersionId = scriptVersionId;
    }

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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
