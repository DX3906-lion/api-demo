package com.apidemo.script.dto;

/**
 * Field preview parsed from an import file.
 */
public class ImportPreviewFieldResponse {

    /** Field scope such as REQUEST_HEADER, REQUEST_QUERY, or REQUEST_BODY. */
    private String fieldScope;

    /** Stable field path in header/query/body notation. */
    private String fieldPath;

    /** Field key or parameter name. */
    private String fieldKey;

    /** Stable business key used to match the same logical field. */
    private String stableFieldKey;

    /** User-facing field name. */
    private String fieldName;

    /** Inferred data type. */
    private String dataType;

    /** Required flag, 1 means required. */
    private Integer required;

    /** Array flag, 1 means array. */
    private Integer arrayFlag;

    /** Sensitive flag, 1 means the value may need masking later. */
    private Integer sensitive;

    /** Imported default value, null means no default row should be created. */
    private String defaultValue;

    /** Value source, defaults to IMPORT for parsed values. */
    private String valueSource;

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
