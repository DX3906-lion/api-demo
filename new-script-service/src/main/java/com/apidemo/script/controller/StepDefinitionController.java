package com.apidemo.script.controller;

import com.apidemo.common.response.ApiResponse;
import com.apidemo.script.dto.StepDefinitionRequest;
import com.apidemo.script.dto.StepDefinitionResponse;
import com.apidemo.script.service.StepDefinitionService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for step definition operations.
 */
@RestController
@RequestMapping("/api/scripts/{scriptId}/versions/{versionId}/steps")
public class StepDefinitionController {

    private final StepDefinitionService stepDefinitionService;

    public StepDefinitionController(StepDefinitionService stepDefinitionService) {
        this.stepDefinitionService = stepDefinitionService;
    }

    /**
     * Creates a step under a DRAFT version.
     *
     * @param scriptId script id path variable
     * @param versionId version id path variable
     * @param request step creation request
     * @return created step response
     */
    @PostMapping
    public ApiResponse<StepDefinitionResponse> createStep(@PathVariable String scriptId,
                                                          @PathVariable String versionId,
                                                          @Valid @RequestBody StepDefinitionRequest request) {
        return ApiResponse.success(stepDefinitionService.createStep(scriptId, versionId, request));
    }

    /**
     * Updates a step under a DRAFT version.
     *
     * @param scriptId script id path variable
     * @param versionId version id path variable
     * @param stepId step id path variable
     * @param request step update request
     * @return updated step response
     */
    @PutMapping("/{stepId}")
    public ApiResponse<StepDefinitionResponse> updateStep(@PathVariable String scriptId,
                                                          @PathVariable String versionId,
                                                          @PathVariable String stepId,
                                                          @Valid @RequestBody StepDefinitionRequest request) {
        return ApiResponse.success(stepDefinitionService.updateStep(scriptId, versionId, stepId, request));
    }

    /**
     * Logically deletes a step under a DRAFT version.
     *
     * @param scriptId script id path variable
     * @param versionId version id path variable
     * @param stepId step id path variable
     * @return empty success response
     */
    @DeleteMapping("/{stepId}")
    public ApiResponse<Void> deleteStep(@PathVariable String scriptId,
                                        @PathVariable String versionId,
                                        @PathVariable String stepId) {
        stepDefinitionService.deleteStep(scriptId, versionId, stepId);
        return ApiResponse.success();
    }

    /**
     * Lists active steps in a version.
     *
     * @param scriptId script id path variable
     * @param versionId version id path variable
     * @return ordered step list response
     */
    @GetMapping
    public ApiResponse<List<StepDefinitionResponse>> listSteps(@PathVariable String scriptId,
                                                               @PathVariable String versionId) {
        return ApiResponse.success(stepDefinitionService.listSteps(scriptId, versionId));
    }
}
