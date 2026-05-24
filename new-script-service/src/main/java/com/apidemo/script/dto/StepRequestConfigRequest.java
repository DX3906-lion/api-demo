package com.apidemo.script.dto;

import javax.validation.constraints.NotBlank;

/**
 * Request body for saving a step request configuration.
 */
public class StepRequestConfigRequest {

    /** HTTP request method. */
    @NotBlank(message = "请求方法不能为空")
    private String method;

    /** URL template before execution-time variable resolution. */
    @NotBlank(message = "请求地址不能为空")
    private String urlTemplate;

    /** Protocol type, defaults to HTTP when absent. */
    private String protocolType;

    /** Content-Type value. */
    private String contentType;

    /** Body format, for example JSON or FORM. */
    private String bodyFormat;

    /** Request character set. */
    private String charset;

    /** Request timeout in milliseconds. */
    private Integer timeoutMs;

    /** Whether redirects are followed, stored as 1 or 0. */
    private String followRedirect;

    /** Extension configuration JSON. */
    private String configJson;

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
}
