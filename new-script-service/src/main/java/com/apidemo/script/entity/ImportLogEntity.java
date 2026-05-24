package com.apidemo.script.entity;

import java.time.LocalDateTime;

/**
 * Import log entity mapped to import_log.
 */
public class ImportLogEntity {

    /** Import log row id. */
    private String id;

    /** Raw import file id. */
    private String rawImportFileId;

    /** Related script id, nullable before confirmation. */
    private String scriptId;

    /** Related script version id, nullable before confirmation. */
    private String scriptVersionId;

    /** Import type, HAR or POSTMAN. */
    private String importType;

    /** Import stage such as PREVIEW or CONFIRM. */
    private String stage;

    /** Stage result status such as SUCCESS, WARNING, or FAILED. */
    private String status;

    /** Stage summary message. */
    private String message;

    /** Detailed stage result JSON. */
    private String detailJson;

    /** Warning list JSON. */
    private String warningJson;

    /** Row created time. */
    private LocalDateTime createdTime;

    /** Creator id. */
    private String createdBy;

    /** Logical delete flag. */
    private Integer deleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRawImportFileId() {
        return rawImportFileId;
    }

    public void setRawImportFileId(String rawImportFileId) {
        this.rawImportFileId = rawImportFileId;
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

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetailJson() {
        return detailJson;
    }

    public void setDetailJson(String detailJson) {
        this.detailJson = detailJson;
    }

    public String getWarningJson() {
        return warningJson;
    }

    public void setWarningJson(String warningJson) {
        this.warningJson = warningJson;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
