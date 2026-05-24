package com.apidemo.script.service;

import com.apidemo.script.dto.ImportConfirmRequest;
import com.apidemo.script.dto.ImportConfirmResponse;
import com.apidemo.script.dto.ImportPreviewResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for import preview and import confirmation operations.
 */
public interface ImportService {

    /**
     * Parses an uploaded import file into a preview model without persisting data.
     *
     * @param file uploaded HAR or Postman collection file
     * @param importType optional import type, HAR or POSTMAN
     * @return import preview response
     */
    ImportPreviewResponse preview(MultipartFile file, String importType);

    /**
     * Confirms a preview and persists steps, field configs, and field defaults into a DRAFT version.
     *
     * @param scriptId target script id
     * @param versionId target script version id
     * @param request confirm request
     * @return import confirmation summary
     */
    ImportConfirmResponse confirm(String scriptId, String versionId, ImportConfirmRequest request);
}
