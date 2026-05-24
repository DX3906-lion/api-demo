package com.apidemo.script.dto;

/**
 * Request body for saving a script field default value.
 */
public class ScriptFieldDefaultRequest {

    /** Default value for the field, may be an empty string. */
    private String defaultValue;

    /** Source of the default value, defaults to MANUAL. */
    private String valueSource;

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
