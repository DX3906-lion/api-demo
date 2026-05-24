package com.apidemo.script.dto;

import java.time.LocalDateTime;

/**
 * Response body for a step definition.
 */
public class StepDefinitionResponse {

    /** Step id. */
    private String id;

    /** Owning script id. */
    private String scriptId;

    /** Owning script version id. */
    private String scriptVersionId;

    /** Parent step id. */
    private String parentStepId;

    /** Step type. */
    private String stepType;

    /** Step name. */
    private String name;

    /** Step sort order. */
    private Integer sortNo;

    /** Optional HTTP request method. */
    private String requestMethod;

    /** Optional request URL. */
    private String requestUrl;

    /** Request configuration JSON text. */
    private String requestConfig;

    /** Assertion configuration JSON text. */
    private String assertionConfig;

    /** Extractor configuration JSON text. */
    private String extractorConfig;

    /** Enabled flag. */
    private Integer enabled;

    /** Step created time. */
    private LocalDateTime createdTime;

    /** Step updated time. */
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
