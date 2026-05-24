package com.apidemo.script.parser;

import com.apidemo.script.dto.ImportPreviewResponse;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Parser contract for supported import file formats.
 */
public interface ImportFileParser {

    /**
     * Returns the import type handled by this parser.
     *
     * @return import type value such as HAR or POSTMAN
     */
    String getImportType();

    /**
     * Parses an import file JSON tree into a normalized preview model.
     *
     * @param root import file root JSON node
     * @return normalized import preview
     */
    ImportPreviewResponse parse(JsonNode root);
}
