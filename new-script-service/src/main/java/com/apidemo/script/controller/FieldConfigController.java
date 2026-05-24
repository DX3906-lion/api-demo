package com.apidemo.script.controller;

import com.apidemo.common.response.ApiResponse;
import com.apidemo.script.dto.FieldConfigRequest;
import com.apidemo.script.dto.FieldConfigResponse;
import com.apidemo.script.dto.ScriptFieldDefaultRequest;
import com.apidemo.script.dto.ScriptFieldDefaultResponse;
import com.apidemo.script.service.FieldConfigService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for field configuration and script field default operations.
 */
@RestController
@RequestMapping("/api/scripts/{scriptId}/versions/{versionId}/fields")
public class FieldConfigController {

    private final FieldConfigService fieldConfigService;

    public FieldConfigController(FieldConfigService fieldConfigService) {
        this.fieldConfigService = fieldConfigService;
    }

    /**
     * Lists field configurations with optional filters.
     *
     * @param scriptId script id path variable
     * @param versionId version id path variable
     * @param stepId optional step id filter
     * @param fieldScope optional field scope filter
     * @return field configuration list response
     */
    @GetMapping
    public ApiResponse<List<FieldConfigResponse>> listFields(@PathVariable String scriptId,
                                                             @PathVariable String versionId,
                                                             @RequestParam(required = false) String stepId,
                                                             @RequestParam(required = false) String fieldScope) {
        return ApiResponse.success(fieldConfigService.listFields(scriptId, versionId, stepId, fieldScope));
    }

    /**
     * Creates a field configuration under a DRAFT version.
     *
     * @param scriptId script id path variable
     * @param versionId version id path variable
     * @param request field configuration request
     * @return created field configuration response
     */
    @PostMapping
    public ApiResponse<FieldConfigResponse> createField(@PathVariable String scriptId,
                                                        @PathVariable String versionId,
                                                        @Valid @RequestBody FieldConfigRequest request) {
        return ApiResponse.success(fieldConfigService.createField(scriptId, versionId, request));
    }

    /**
     * Updates a field configuration under a DRAFT version.
     *
     * @param scriptId script id path variable
     * @param versionId version id path variable
     * @param fieldId field configuration id path variable
     * @param request field configuration request
     * @return updated field configuration response
     */
    @PutMapping("/{fieldId}")
    public ApiResponse<FieldConfigResponse> updateField(@PathVariable String scriptId,
                                                        @PathVariable String versionId,
                                                        @PathVariable String fieldId,
                                                        @Valid @RequestBody FieldConfigRequest request) {
        return ApiResponse.success(fieldConfigService.updateField(scriptId, versionId, fieldId, request));
    }

    /**
     * Logically deletes a field configuration under a DRAFT version.
     *
     * @param scriptId script id path variable
     * @param versionId version id path variable
     * @param fieldId field configuration id path variable
     * @return empty success response
     */
    @DeleteMapping("/{fieldId}")
    public ApiResponse<Void> deleteField(@PathVariable String scriptId,
                                         @PathVariable String versionId,
                                         @PathVariable String fieldId) {
        fieldConfigService.deleteField(scriptId, versionId, fieldId);
        return ApiResponse.success();
    }

    /**
     * Saves a script field default value under a DRAFT version.
     *
     * @param scriptId script id path variable
     * @param versionId version id path variable
     * @param fieldId field configuration id path variable
     * @param request field default request
     * @return saved field default response
     */
    @PutMapping("/{fieldId}/default")
    public ApiResponse<ScriptFieldDefaultResponse> saveDefault(@PathVariable String scriptId,
                                                               @PathVariable String versionId,
                                                               @PathVariable String fieldId,
                                                               @RequestBody ScriptFieldDefaultRequest request) {
        return ApiResponse.success(fieldConfigService.saveDefault(scriptId, versionId, fieldId, request));
    }

    /**
     * Lists script field defaults with field metadata.
     *
     * @param scriptId script id path variable
     * @param versionId version id path variable
     * @param stepId optional step id filter
     * @param fieldScope optional field scope filter
     * @return field default list response
     */
    @GetMapping("/defaults")
    public ApiResponse<List<ScriptFieldDefaultResponse>> listDefaults(@PathVariable String scriptId,
                                                                      @PathVariable String versionId,
                                                                      @RequestParam(required = false) String stepId,
                                                                      @RequestParam(required = false) String fieldScope) {
        return ApiResponse.success(fieldConfigService.listDefaults(scriptId, versionId, stepId, fieldScope));
    }
}
