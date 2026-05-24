package com.apidemo.script.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Request body for creating or updating a case data set.
 */
public class CaseDataSetRequest {

    /** Case data set name, required and limited to 128 characters. */
    @NotBlank(message = "用例数据集名称不能为空")
    @Size(max = 128, message = "用例数据集名称长度不能超过128")
    private String name;

    /** Case data set description. */
    private String description;

    /** Case data set status, defaults to ENABLED. */
    private String status;

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
