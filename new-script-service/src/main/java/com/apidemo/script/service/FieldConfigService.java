package com.apidemo.script.service;

import com.apidemo.script.dto.FieldConfigRequest;
import com.apidemo.script.dto.FieldConfigResponse;
import com.apidemo.script.dto.ScriptFieldDefaultRequest;
import com.apidemo.script.dto.ScriptFieldDefaultResponse;
import java.util.List;

/**
 * Service interface for field configuration and script default value operations.
 */
public interface FieldConfigService {

    /**
     * Lists field configurations in one script version.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param stepId optional step id filter
     * @param fieldScope optional field scope filter
     * @return field configuration list
     */
    List<FieldConfigResponse> listFields(String scriptId, String versionId, String stepId, String fieldScope);

    /**
     * Creates a field configuration under a DRAFT version.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param request field configuration request
     * @return created field configuration
     */
    FieldConfigResponse createField(String scriptId, String versionId, FieldConfigRequest request);

    /**
     * Updates a field configuration under a DRAFT version.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param fieldId field configuration id
     * @param request field configuration request
     * @return updated field configuration
     */
    FieldConfigResponse updateField(String scriptId, String versionId, String fieldId, FieldConfigRequest request);

    /**
     * Logically deletes a field configuration and its script default under a DRAFT version.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param fieldId field configuration id
     */
    void deleteField(String scriptId, String versionId, String fieldId);

    /**
     * Saves a field default value under a DRAFT version.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param fieldId field configuration id
     * @param request field default value request
     * @return saved default value
     */
    ScriptFieldDefaultResponse saveDefault(String scriptId, String versionId, String fieldId, ScriptFieldDefaultRequest request);

    /**
     * Lists field defaults with field metadata in one script version.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param stepId optional step id filter
     * @param fieldScope optional field scope filter
     * @return field default list
     */
    List<ScriptFieldDefaultResponse> listDefaults(String scriptId, String versionId, String stepId, String fieldScope);
}
