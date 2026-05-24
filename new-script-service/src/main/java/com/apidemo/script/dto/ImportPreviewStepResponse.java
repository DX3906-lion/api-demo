package com.apidemo.script.dto;

import java.util.List;

/**
 * Step preview parsed from an import file.
 */
public class ImportPreviewStepResponse {

    /** Temporary step id used only by preview clients. */
    private String tempStepId;

    /** Step name shown to the user. */
    private String name;

    /** Step type, currently HTTP for imported API requests. */
    private String stepType;

    /** HTTP request method. */
    private String requestMethod;

    /** HTTP request URL. */
    private String requestUrl;

    /** Normalized request configuration JSON. */
    private String requestConfig;

    /** Parsed request fields. */
    private List<ImportPreviewFieldResponse> fields;

    public String getTempStepId() {
        return tempStepId;
    }

    public void setTempStepId(String tempStepId) {
        this.tempStepId = tempStepId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
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

    public List<ImportPreviewFieldResponse> getFields() {
        return fields;
    }

    public void setFields(List<ImportPreviewFieldResponse> fields) {
        this.fields = fields;
    }
}
