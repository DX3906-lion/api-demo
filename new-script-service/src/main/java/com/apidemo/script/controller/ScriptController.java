package com.apidemo.script.controller;

import com.apidemo.common.response.ApiResponse;
import com.apidemo.script.dto.ScriptCreateRequest;
import com.apidemo.script.dto.ScriptDetailResponse;
import com.apidemo.script.service.ScriptService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for script master operations.
 */
@RestController
@RequestMapping("/api/scripts")
public class ScriptController {

    private final ScriptService scriptService;

    public ScriptController(ScriptService scriptService) {
        this.scriptService = scriptService;
    }

    /**
     * Creates a script and returns its default DRAFT version.
     *
     * @param request script creation request
     * @return created script detail response
     */
    @PostMapping
    public ApiResponse<ScriptDetailResponse> createScript(@Valid @RequestBody ScriptCreateRequest request) {
        return ApiResponse.success(scriptService.createScript(request));
    }

    /**
     * Gets script detail by script id.
     *
     * @param scriptId script id path variable
     * @return script detail response
     */
    @GetMapping("/{scriptId}")
    public ApiResponse<ScriptDetailResponse> getScriptDetail(@PathVariable String scriptId) {
        return ApiResponse.success(scriptService.getScriptDetail(scriptId));
    }
}
