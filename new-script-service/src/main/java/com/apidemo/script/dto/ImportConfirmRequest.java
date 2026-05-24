package com.apidemo.script.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * Request body for confirming an import into an existing DRAFT version.
 */
public class ImportConfirmRequest {

    /** Raw import file id returned by preview and used for import traceability. */
    @NotBlank(message = "importFileId must not be blank")
    private String importFileId;

    /** Import type, HAR or POSTMAN. */
    private String importType;

    /** Step list to persist in the same order. */
    @Valid
    @NotEmpty(message = "steps must not be empty")
    private List<ImportConfirmStepRequest> steps;

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

    public List<ImportConfirmStepRequest> getSteps() {
        return steps;
    }

    public void setSteps(List<ImportConfirmStepRequest> steps) {
        this.steps = steps;
    }
}
