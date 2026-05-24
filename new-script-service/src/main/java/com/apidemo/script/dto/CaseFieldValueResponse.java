package com.apidemo.script.dto;

import java.time.LocalDateTime;

/**
 * Response body for case field override values with field metadata.
 */
public class CaseFieldValueResponse {

    /** Case field value row id. */
    private String id;

    /** Owning case data set id. */
    private String caseDataSetId;

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

    /** Override value. */
    private String value;

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

    public String getCaseDataSetId() {
        return caseDataSetId;
    }

    public void setCaseDataSetId(String caseDataSetId) {
        this.caseDataSetId = caseDataSetId;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
