package com.apidemo.script.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * Step request item used when confirming an import.
 */
public class ImportConfirmStepRequest {

    /** Step name to store in step_definition. */
    @NotBlank(message = "name must not be blank")
    private String name;

    /** Step type, defaults to HTTP. */
    private String stepType;

    /** HTTP request method. */
    private String requestMethod;

    /** HTTP request URL. */
    private String requestUrl;

    /** Request configuration JSON. */
    private String requestConfig;

    /** Imported field list for this step. */
    @Valid
    private List<ImportConfirmFieldRequest> fields;

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

    public List<ImportConfirmFieldRequest> getFields() {
        return fields;
    }

    public void setFields(List<ImportConfirmFieldRequest> fields) {
        this.fields = fields;
    }
}
