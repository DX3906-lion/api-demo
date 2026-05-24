package com.apidemo.script.controller;

import com.apidemo.common.response.ApiResponse;
import com.apidemo.script.dto.ImportConfirmRequest;
import com.apidemo.script.dto.ImportConfirmResponse;
import com.apidemo.script.dto.ImportPreviewResponse;
import com.apidemo.script.service.ImportService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for HAR and Postman import preview and confirmation.
 */
@RestController
public class ImportController {

    private final ImportService importService;

    public ImportController(ImportService importService) {
        this.importService = importService;
    }

    /**
     * Parses an uploaded import file and returns a preview without persisting data.
     *
     * @param file uploaded HAR or Postman JSON file
     * @param importType optional import type
     * @return import preview response
     */
    @PostMapping("/api/imports/preview")
    public ApiResponse<ImportPreviewResponse> preview(@RequestParam("file") MultipartFile file,
                                                       @RequestParam(value = "importType", required = false) String importType) {
        return ApiResponse.success(importService.preview(file, importType));
    }

    /**
     * Confirms imported steps and persists them into a DRAFT script version.
     *
     * @param scriptId target script id
     * @param versionId target script version id
     * @param request import confirmation request
     * @return import confirmation summary
     */
    @PostMapping("/api/scripts/{scriptId}/versions/{versionId}/imports/confirm")
    public ApiResponse<ImportConfirmResponse> confirm(@PathVariable String scriptId,
                                                       @PathVariable String versionId,
                                                       @Valid @RequestBody ImportConfirmRequest request) {
        return ApiResponse.success(importService.confirm(scriptId, versionId, request));
    }
}
