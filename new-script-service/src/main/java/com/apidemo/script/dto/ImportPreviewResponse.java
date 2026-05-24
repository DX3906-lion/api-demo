package com.apidemo.script.dto;

import java.util.List;

/**
 * Import preview response containing normalized steps and warnings.
 */
public class ImportPreviewResponse {

    /** Raw import file id saved for traceability, not formal script data. */
    private String importFileId;

    /** Resolved import type, HAR or POSTMAN. */
    private String importType;

    /** Whether preview created formal script data such as Script or StepDefinition. */
    private Boolean formalScriptDataCreated;

    /** Notice explaining the persisted raw file is not executable script configuration. */
    private String importFileNotice;

    /** Parsed step preview list. */
    private List<ImportPreviewStepResponse> steps;

    /** Non-blocking parser warnings. */
    private List<String> warnings;

    public String getImportFileId() {
        return importFileId;
    }

    public void setImportFileId(String importFileId) {
        this.importFileId = importFileId;
    }

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    public Boolean getFormalScriptDataCreated() {
        return formalScriptDataCreated;
    }

    public void setFormalScriptDataCreated(Boolean formalScriptDataCreated) {
        this.formalScriptDataCreated = formalScriptDataCreated;
    }

    public String getImportFileNotice() {
        return importFileNotice;
    }

    public void setImportFileNotice(String importFileNotice) {
        this.importFileNotice = importFileNotice;
    }

    public List<ImportPreviewStepResponse> getSteps() {
        return steps;
    }

    public void setSteps(List<ImportPreviewStepResponse> steps) {
        this.steps = steps;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }
}
