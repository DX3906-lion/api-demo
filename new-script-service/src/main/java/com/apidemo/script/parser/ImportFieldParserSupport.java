package com.apidemo.script.parser;

import com.apidemo.script.constant.ScriptConstants;
import com.apidemo.script.dto.ImportPreviewFieldResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Shared helpers for extracting basic request fields from imported HTTP requests.
 */
@Component
public class ImportFieldParserSupport {

    private static final int STABLE_KEY_MAX_LENGTH = 500;

    private final ObjectMapper objectMapper;

    public ImportFieldParserSupport(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Builds a field preview from primitive import metadata.
     *
     * @param stepName step name used in stable key generation
     * @param fieldScope field scope
     * @param fieldPath field path
     * @param fieldKey field key
     * @param defaultValue default value, may be null
     * @param dataType inferred data type
     * @param arrayFlag whether the field is an array
     * @return field preview
     */
    public ImportPreviewFieldResponse buildField(String stepName,
                                                 String fieldScope,
                                                 String fieldPath,
                                                 String fieldKey,
                                                 String defaultValue,
                                                 String dataType,
                                                 Integer arrayFlag) {
        ImportPreviewFieldResponse field = new ImportPreviewFieldResponse();
        field.setFieldScope(fieldScope);
        field.setFieldPath(fieldPath);
        field.setFieldKey(fieldKey);
        field.setFieldName(fieldKey);
        field.setStableFieldKey(stableFieldKey(stepName, fieldScope, fieldPath));
        field.setDataType(dataType == null ? ScriptConstants.DATA_TYPE_STRING : dataType);
        field.setRequired(ScriptConstants.DISABLED);
        field.setArrayFlag(arrayFlag == null ? ScriptConstants.DISABLED : arrayFlag);
        field.setSensitive(isSensitive(fieldKey, fieldPath) ? ScriptConstants.ENABLED : ScriptConstants.DISABLED);
        field.setDefaultValue(defaultValue);
        field.setValueSource(defaultValue == null ? null : ScriptConstants.VALUE_SOURCE_IMPORT);
        return field;
    }

    /**
     * Extracts fields from a JSON body string.
     *
     * @param stepName step name
     * @param bodyText body text
     * @param warnings parser warnings to append to
     * @return parsed body fields
     */
    public List<ImportPreviewFieldResponse> extractBodyFields(String stepName, String bodyText, List<String> warnings) {
        List<ImportPreviewFieldResponse> fields = new ArrayList<ImportPreviewFieldResponse>();
        if (bodyText == null || bodyText.trim().isEmpty()) {
            return fields;
        }
        try {
            JsonNode root = objectMapper.readTree(bodyText);
            if (root.isObject()) {
                Iterator<Map.Entry<String, JsonNode>> iterator = root.fields();
                while (iterator.hasNext()) {
                    Map.Entry<String, JsonNode> entry = iterator.next();
                    collectJsonField(stepName, "$." + entry.getKey(), entry.getKey(), entry.getValue(), fields);
                }
            } else {
                addJsonNodeField(stepName, "$", "body", root, fields);
            }
        } catch (Exception ex) {
            fields.add(buildField(stepName, ScriptConstants.FIELD_SCOPE_REQUEST_BODY, "$", "body",
                    bodyText, ScriptConstants.DATA_TYPE_STRING, ScriptConstants.DISABLED));
            warnings.add("Request body is not JSON; imported as a raw REQUEST_BODY field.");
        }
        return fields;
    }

    /**
     * Infers a compact data type from a string value.
     *
     * @param value imported text value
     * @return data type
     */
    public String inferStringType(String value) {
        if (value == null) {
            return ScriptConstants.DATA_TYPE_STRING;
        }
        String normalized = value.trim();
        if ("true".equalsIgnoreCase(normalized) || "false".equalsIgnoreCase(normalized)) {
            return ScriptConstants.DATA_TYPE_BOOLEAN;
        }
        if (normalized.matches("-?\\d+(\\.\\d+)?")) {
            return ScriptConstants.DATA_TYPE_NUMBER;
        }
        return ScriptConstants.DATA_TYPE_STRING;
    }

    private void collectJsonField(String stepName,
                                  String path,
                                  String key,
                                  JsonNode node,
                                  List<ImportPreviewFieldResponse> fields) throws JsonProcessingException {
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = iterator.next();
                collectJsonField(stepName, path + "." + entry.getKey(), entry.getKey(), entry.getValue(), fields);
            }
            return;
        }
        addJsonNodeField(stepName, path, key, node, fields);
    }

    private void addJsonNodeField(String stepName,
                                  String path,
                                  String key,
                                  JsonNode node,
                                  List<ImportPreviewFieldResponse> fields) throws JsonProcessingException {
        String dataType = inferJsonType(node);
        Integer arrayFlag = node.isArray() ? ScriptConstants.ENABLED : ScriptConstants.DISABLED;
        fields.add(buildField(stepName, ScriptConstants.FIELD_SCOPE_REQUEST_BODY, path, key,
                jsonDefaultValue(node), dataType, arrayFlag));
    }

    private String inferJsonType(JsonNode node) {
        if (node.isNumber()) {
            return ScriptConstants.DATA_TYPE_NUMBER;
        }
        if (node.isBoolean()) {
            return ScriptConstants.DATA_TYPE_BOOLEAN;
        }
        if (node.isObject()) {
            return ScriptConstants.DATA_TYPE_OBJECT;
        }
        if (node.isArray()) {
            return ScriptConstants.DATA_TYPE_ARRAY;
        }
        return ScriptConstants.DATA_TYPE_STRING;
    }

    private String jsonDefaultValue(JsonNode node) throws JsonProcessingException {
        if (node == null || node.isMissingNode() || node.isNull()) {
            return "";
        }
        if (node.isValueNode()) {
            return node.asText();
        }
        return objectMapper.writeValueAsString(node);
    }

    private boolean isSensitive(String fieldKey, String fieldPath) {
        String value = ((fieldKey == null ? "" : fieldKey) + " " + (fieldPath == null ? "" : fieldPath))
                .toLowerCase(Locale.ROOT);
        return value.contains("password")
                || value.contains("token")
                || value.contains("secret")
                || value.contains("authorization")
                || value.contains("cookie");
    }

    private String stableFieldKey(String stepName, String fieldScope, String fieldPath) {
        String raw = safe(stepName) + "." + safe(fieldScope) + "." + safe(fieldPath);
        String cleaned = raw.replaceAll("[^A-Za-z0-9_.-]+", "_");
        cleaned = cleaned.replaceAll("_+", "_");
        if (cleaned.length() > STABLE_KEY_MAX_LENGTH) {
            return cleaned.substring(0, STABLE_KEY_MAX_LENGTH);
        }
        return cleaned;
    }

    private String safe(String value) {
        return value == null || value.trim().isEmpty() ? "unknown" : value.trim();
    }
}
