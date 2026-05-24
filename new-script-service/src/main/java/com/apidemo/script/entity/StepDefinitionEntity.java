package com.apidemo.script.entity;

import com.apidemo.common.model.BaseEntity;

/**
 * Step definition entity mapped to the step_definition table.
 */
public class StepDefinitionEntity extends BaseEntity {

    /** Owning script id, denormalized for direct lookup. */
    private String scriptId;

    /** Owning script version id. */
    private String scriptVersionId;

    /** Parent step id for nested flow steps. */
    private String parentStepId;

    /** Step type, for example HTTP in the current minimal CRUD stage. */
    private String stepType;

    /** Step display name. */
    private String name;

    /** Sort order inside the version. */
    private Integer sortNo;

    /** Optional HTTP request method. */
    private String requestMethod;

    /** Optional request URL template. */
    private String requestUrl;

    /** Request configuration JSON text. */
    private String requestConfig;

    /** Assertion configuration JSON text. */
    private String assertionConfig;

    /** Extractor configuration JSON text. */
    private String extractorConfig;

    /** Enabled flag, 1 means enabled and 0 means disabled. */
    private Integer enabled;

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

    public String getParentStepId() {
        return parentStepId;
    }

    public void setParentStepId(String parentStepId) {
        this.parentStepId = parentStepId;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestConfig() {
        return requestConfig;
    }

    public void setRequestConfig(String requestConfig) {
        this.requestConfig = requestConfig;
    }

    public String getAssertionConfig() {
        return assertionConfig;
    }

    public void setAssertionConfig(String assertionConfig) {
        this.assertionConfig = assertionConfig;
    }

    public String getExtractorConfig() {
        return extractorConfig;
    }

    public void setExtractorConfig(String extractorConfig) {
        this.extractorConfig = extractorConfig;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }
}
