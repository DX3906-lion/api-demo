package com.apidemo.script.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Request body for creating a script.
 */
public class ScriptCreateRequest {

    /** Script name, required and limited to 128 characters by T04. */
    @NotBlank(message = "脚本名称不能为空")
    @Size(max = 128, message = "脚本名称长度不能超过128")
    private String name;

    /** Optional script description. */
    private String description;

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
}
