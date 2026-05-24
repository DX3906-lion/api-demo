package com.apidemo.script.service.impl;

import com.apidemo.common.error.ErrorCode;
import com.apidemo.common.exception.BizException;
import com.apidemo.common.util.IdGenerator;
import com.apidemo.script.constant.ScriptConstants;
import com.apidemo.script.dto.ImportConfirmFieldRequest;
import com.apidemo.script.dto.ImportConfirmRequest;
import com.apidemo.script.dto.ImportConfirmResponse;
import com.apidemo.script.dto.ImportConfirmStepRequest;
import com.apidemo.script.dto.ImportPreviewResponse;
import com.apidemo.script.entity.FieldConfigEntity;
import com.apidemo.script.entity.ScriptEntity;
import com.apidemo.script.entity.ScriptFieldDefaultEntity;
import com.apidemo.script.entity.ScriptVersionEntity;
import com.apidemo.script.entity.StepDefinitionEntity;
import com.apidemo.script.mapper.FieldConfigMapper;
import com.apidemo.script.mapper.ScriptFieldDefaultMapper;
import com.apidemo.script.mapper.ScriptMapper;
import com.apidemo.script.mapper.ScriptVersionMapper;
import com.apidemo.script.mapper.StepDefinitionMapper;
import com.apidemo.script.parser.ImportFileParser;
import com.apidemo.script.service.ImportService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Default import service implementation for previewing and confirming HAR/Postman imports.
 */
@Service
public class ImportServiceImpl implements ImportService {

    private static final int STABLE_KEY_MAX_LENGTH = 500;

    private final ObjectMapper objectMapper;
    private final List<ImportFileParser> parsers;
    private final ScriptMapper scriptMapper;
    private final ScriptVersionMapper scriptVersionMapper;
    private final StepDefinitionMapper stepDefinitionMapper;
    private final FieldConfigMapper fieldConfigMapper;
    private final ScriptFieldDefaultMapper scriptFieldDefaultMapper;

    public ImportServiceImpl(ObjectMapper objectMapper,
                             List<ImportFileParser> parsers,
                             ScriptMapper scriptMapper,
                             ScriptVersionMapper scriptVersionMapper,
                             StepDefinitionMapper stepDefinitionMapper,
                             FieldConfigMapper fieldConfigMapper,
                             ScriptFieldDefaultMapper scriptFieldDefaultMapper) {
        this.objectMapper = objectMapper;
        this.parsers = parsers;
        this.scriptMapper = scriptMapper;
        this.scriptVersionMapper = scriptVersionMapper;
        this.stepDefinitionMapper = stepDefinitionMapper;
        this.fieldConfigMapper = fieldConfigMapper;
        this.scriptFieldDefaultMapper = scriptFieldDefaultMapper;
    }

    /**
     * Parses an uploaded file without creating any database rows.
     *
     * @param file uploaded HAR or Postman collection file
     * @param importType optional import type
     * @return preview response
     */
    @Override
    public ImportPreviewResponse preview(MultipartFile file, String importType) {
        if (file == null || file.isEmpty()) {
            throw new BizException(ErrorCode.PARAM_INVALID, "Import file must not be empty");
        }
        JsonNode root = readJson(file);
        String resolvedType = normalizeImportType(importType);
        if (resolvedType == null) {
            resolvedType = detectImportType(root);
        }
        return getParser(resolvedType).parse(root);
    }

