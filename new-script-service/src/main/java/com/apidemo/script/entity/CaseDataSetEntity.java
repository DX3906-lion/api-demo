package com.apidemo.script.entity;

import com.apidemo.common.model.BaseEntity;

/**
 * Case data set entity mapped to the case_data_set table.
 */
public class CaseDataSetEntity extends BaseEntity {

    /** Owning script id. */
    private String scriptId;

    /** Owning script version id. */
    private String scriptVersionId;

    /** Case data set name. */
    private String name;

    /** Case data set description. */
    private String description;

    /** Case data set status, defaults to ENABLED. */
    private String status;

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
}
