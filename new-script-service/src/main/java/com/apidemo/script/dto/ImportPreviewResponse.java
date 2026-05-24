package com.apidemo.script.dto;

import java.util.List;

/**
 * Import preview response containing normalized steps and warnings.
 */
public class ImportPreviewResponse {

    /** Resolved import type, HAR or POSTMAN. */
    private String importType;

    /** Parsed step preview list. */
    private List<ImportPreviewStepResponse> steps;

    /** Non-blocking parser warnings. */
    private List<String> warnings;

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
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
