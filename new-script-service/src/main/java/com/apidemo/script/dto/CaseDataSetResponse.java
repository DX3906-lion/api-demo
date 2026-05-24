package com.apidemo.script.dto;

import java.time.LocalDateTime;

/**
 * Response body for case data set summary.
 */
public class CaseDataSetResponse {

    /** Case data set id. */
    private String id;

    /** Owning script id. */
    private String scriptId;

    /** Owning script version id. */
    private String scriptVersionId;

    /** Case data set name. */
    private String name;

    /** Case data set description. */
    private String description;

    /** Case data set status. */
    private String status;

    /** Created time. */
    private LocalDateTime createdTime;

    /** Updated time. */
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

    public String getScriptVersionId() {
        return scriptVersionId;
    }

    public void setScriptVersionId(String scriptVersionId) {
        this.scriptVersionId = scriptVersionId;
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
