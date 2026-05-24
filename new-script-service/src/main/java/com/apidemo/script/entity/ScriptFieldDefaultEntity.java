package com.apidemo.script.entity;

import com.apidemo.common.model.BaseEntity;

/**
 * Script field default entity mapped to the script_field_default table.
 */
public class ScriptFieldDefaultEntity extends BaseEntity {

    /** Owning script id. */
    private String scriptId;

    /** Owning script version id. */
    private String scriptVersionId;

    /** Field configuration id. */
    private String fieldConfigId;

    /** Default value for the field, may be an empty string. */
    private String defaultValue;

    /** Source of the default value, for example MANUAL. */
    private String valueSource;

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
}
