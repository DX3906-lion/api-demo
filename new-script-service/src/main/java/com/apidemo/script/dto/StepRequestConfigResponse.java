package com.apidemo.script.dto;

import java.time.LocalDateTime;

/**
 * Response body for a step request configuration.
 */
public class StepRequestConfigResponse {

    /** Request config row id. */
    private String id;

    /** Owning step id. */
    private String stepId;

    /** HTTP request method. */
    private String method;

    /** URL template before execution-time variable resolution. */
    private String urlTemplate;

    /** Protocol type. */
    private String protocolType;

    /** Content-Type value. */
    private String contentType;

    /** Body format. */
    private String bodyFormat;

    /** Request character set. */
    private String charset;

    /** Request timeout in milliseconds. */
    private Integer timeoutMs;

    /** Whether redirects are followed, stored as 1 or 0. */
    private String followRedirect;

    /** Extension configuration JSON. */
    private String configJson;

    /** Row created time. */
    private LocalDateTime createdTime;

    /** Row updated time. */
    private LocalDateTime updatedTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public void setUrlTemplate(String urlTemplate) {
        this.urlTemplate = urlTemplate;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBodyFormat() {
        return bodyFormat;
    }

    public void setBodyFormat(String bodyFormat) {
        this.bodyFormat = bodyFormat;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Integer getTimeoutMs() {
        return timeoutMs;
    }

    public void setTimeoutMs(Integer timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    public String getFollowRedirect() {
        return followRedirect;
    }

    public void setFollowRedirect(String followRedirect) {
        this.followRedirect = followRedirect;
    }

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson;
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
