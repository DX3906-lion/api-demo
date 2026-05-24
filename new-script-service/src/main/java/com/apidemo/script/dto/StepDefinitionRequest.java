package com.apidemo.script.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Request body for creating or updating a step definition.
 */
public class StepDefinitionRequest {

    /** Optional parent step id. */
    private String parentStepId;

    /** Step type, required for routing later step capabilities. */
    @NotBlank(message = "步骤类型不能为空")
    private String stepType;

    /** Step display name. */
    @NotBlank(message = "步骤名称不能为空")
    private String name;

    /** Step sort order inside the script version. */
    @NotNull(message = "步骤顺序不能为空")
    private Integer sortNo;

    /** Optional HTTP request method. */
    private String requestMethod;

    /** Optional request URL. */
    private String requestUrl;

    /** Optional request configuration JSON text. */
    private String requestConfig;

    /** Optional assertion configuration JSON text. */
    private String assertionConfig;

    /** Optional extractor configuration JSON text. */
    private String extractorConfig;

    /** Optional enabled flag, defaults to 1 when absent. */
    private Integer enabled;

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
