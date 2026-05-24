package com.apidemo.script.entity;

import com.apidemo.common.model.BaseEntity;

/**
 * Field configuration entity mapped to the field_config table.
 */
public class FieldConfigEntity extends BaseEntity {

    /** Owning script id. */
    private String scriptId;

    /** Owning script version id. */
    private String scriptVersionId;

    /** Owning step id. */
    private String stepId;

    /** Field scope, for example REQUEST_BODY or RESPONSE_BODY. */
    private String fieldScope;

    /** Field path within the request or response structure. */
    private String fieldPath;

    /** Field key extracted from the path or payload. */
    private String fieldKey;

    /** Stable key used to match the same logical field across edits. */
    private String stableFieldKey;

    /** User-facing field name. */
    private String fieldName;

    /** Field data type such as STRING, NUMBER, OBJECT, or ARRAY. */
    private String dataType;

    /** Required flag, 1 means required. */
    private Integer required;

    /** Array flag, 1 means the field is an array. */
    private Integer arrayFlag;

    /** Sensitive flag, 1 means the field may need masking later. */
    private Integer sensitive;

    /** Field description. */
    private String description;

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
}