    /**
     * Persists imported steps, field configs, and field defaults into a DRAFT version atomically.
     *
     * @param scriptId target script id
     * @param versionId target version id
     * @param request import confirmation request
     * @return confirmation summary
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImportConfirmResponse confirm(String scriptId, String versionId, ImportConfirmRequest request) {
        assertDraftVersion(scriptId, versionId);
        if (request == null || request.getSteps() == null || request.getSteps().isEmpty()) {
            throw new BizException(ErrorCode.PARAM_INVALID, "Import steps must not be empty");
        }

        Integer maxSortNo = stepDefinitionMapper.selectMaxSortNo(scriptId, versionId);
        int nextSortNo = maxSortNo == null ? 1 : maxSortNo + 1;
        LocalDateTime now = LocalDateTime.now();
        Set<String> usedStableKeys = new HashSet<String>();
        List<String> stepIds = new ArrayList<String>();
        int fieldCount = 0;
        int defaultCount = 0;

        for (ImportConfirmStepRequest stepRequest : request.getSteps()) {
            StepDefinitionEntity step = buildStep(scriptId, versionId, stepRequest, nextSortNo, now);
            stepDefinitionMapper.insert(step);
            stepIds.add(step.getId());
            nextSortNo++;

            if (stepRequest.getFields() == null) {
                continue;
            }
            for (ImportConfirmFieldRequest fieldRequest : stepRequest.getFields()) {
                FieldConfigEntity field = buildField(scriptId, versionId, step.getId(), step.getName(), fieldRequest, now, usedStableKeys);
                fieldConfigMapper.insert(field);
                fieldCount++;
                if (fieldRequest.getDefaultValue() != null) {
                    ScriptFieldDefaultEntity fieldDefault = buildFieldDefault(scriptId, versionId, field.getId(), fieldRequest, now);
                    scriptFieldDefaultMapper.insert(fieldDefault);
                    defaultCount++;
                }
            }
        }

        ImportConfirmResponse response = new ImportConfirmResponse();
        response.setScriptId(scriptId);
        response.setScriptVersionId(versionId);
        response.setImportedStepCount(stepIds.size());
        response.setImportedFieldCount(fieldCount);
        response.setImportedDefaultCount(defaultCount);
        response.setStepIds(stepIds);
        return response;
    }

    private JsonNode readJson(MultipartFile file) {
        try {
            return objectMapper.readTree(file.getInputStream());
        } catch (IOException ex) {
            throw new BizException(ErrorCode.IMPORT_PARSE_FAILED, "Import file is not valid JSON");
        }
    }

    private String detectImportType(JsonNode root) {
        if (root.path("log").path("entries").isArray()) {
            return ScriptConstants.IMPORT_TYPE_HAR;
        }
        if (root.path("item").isArray()) {
            return ScriptConstants.IMPORT_TYPE_POSTMAN;
        }
        throw new BizException(ErrorCode.IMPORT_PARSE_FAILED, "Unable to detect import type");
    }

    private ImportFileParser getParser(String importType) {
        for (ImportFileParser parser : parsers) {
            if (parser.getImportType().equals(importType)) {
                return parser;
            }
        }
        throw new BizException(ErrorCode.PARAM_INVALID, "Unsupported import type");
    }

    private String normalizeImportType(String importType) {
        if (importType == null || importType.trim().isEmpty()) {
            return null;
        }
        String normalized = importType.trim().toUpperCase(Locale.ROOT);
        if (!ScriptConstants.IMPORT_TYPE_HAR.equals(normalized)
                && !ScriptConstants.IMPORT_TYPE_POSTMAN.equals(normalized)) {
            throw new BizException(ErrorCode.PARAM_INVALID, "Unsupported import type");
        }
        return normalized;
    }

    private ScriptVersionEntity assertDraftVersion(String scriptId, String versionId) {
        ScriptEntity script = scriptMapper.selectById(scriptId);
        if (script == null) {
            throw new BizException(ErrorCode.RESOURCE_NOT_FOUND, "Script does not exist");
        }
        ScriptVersionEntity version = scriptVersionMapper.selectByScriptIdAndId(scriptId, versionId);
        if (version == null) {
            throw new BizException(ErrorCode.RESOURCE_NOT_FOUND, "Script version does not exist");
        }
        if (!ScriptConstants.STATUS_DRAFT.equals(version.getVersionStatus())) {
            throw new BizException(ErrorCode.VERSION_STATUS_INVALID, "Only DRAFT versions can be imported into");
        }
        return version;
    }

    private StepDefinitionEntity buildStep(String scriptId,
                                           String versionId,
                                           ImportConfirmStepRequest request,
                                           int sortNo,
                                           LocalDateTime now) {
        StepDefinitionEntity step = new StepDefinitionEntity();
        step.setId(IdGenerator.nextId());
        step.setScriptId(scriptId);
        step.setScriptVersionId(versionId);
        step.setStepType(defaultString(request.getStepType(), ScriptConstants.STEP_TYPE_HTTP));
        step.setName(request.getName());
        step.setSortNo(sortNo);
        step.setRequestMethod(request.getRequestMethod());
        step.setRequestUrl(request.getRequestUrl());
        step.setRequestConfig(defaultString(request.getRequestConfig(), ScriptConstants.EMPTY_JSON_OBJECT));
        step.setAssertionConfig(ScriptConstants.EMPTY_JSON_OBJECT);
        step.setExtractorConfig(ScriptConstants.EMPTY_JSON_OBJECT);
        step.setEnabled(ScriptConstants.ENABLED);
        step.setCreatedTime(now);
        step.setUpdatedTime(now);
        step.setDeleted(ScriptConstants.NOT_DELETED);
        return step;
    }

    private FieldConfigEntity buildField(String scriptId,
                                         String versionId,
                                         String stepId,
                                         String stepName,
                                         ImportConfirmFieldRequest request,
                                         LocalDateTime now,
                                         Set<String> usedStableKeys) {
        FieldConfigEntity field = new FieldConfigEntity();
        field.setId(IdGenerator.nextId());
        field.setScriptId(scriptId);
        field.setScriptVersionId(versionId);
        field.setStepId(stepId);
        field.setFieldScope(request.getFieldScope());
        field.setFieldPath(request.getFieldPath());
        field.setFieldKey(request.getFieldKey());
        field.setStableFieldKey(uniqueStableFieldKey(versionId, stepName, request, usedStableKeys));
        field.setFieldName(request.getFieldName());
        field.setDataType(defaultString(request.getDataType(), ScriptConstants.DATA_TYPE_STRING));
        field.setRequired(defaultInteger(request.getRequired(), ScriptConstants.DISABLED));
        field.setArrayFlag(defaultInteger(request.getArrayFlag(), ScriptConstants.DISABLED));
        field.setSensitive(defaultInteger(request.getSensitive(), ScriptConstants.DISABLED));
        field.setCreatedTime(now);
        field.setUpdatedTime(now);
        field.setDeleted(ScriptConstants.NOT_DELETED);
        return field;
    }

    private ScriptFieldDefaultEntity buildFieldDefault(String scriptId,
                                                       String versionId,
                                                       String fieldConfigId,
                                                       ImportConfirmFieldRequest request,
                                                       LocalDateTime now) {
        ScriptFieldDefaultEntity fieldDefault = new ScriptFieldDefaultEntity();
        fieldDefault.setId(IdGenerator.nextId());
        fieldDefault.setScriptId(scriptId);
        fieldDefault.setScriptVersionId(versionId);
        fieldDefault.setFieldConfigId(fieldConfigId);
        fieldDefault.setDefaultValue(request.getDefaultValue());
        fieldDefault.setValueSource(defaultString(request.getValueSource(), ScriptConstants.VALUE_SOURCE_IMPORT));
        fieldDefault.setCreatedTime(now);
        fieldDefault.setUpdatedTime(now);
        fieldDefault.setDeleted(ScriptConstants.NOT_DELETED);
        return fieldDefault;
    }

    private String uniqueStableFieldKey(String versionId,
                                        String stepName,
                                        ImportConfirmFieldRequest request,
                                        Set<String> usedStableKeys) {
        String base = request.getStableFieldKey();
        if (base == null || base.trim().isEmpty()) {
            base = stepName + "." + request.getFieldScope() + "." + request.getFieldPath();
        }
        base = sanitizeStableFieldKey(base);
        String candidate = base;
        int index = 1;
        while (usedStableKeys.contains(candidate) || fieldConfigMapper.countByStableFieldKey(versionId, candidate, null) > 0) {
            String suffix = "_" + index;
            int maxBaseLength = STABLE_KEY_MAX_LENGTH - suffix.length();
            candidate = (base.length() > maxBaseLength ? base.substring(0, maxBaseLength) : base) + suffix;
            index++;
        }
        usedStableKeys.add(candidate);
        return candidate;
    }

    private String sanitizeStableFieldKey(String value) {
        String cleaned = defaultString(value, "unknown").replaceAll("[^A-Za-z0-9_.-]+", "_");
        cleaned = cleaned.replaceAll("_+", "_");
        if (cleaned.length() > STABLE_KEY_MAX_LENGTH) {
            return cleaned.substring(0, STABLE_KEY_MAX_LENGTH);
        }
        return cleaned;
    }

    private String defaultString(String value, String defaultValue) {
        return value == null || value.trim().isEmpty() ? defaultValue : value;
    }

    private Integer defaultInteger(Integer value, Integer defaultValue) {
        return value == null ? defaultValue : value;
    }
}
