package com.apidemo.script.parser;

import com.apidemo.common.error.ErrorCode;
import com.apidemo.common.exception.BizException;
import com.apidemo.script.constant.ScriptConstants;
import com.apidemo.script.dto.ImportPreviewFieldResponse;
import com.apidemo.script.dto.ImportPreviewResponse;
import com.apidemo.script.dto.ImportPreviewStepResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Minimal HAR parser that converts log.entries into HTTP step previews.
 */
@Component
public class HarImportParser implements ImportFileParser {

    private final ObjectMapper objectMapper;
    private final ImportFieldParserSupport fieldParserSupport;

    public HarImportParser(ObjectMapper objectMapper, ImportFieldParserSupport fieldParserSupport) {
        this.objectMapper = objectMapper;
        this.fieldParserSupport = fieldParserSupport;
    }

    /**
     * Returns the supported import type.
     *
     * @return HAR import type
     */
    @Override
    public String getImportType() {
        return ScriptConstants.IMPORT_TYPE_HAR;
    }

    /**
     * Parses HAR log.entries into normalized HTTP step previews.
     *
     * @param root HAR root JSON node
     * @return normalized import preview
     */
    @Override
    public ImportPreviewResponse parse(JsonNode root) {
        JsonNode entries = root.path("log").path("entries");
        if (!entries.isArray()) {
            throw new BizException(ErrorCode.IMPORT_PARSE_FAILED, "HAR log.entries is missing or invalid");
        }

        List<String> warnings = new ArrayList<String>();
        List<ImportPreviewStepResponse> steps = new ArrayList<ImportPreviewStepResponse>();
        int index = 1;
        for (JsonNode entry : entries) {
            JsonNode request = entry.path("request");
            if (request.isMissingNode() || request.path("url").asText("").trim().isEmpty()) {
                warnings.add("Skipped a HAR entry because request.url is missing.");
                continue;
            }
            steps.add(parseRequest(request, index, warnings));
            index++;
        }

        ImportPreviewResponse response = new ImportPreviewResponse();
        response.setImportType(getImportType());
        response.setSteps(steps);
        response.setWarnings(warnings);
        return response;
    }

    private ImportPreviewStepResponse parseRequest(JsonNode request, int index, List<String> warnings) {
        String method = request.path("method").asText("GET");
        String url = request.path("url").asText();
        String name = method + " " + requestPath(url);

        ObjectNode requestConfig = objectMapper.createObjectNode();
        requestConfig.set("headers", request.path("headers"));
        requestConfig.set("queryString", request.path("queryString"));
        requestConfig.set("postData", request.path("postData"));
        requestConfig.set("cookies", request.path("cookies"));

        List<ImportPreviewFieldResponse> fields = new ArrayList<ImportPreviewFieldResponse>();
        fields.addAll(parseNameValueArray(name, request.path("headers"),
                ScriptConstants.FIELD_SCOPE_REQUEST_HEADER, "header."));
        fields.addAll(parseNameValueArray(name, request.path("queryString"),
                ScriptConstants.FIELD_SCOPE_REQUEST_QUERY, "query."));
        fields.addAll(parsePostData(name, request.path("postData"), warnings));

        ImportPreviewStepResponse step = new ImportPreviewStepResponse();
        step.setTempStepId("har-step-" + index);
        step.setName(name);
        step.setStepType(ScriptConstants.STEP_TYPE_HTTP);
        step.setRequestMethod(method);
        step.setRequestUrl(url);
        step.setRequestConfig(toJson(requestConfig, warnings));
        step.setFields(fields);
        return step;
    }

    private List<ImportPreviewFieldResponse> parsePostData(String stepName, JsonNode postData, List<String> warnings) {
        List<ImportPreviewFieldResponse> fields = new ArrayList<ImportPreviewFieldResponse>();
        if (postData.isMissingNode() || postData.isNull()) {
            return fields;
        }
        JsonNode params = postData.path("params");
        if (params.isArray() && params.size() > 0) {
            fields.addAll(parseNameValueArray(stepName, params, ScriptConstants.FIELD_SCOPE_REQUEST_BODY, "form."));
            return fields;
        }
        String text = postData.path("text").asText(null);
        fields.addAll(fieldParserSupport.extractBodyFields(stepName, text, warnings));
        return fields;
    }

    private List<ImportPreviewFieldResponse> parseNameValueArray(String stepName,
                                                                 JsonNode array,
                                                                 String fieldScope,
                                                                 String pathPrefix) {
        List<ImportPreviewFieldResponse> fields = new ArrayList<ImportPreviewFieldResponse>();
        if (!array.isArray()) {
            return fields;
        }
        for (JsonNode item : array) {
            String name = item.path("name").asText("");
            if (name.trim().isEmpty()) {
                continue;
            }
            String value = item.path("value").isMissingNode() ? null : item.path("value").asText("");
            fields.add(fieldParserSupport.buildField(stepName, fieldScope, pathPrefix + name, name,
                    value, fieldParserSupport.inferStringType(value), ScriptConstants.DISABLED));
        }
        return fields;
    }

    private String requestPath(String url) {
        try {
            URI uri = URI.create(url);
            if (uri.getRawPath() != null && !uri.getRawPath().isEmpty()) {
                return uri.getRawPath();
            }
        } catch (IllegalArgumentException ex) {
            return url;
        }
        return url;
    }

    private String toJson(JsonNode node, List<String> warnings) {
        try {
            return objectMapper.writeValueAsString(node);
        } catch (Exception ex) {
            warnings.add("Failed to serialize request config; using empty config.");
            return ScriptConstants.EMPTY_JSON_OBJECT;
        }
    }
}
