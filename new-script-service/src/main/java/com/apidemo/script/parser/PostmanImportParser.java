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
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Minimal Postman Collection v2.1 parser that flattens request items into HTTP step previews.
 */
@Component
public class PostmanImportParser implements ImportFileParser {

    private final ObjectMapper objectMapper;
    private final ImportFieldParserSupport fieldParserSupport;

    public PostmanImportParser(ObjectMapper objectMapper, ImportFieldParserSupport fieldParserSupport) {
        this.objectMapper = objectMapper;
        this.fieldParserSupport = fieldParserSupport;
    }

    /**
     * Returns the supported import type.
     *
     * @return POSTMAN import type
     */
    @Override
    public String getImportType() {
        return ScriptConstants.IMPORT_TYPE_POSTMAN;
    }

    /**
     * Parses Postman collection items into normalized HTTP step previews.
     *
     * @param root Postman collection root JSON node
     * @return normalized import preview
     */
    @Override
    public ImportPreviewResponse parse(JsonNode root) {
        JsonNode items = root.path("item");
        if (!items.isArray()) {
            throw new BizException(ErrorCode.IMPORT_PARSE_FAILED, "Postman item array is missing or invalid");
        }

        List<String> warnings = new ArrayList<String>();
        List<ImportPreviewStepResponse> steps = new ArrayList<ImportPreviewStepResponse>();
        if (root.has("event")) {
            warnings.add("Postman collection event scripts are not imported.");
        }
        collectItems(items, steps, warnings);

        ImportPreviewResponse response = new ImportPreviewResponse();
        response.setImportType(getImportType());
        response.setSteps(steps);
        response.setWarnings(warnings);
        return response;
    }

    private void collectItems(JsonNode items, List<ImportPreviewStepResponse> steps, List<String> warnings) {
        for (JsonNode item : items) {
            if (item.has("item") && item.path("item").isArray()) {
                collectItems(item.path("item"), steps, warnings);
            }
            if (item.has("request")) {
                steps.add(parseRequestItem(item, steps.size() + 1, warnings));
            }
        }
    }

    private ImportPreviewStepResponse parseRequestItem(JsonNode item, int index, List<String> warnings) {
        JsonNode request = item.path("request");
        String name = item.path("name").asText("Postman Request " + index);
        String method = request.path("method").asText("GET");
        String url = buildUrl(request.path("url"));

        if (request.has("auth")) {
            warnings.add("Postman auth configuration for step '" + name + "' is not imported.");
        }
        if (item.has("event") || request.has("event")) {
            warnings.add("Postman pre-request/test scripts for step '" + name + "' are not imported.");
        }

        ObjectNode requestConfig = objectMapper.createObjectNode();
        requestConfig.set("headers", request.path("header"));
        requestConfig.set("queryParams", request.path("url").path("query"));
        requestConfig.set("body", request.path("body"));

        List<ImportPreviewFieldResponse> fields = new ArrayList<ImportPreviewFieldResponse>();
        fields.addAll(parseNameValueArray(name, request.path("header"),
                ScriptConstants.FIELD_SCOPE_REQUEST_HEADER, "header."));
        fields.addAll(parseNameValueArray(name, request.path("url").path("query"),
                ScriptConstants.FIELD_SCOPE_REQUEST_QUERY, "query."));
        fields.addAll(parseBody(name, request.path("body"), warnings));

        ImportPreviewStepResponse step = new ImportPreviewStepResponse();
        step.setTempStepId("postman-step-" + index);
        step.setName(name);
        step.setStepType(ScriptConstants.STEP_TYPE_HTTP);
        step.setRequestMethod(method);
        step.setRequestUrl(url);
        step.setRequestConfig(toJson(requestConfig, warnings));
        step.setFields(fields);
        return step;
    }

    private List<ImportPreviewFieldResponse> parseBody(String stepName, JsonNode body, List<String> warnings) {
        List<ImportPreviewFieldResponse> fields = new ArrayList<ImportPreviewFieldResponse>();
        if (body.isMissingNode() || body.isNull()) {
            return fields;
        }
        String mode = body.path("mode").asText("");
        if ("raw".equalsIgnoreCase(mode)) {
            fields.addAll(fieldParserSupport.extractBodyFields(stepName, body.path("raw").asText(null), warnings));
            return fields;
        }
        if ("urlencoded".equalsIgnoreCase(mode)) {
            fields.addAll(parseNameValueArray(stepName, body.path("urlencoded"),
                    ScriptConstants.FIELD_SCOPE_REQUEST_BODY, "form."));
            return fields;
        }
        if ("formdata".equalsIgnoreCase(mode)) {
            fields.addAll(parseNameValueArray(stepName, body.path("formdata"),
                    ScriptConstants.FIELD_SCOPE_REQUEST_BODY, "form."));
            return fields;
        }
        if (!mode.trim().isEmpty()) {
            warnings.add("Unsupported Postman body mode '" + mode + "' for step '" + stepName + "'.");
        }
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
            String key = item.path("key").asText("");
            if (key.trim().isEmpty()) {
                key = item.path("name").asText("");
            }
            if (key.trim().isEmpty()) {
                continue;
            }
            String value = item.path("value").isMissingNode() ? null : item.path("value").asText("");
            fields.add(fieldParserSupport.buildField(stepName, fieldScope, pathPrefix + key, key,
                    value, fieldParserSupport.inferStringType(value), ScriptConstants.DISABLED));
        }
        return fields;
    }

    private String buildUrl(JsonNode urlNode) {
        if (urlNode.isTextual()) {
            return urlNode.asText();
        }
        String raw = urlNode.path("raw").asText("");
        if (!raw.trim().isEmpty()) {
            return raw;
        }
        String protocol = urlNode.path("protocol").asText("http");
        String host = joinArray(urlNode.path("host"), ".");
        String path = joinArray(urlNode.path("path"), "/");
        StringBuilder url = new StringBuilder();
        url.append(protocol).append("://").append(host);
        if (!path.isEmpty()) {
            url.append("/").append(path);
        }
        String query = buildQuery(urlNode.path("query"));
        if (!query.isEmpty()) {
            url.append("?").append(query);
        }
        return url.toString();
    }

    private String buildQuery(JsonNode queryNode) {
        if (!queryNode.isArray()) {
            return "";
        }
        StringBuilder query = new StringBuilder();
        for (JsonNode item : queryNode) {
            String key = item.path("key").asText("");
            if (key.trim().isEmpty()) {
                continue;
            }
            if (query.length() > 0) {
                query.append("&");
            }
            query.append(key).append("=").append(item.path("value").asText(""));
        }
        return query.toString();
    }

    private String joinArray(JsonNode array, String separator) {
        if (!array.isArray()) {
            return array.asText("");
        }
        StringBuilder builder = new StringBuilder();
        for (JsonNode item : array) {
            if (builder.length() > 0) {
                builder.append(separator);
            }
            builder.append(item.asText());
        }
        return builder.toString();
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
