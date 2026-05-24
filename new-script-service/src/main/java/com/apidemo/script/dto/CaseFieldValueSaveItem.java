package com.apidemo.script.dto;

import javax.validation.constraints.NotBlank;

/**
 * One field override value item in a batch save request.
 */
public class CaseFieldValueSaveItem {

    /** Field configuration id, required. */
    @NotBlank(message = "字段配置ID不能为空")
    private String fieldConfigId;

    /** Override value, may be an empty string. */
    private String value;

    /** Value source, defaults to MANUAL. */
    private String valueSource;

    public String getFieldConfigId() {
        return fieldConfigId;
    }

    public void setFieldConfigId(String fieldConfigId) {
        this.fieldConfigId = fieldConfigId;
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
}
