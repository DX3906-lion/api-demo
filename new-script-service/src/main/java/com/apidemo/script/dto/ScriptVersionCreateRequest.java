package com.apidemo.script.dto;

/**
 * Request body for creating a new draft script version.
 */
public class ScriptVersionCreateRequest {

    /** Optional version description. */
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
