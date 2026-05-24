package com.apidemo.script.dto;

import java.time.LocalDateTime;

/**
 * Response body for a script version.
 */
public class ScriptVersionResponse {

    /** Script version id. */
    private String id;

    /** Owning script id. */
    private String scriptId;

    /** Version number under the script. */
    private Integer versionNo;

    /** Version status. */
    private String versionStatus;

    /** Version description. */
    private String description;

    /** Publish time when the version is published. */
    private LocalDateTime publishedAt;

    /** Version created time. */
    private LocalDateTime createdTime;

    /** Version updated time. */
    private LocalDateTime updatedTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
