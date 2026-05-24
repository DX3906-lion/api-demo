package com.apidemo.script.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * Request body for saving one or more payload content rows for a step.
 */
public class StepPayloadContentBatchSaveRequest {

    /** Payload content rows to upsert. */
    @Valid
    @NotEmpty(message = "Payload 内容不能为空")
    private List<StepPayloadContentItemRequest> contents;

    public List<StepPayloadContentItemRequest> getContents() {
        return contents;
    }

    public void setContents(List<StepPayloadContentItemRequest> contents) {
        this.contents = contents;
    }
}
