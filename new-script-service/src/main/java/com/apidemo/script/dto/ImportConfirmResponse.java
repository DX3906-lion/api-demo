package com.apidemo.script.dto;

import java.util.List;

/**
 * Response returned after confirming an import into a script version.
 */
public class ImportConfirmResponse {

    /** Raw import file id associated with the confirmed script version. */
    private String importFileId;

    /** Script id that received the imported configuration. */
    private String scriptId;

    /** Script version id that received the imported configuration. */
    private String scriptVersionId;

    /** Number of step_definition rows created. */
    private Integer importedStepCount;

    /** Number of field_config rows created. */
    private Integer importedFieldCount;

    /** Number of script_field_default rows created. */
    private Integer importedDefaultCount;

    /** Created step ids in request order. */
    private List<String> stepIds;

    public String getImportFileId() {
        return importFileId;
    }

    public void setImportFileId(String importFileId) {
        this.importFileId = importFileId;
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

    public Integer getImportedStepCount() {
        return importedStepCount;
    }

    public void setImportedStepCount(Integer importedStepCount) {
        this.importedStepCount = importedStepCount;
    }

    public Integer getImportedFieldCount() {
        return importedFieldCount;
    }

    public void setImportedFieldCount(Integer importedFieldCount) {
        this.importedFieldCount = importedFieldCount;
    }

    public Integer getImportedDefaultCount() {
        return importedDefaultCount;
    }

    public void setImportedDefaultCount(Integer importedDefaultCount) {
        this.importedDefaultCount = importedDefaultCount;
    }

    public List<String> getStepIds() {
        return stepIds;
    }

    public void setStepIds(List<String> stepIds) {
        this.stepIds = stepIds;
    }
}
