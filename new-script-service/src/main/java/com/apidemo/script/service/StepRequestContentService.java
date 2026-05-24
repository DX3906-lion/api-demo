package com.apidemo.script.service;

import com.apidemo.script.dto.StepPayloadContentBatchSaveRequest;
import com.apidemo.script.dto.StepPayloadContentResponse;
import com.apidemo.script.dto.StepRequestConfigRequest;
import com.apidemo.script.dto.StepRequestConfigResponse;
import java.util.List;

/**
 * Service interface for step request configuration and payload content operations.
 */
public interface StepRequestContentService {

    /**
     * Gets request configuration by step id.
     *
     * @param stepId step id
     * @return request configuration response, or a compatibility response from step_definition when no config row exists
     */
    StepRequestConfigResponse getRequestConfig(String stepId);

    /**
     * Saves request configuration for a DRAFT-version step.
     *
     * @param stepId step id
     * @param request request configuration payload
     * @return saved request configuration response
     */
    StepRequestConfigResponse saveRequestConfig(String stepId, StepRequestConfigRequest request);

    /**
     * Lists payload content rows by step id.
     *
     * @param stepId step id
     * @return payload content list ordered by direction and location
     */
    List<StepPayloadContentResponse> listPayloadContents(String stepId);

    /**
     * Saves payload content rows for a DRAFT-version step.
     *
     * @param stepId step id
     * @param request payload content batch request
     * @return saved payload content rows
     */
    List<StepPayloadContentResponse> savePayloadContents(String stepId, StepPayloadContentBatchSaveRequest request);
}
