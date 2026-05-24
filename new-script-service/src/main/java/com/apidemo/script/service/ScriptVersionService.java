package com.apidemo.script.service;

import com.apidemo.script.dto.ScriptVersionCreateRequest;
import com.apidemo.script.dto.ScriptVersionResponse;

/**
 * Service interface for script version operations.
 */
public interface ScriptVersionService {

    /**
     * Creates a new DRAFT version for a script.
     *
     * @param scriptId script id
     * @param request draft version creation request
     * @return created version detail
     */
    ScriptVersionResponse createDraftVersion(String scriptId, ScriptVersionCreateRequest request);

    /**
     * Gets a version detail by script id and version id.
     *
     * @param scriptId script id
     * @param versionId version id
     * @return version detail
     */
    ScriptVersionResponse getVersionDetail(String scriptId, String versionId);

    /**
     * Publishes a DRAFT version and updates the script current version pointer.
     *
     * @param scriptId script id
     * @param versionId version id
     * @return published version detail
     */
    ScriptVersionResponse publishVersion(String scriptId, String versionId);
}
