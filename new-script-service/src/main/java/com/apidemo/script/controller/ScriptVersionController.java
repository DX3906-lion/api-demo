package com.apidemo.script.controller;

import com.apidemo.common.response.ApiResponse;
import com.apidemo.script.dto.ScriptVersionCreateRequest;
import com.apidemo.script.dto.ScriptVersionResponse;
import com.apidemo.script.service.ScriptVersionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for script version operations.
 */
@RestController
@RequestMapping("/api/scripts/{scriptId}/versions")
public class ScriptVersionController {

    private final ScriptVersionService scriptVersionService;

    public ScriptVersionController(ScriptVersionService scriptVersionService) {
        this.scriptVersionService = scriptVersionService;
    }

    /**
     * Creates a new DRAFT version for the script.
     *
     * @param scriptId script id path variable
     * @param request draft version creation request
     * @return created version response
     */
    @PostMapping
    public ApiResponse<ScriptVersionResponse> createDraftVersion(@PathVariable String scriptId,
                                                                 @RequestBody(required = false) ScriptVersionCreateRequest request) {
        return ApiResponse.success(scriptVersionService.createDraftVersion(scriptId, request));
    }

    /**
     * Gets version detail by script id and version id.
     *
     * @param scriptId script id path variable
     * @param versionId version id path variable
     * @return version detail response
     */
    @GetMapping("/{versionId}")
    public ApiResponse<ScriptVersionResponse> getVersionDetail(@PathVariable String scriptId,
                                                               @PathVariable String versionId) {
        return ApiResponse.success(scriptVersionService.getVersionDetail(scriptId, versionId));
    }

    /**
     * Publishes a DRAFT version.
     *
     * @param scriptId script id path variable
     * @param versionId version id path variable
     * @return published version response
     */
    @PostMapping("/{versionId}/publish")
    public ApiResponse<ScriptVersionResponse> publishVersion(@PathVariable String scriptId,
                                                             @PathVariable String versionId) {
        return ApiResponse.success(scriptVersionService.publishVersion(scriptId, versionId));
    }
}
