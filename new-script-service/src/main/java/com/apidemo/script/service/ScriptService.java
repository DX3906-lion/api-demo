package com.apidemo.script.service;

import com.apidemo.script.dto.ScriptCreateRequest;
import com.apidemo.script.dto.ScriptDetailResponse;

/**
 * Service interface for script master operations.
 */
public interface ScriptService {

    /**
     * Creates a script and its initial DRAFT version in one transaction.
     *
     * @param request script creation request
     * @return created script detail
     */
    ScriptDetailResponse createScript(ScriptCreateRequest request);

    /**
     * Gets script detail and the current version detail.
     *
     * @param scriptId script id
     * @return script detail
     */
    ScriptDetailResponse getScriptDetail(String scriptId);
}
