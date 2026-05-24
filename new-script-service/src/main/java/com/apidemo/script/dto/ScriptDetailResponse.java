package com.apidemo.script.dto;

import java.time.LocalDateTime;

/**
 * Response body for script details.
 */
public class ScriptDetailResponse {

    /** Script id. */
    private String id;

    /** Script name. */
    private String name;

    /** Script description. */
    private String description;

    /** Script status. */
    private String status;

    /** Current version id recorded on the script row. */
    private String currentVersionId;

    /** Current version detail. */
    private ScriptVersionResponse currentVersion;

    /** Script created time. */
    private LocalDateTime createdTime;

    /** Script updated time. */
    private LocalDateTime updatedTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentVersionId() {
        return currentVersionId;
    }

    public void setCurrentVersionId(String currentVersionId) {
        this.currentVersionId = currentVersionId;
    }

    public ScriptVersionResponse getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(ScriptVersionResponse currentVersion) {
        this.currentVersion = currentVersion;
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
