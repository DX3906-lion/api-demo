package com.apidemo.script.dto;

import java.time.LocalDateTime;

/**
 * Response body for field default values with field metadata.
 */
public class ScriptFieldDefaultResponse {

    /** Default value row id. */
    private String id;

    /** Owning script id. */
    private String scriptId;

    /** Owning script version id. */
    private String scriptVersionId;

    /** Field configuration id. */
    private String fieldConfigId;

    /** Field step id. */
    private String stepId;

    /** Field scope. */
    private String fieldScope;

    /** Field path. */
    private String fieldPath;

    /** Field name. */
    private String fieldName;

    /** Field data type. */
    private String dataType;

    /** Default value. */
    private String defaultValue;

    /** Value source. */
    private String valueSource;

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

    public String getFieldConfigId() {
        return fieldConfigId;
    }

    public void setFieldConfigId(String fieldConfigId) {
        this.fieldConfigId = fieldConfigId;
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

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getValueSource() {
        return valueSource;
    }

    public void setValueSource(String valueSource) {
        this.valueSource = valueSource;
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
