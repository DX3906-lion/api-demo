package com.apidemo.script.entity;

import com.apidemo.common.model.BaseEntity;

/**
 * Case field value entity mapped to the case_field_value table.
 */
public class CaseFieldValueEntity extends BaseEntity {

    /** Owning case data set id. */
    private String caseDataSetId;

    /** Field configuration id. */
    private String fieldConfigId;

    /** Override value for the field, may be an empty string. */
    private String value;

    /** Source of the override value, for example MANUAL. */
    private String valueSource;

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
