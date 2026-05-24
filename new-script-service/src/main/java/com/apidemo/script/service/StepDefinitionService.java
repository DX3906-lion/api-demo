package com.apidemo.script.service;

import com.apidemo.script.dto.StepDefinitionRequest;
import com.apidemo.script.dto.StepDefinitionResponse;
import java.util.List;

/**
 * Service interface for step definition operations.
 */
public interface StepDefinitionService {

    /**
     * Creates a step under a DRAFT version.
     *
     * @param scriptId script id
     * @param versionId version id
     * @param request step creation request
     * @return created step detail
     */
    StepDefinitionResponse createStep(String scriptId, String versionId, StepDefinitionRequest request);

    /**
     * Updates a step under a DRAFT version.
     *
     * @param scriptId script id
     * @param versionId version id
     * @param stepId step id
     * @param request step update request
     * @return updated step detail
     */
    StepDefinitionResponse updateStep(String scriptId, String versionId, String stepId, StepDefinitionRequest request);

    /**
     * Logically deletes a step under a DRAFT version.
     *
     * @param scriptId script id
     * @param versionId version id
     * @param stepId step id
     */
    void deleteStep(String scriptId, String versionId, String stepId);

    /**
     * Lists active steps in a version ordered by sort number.
     *
     * @param scriptId script id
     * @param versionId version id
     * @return active step list
     */
    List<StepDefinitionResponse> listSteps(String scriptId, String versionId);
}
