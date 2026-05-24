package com.apidemo.script.entity;

import com.apidemo.common.model.BaseEntity;

/**
 * Script master entity mapped to the script table.
 */
public class ScriptEntity extends BaseEntity {

    /** Script display name. */
    private String name;

    /** Script description entered by the user. */
    private String description;

    /** Script lifecycle status, such as DRAFT or PUBLISHED in the current stage. */
    private String status;

    /** Current script version id used by the editor. */
    private String currentVersionId;

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
}
