package com.apidemo.script.controller;

import com.apidemo.common.response.ApiResponse;
import com.apidemo.script.dto.StepPayloadContentBatchSaveRequest;
import com.apidemo.script.dto.StepPayloadContentResponse;
import com.apidemo.script.dto.StepRequestConfigRequest;
import com.apidemo.script.dto.StepRequestConfigResponse;
import com.apidemo.script.service.StepRequestContentService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for step request configuration and payload content operations.
 */
@RestController
@RequestMapping("/api/steps/{stepId}")
public class StepRequestContentController {

    private final StepRequestContentService stepRequestContentService;

    public StepRequestContentController(StepRequestContentService stepRequestContentService) {
        this.stepRequestContentService = stepRequestContentService;
    }

    /**
     * Gets request configuration by step id.
     *
     * @param stepId step id path variable
     * @return request configuration response
     */
    @GetMapping("/request-config")
    public ApiResponse<StepRequestConfigResponse> getRequestConfig(@PathVariable String stepId) {
        return ApiResponse.success(stepRequestContentService.getRequestConfig(stepId));
    }

    /**
     * Saves request configuration for a DRAFT-version step.
     *
     * @param stepId step id path variable
     * @param request request configuration body
     * @return saved request configuration response
     */
    @PutMapping("/request-config")
    public ApiResponse<StepRequestConfigResponse> saveRequestConfig(@PathVariable String stepId,
                                                                    @Valid @RequestBody StepRequestConfigRequest request) {
        return ApiResponse.success(stepRequestContentService.saveRequestConfig(stepId, request));
    }

    /**
     * Lists payload content rows by step id.
     *
     * @param stepId step id path variable
     * @return payload content list response
     */
    @GetMapping("/payload")
    public ApiResponse<List<StepPayloadContentResponse>> listPayloadContents(@PathVariable String stepId) {
        return ApiResponse.success(stepRequestContentService.listPayloadContents(stepId));
    }

    /**
     * Saves payload content rows for a DRAFT-version step.
     *
     * @param stepId step id path variable
     * @param request payload content batch body
     * @return saved payload content list response
     */
    @PutMapping("/payload")
    public ApiResponse<List<StepPayloadContentResponse>> savePayloadContents(@PathVariable String stepId,
                                                                            @Valid @RequestBody StepPayloadContentBatchSaveRequest request) {
        return ApiResponse.success(stepRequestContentService.savePayloadContents(stepId, request));
    }
}
