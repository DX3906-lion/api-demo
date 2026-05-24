package com.apidemo.script.entity;

import com.apidemo.common.model.BaseEntity;
import java.time.LocalDateTime;

/**
 * Script version entity mapped to the script_version table.
 */
public class ScriptVersionEntity extends BaseEntity {

    /** Owning script id. */
    private String scriptId;

    /** Monotonically increasing version number under one script. */
    private Integer versionNo;

    /** Version lifecycle status: DRAFT, PUBLISHED, DISABLED, or ARCHIVED. */
    private String versionStatus;

    /** Version description. */
    private String description;

    /** Publish time, set when a DRAFT version is published. */
    private LocalDateTime publishedAt;

    /** Raw import file id associated when this version is created or updated by import confirmation. */
    private String rawImportFileId;

    public String getScriptId() {
        return scriptId;
    }

    public void setScriptId(String scriptId) {
        this.scriptId = scriptId;
    }

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    public String getVersionStatus() {
        return versionStatus;
    }

    public void setVersionStatus(String versionStatus) {
        this.versionStatus = versionStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getRawImportFileId() {
        return rawImportFileId;
    }

    public void setRawImportFileId(String rawImportFileId) {
        this.rawImportFileId = rawImportFileId;
    }
}
