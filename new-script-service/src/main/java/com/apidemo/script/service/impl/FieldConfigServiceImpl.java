package com.apidemo.script.service.impl;

import com.apidemo.common.error.ErrorCode;
import com.apidemo.common.exception.BizException;
import com.apidemo.common.util.IdGenerator;
import com.apidemo.script.constant.ScriptConstants;
import com.apidemo.script.dto.FieldConfigRequest;
import com.apidemo.script.dto.FieldConfigResponse;
import com.apidemo.script.dto.ScriptFieldDefaultRequest;
import com.apidemo.script.dto.ScriptFieldDefaultResponse;
import com.apidemo.script.entity.FieldConfigEntity;
import com.apidemo.script.entity.ScriptFieldDefaultEntity;
import com.apidemo.script.entity.ScriptVersionEntity;
import com.apidemo.script.mapper.FieldConfigMapper;
import com.apidemo.script.mapper.ScriptFieldDefaultMapper;
import com.apidemo.script.mapper.ScriptVersionMapper;
import com.apidemo.script.mapper.StepDefinitionMapper;
import com.apidemo.script.service.FieldConfigService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default field configuration and script default value service implementation.
 */
@Service
public class FieldConfigServiceImpl implements FieldConfigService {

    private final ScriptVersionMapper scriptVersionMapper;
    private final StepDefinitionMapper stepDefinitionMapper;
    private final FieldConfigMapper fieldConfigMapper;
    private final ScriptFieldDefaultMapper scriptFieldDefaultMapper;

    public FieldConfigServiceImpl(ScriptVersionMapper scriptVersionMapper,
                                  StepDefinitionMapper stepDefinitionMapper,
                                  FieldConfigMapper fieldConfigMapper,
                                  ScriptFieldDefaultMapper scriptFieldDefaultMapper) {
        this.scriptVersionMapper = scriptVersionMapper;
        this.stepDefinitionMapper = stepDefinitionMapper;
        this.fieldConfigMapper = fieldConfigMapper;
        this.scriptFieldDefaultMapper = scriptFieldDefaultMapper;
    }

    /**
     * Lists active field configurations.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param stepId optional step id filter
     * @param fieldScope optional field scope filter
     * @return field configuration list
     */
    @Override
    public List<FieldConfigResponse> listFields(String scriptId, String versionId, String stepId, String fieldScope) {
        getExistingVersion(scriptId, versionId);
        List<FieldConfigEntity> fields = fieldConfigMapper.listByVersion(scriptId, versionId, stepId, fieldScope);
        List<FieldConfigResponse> responses = new ArrayList<FieldConfigResponse>();
        for (FieldConfigEntity field : fields) {
            responses.add(toFieldResponse(field));
        }
        return responses;
    }

    /**
     * Creates a field configuration under a DRAFT version.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param request field configuration request
     * @return created field configuration
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FieldConfigResponse createField(String scriptId, String versionId, FieldConfigRequest request) {
        assertDraftVersion(scriptId, versionId);
        validateStep(scriptId, versionId, request.getStepId());
        validateStableFieldKeyUnique(versionId, request.getStableFieldKey(), null);

        LocalDateTime now = LocalDateTime.now();
        FieldConfigEntity field = buildFieldConfig(scriptId, versionId, request);
        field.setId(IdGenerator.nextId());
        field.setCreatedTime(now);
        field.setUpdatedTime(now);
        field.setDeleted(ScriptConstants.NOT_DELETED);
        fieldConfigMapper.insert(field);
        return toFieldResponse(field);
    }

    /**
     * Updates a field configuration under a DRAFT version.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param fieldId field configuration id
     * @param request field configuration request
     * @return updated field configuration
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FieldConfigResponse updateField(String scriptId, String versionId, String fieldId, FieldConfigRequest request) {
        assertDraftVersion(scriptId, versionId);
        FieldConfigEntity existingField = getExistingField(scriptId, versionId, fieldId);
        validateStep(scriptId, versionId, request.getStepId());
        validateStableFieldKeyUnique(versionId, request.getStableFieldKey(), fieldId);

        FieldConfigEntity field = buildFieldConfig(scriptId, versionId, request);
        field.setId(existingField.getId());
        field.setCreatedTime(existingField.getCreatedTime());
        field.setUpdatedTime(LocalDateTime.now());
        field.setDeleted(ScriptConstants.NOT_DELETED);
        fieldConfigMapper.update(field);
        return toFieldResponse(getExistingField(scriptId, versionId, fieldId));
    }

    /**
     * Logically deletes a field configuration and its default under a DRAFT version.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param fieldId field configuration id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteField(String scriptId, String versionId, String fieldId) {
        assertDraftVersion(scriptId, versionId);
        getExistingField(scriptId, versionId, fieldId);
        LocalDateTime now = LocalDateTime.now();
        fieldConfigMapper.logicalDelete(fieldId, scriptId, versionId, now, null);
        scriptFieldDefaultMapper.logicalDeleteByFieldConfigId(fieldId, now, null);
    }

    /**
     * Saves or updates a field default under a DRAFT version.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param fieldId field configuration id
     * @param request field default value request
     * @return saved default value
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScriptFieldDefaultResponse saveDefault(String scriptId, String versionId, String fieldId, ScriptFieldDefaultRequest request) {
        assertDraftVersion(scriptId, versionId);
        FieldConfigEntity field = getExistingField(scriptId, versionId, fieldId);
        LocalDateTime now = LocalDateTime.now();
        ScriptFieldDefaultEntity existingDefault = scriptFieldDefaultMapper.selectByVersionAndFieldConfigId(versionId, fieldId);

        if (existingDefault == null) {
            ScriptFieldDefaultEntity fieldDefault = new ScriptFieldDefaultEntity();
            fieldDefault.setId(IdGenerator.nextId());
            fieldDefault.setScriptId(scriptId);
            fieldDefault.setScriptVersionId(versionId);
            fieldDefault.setFieldConfigId(fieldId);
            fieldDefault.setDefaultValue(request.getDefaultValue());
            fieldDefault.setValueSource(defaultValueSource(request.getValueSource()));
            fieldDefault.setCreatedTime(now);
            fieldDefault.setUpdatedTime(now);
            fieldDefault.setDeleted(ScriptConstants.NOT_DELETED);
            scriptFieldDefaultMapper.insert(fieldDefault);
        } else {
            existingDefault.setDefaultValue(request.getDefaultValue());
            existingDefault.setValueSource(defaultValueSource(request.getValueSource()));
            existingDefault.setUpdatedTime(now);
            existingDefault.setDeleted(ScriptConstants.NOT_DELETED);
            scriptFieldDefaultMapper.update(existingDefault);
        }

        return findDefaultResponse(scriptId, versionId, field);
    }

    /**
     * Lists field default values with field metadata.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param stepId optional step id filter
     * @param fieldScope optional field scope filter
     * @return field default list
     */
    @Override
    public List<ScriptFieldDefaultResponse> listDefaults(String scriptId, String versionId, String stepId, String fieldScope) {
        getExistingVersion(scriptId, versionId);
        return scriptFieldDefaultMapper.listDefaults(scriptId, versionId, stepId, fieldScope);
    }

    private ScriptVersionEntity assertDraftVersion(String scriptId, String versionId) {
        ScriptVersionEntity version = getExistingVersion(scriptId, versionId);
        if (!ScriptConstants.STATUS_DRAFT.equals(version.getVersionStatus())) {
            throw new BizException(ErrorCode.VERSION_STATUS_INVALID, "已发布版本不可修改字段配置");
        }
        return version;
    }

    private ScriptVersionEntity getExistingVersion(String scriptId, String versionId) {
        ScriptVersionEntity version = scriptVersionMapper.selectByScriptIdAndId(scriptId, versionId);
        if (version == null) {
            throw new BizException(ErrorCode.RESOURCE_NOT_FOUND, "脚本版本不存在");
        }
        return version;
    }

    private void validateStep(String scriptId, String versionId, String stepId) {
        if (stepDefinitionMapper.selectByScriptVersionAndId(scriptId, versionId, stepId) == null) {
            throw new BizException(ErrorCode.RESOURCE_NOT_FOUND, "步骤不存在");
        }
    }

    private FieldConfigEntity getExistingField(String scriptId, String versionId, String fieldId) {
        FieldConfigEntity field = fieldConfigMapper.selectByScriptVersionAndId(scriptId, versionId, fieldId);
        if (field == null) {
            throw new BizException(ErrorCode.RESOURCE_NOT_FOUND, "字段配置不存在");
        }
        return field;
    }

    private void validateStableFieldKeyUnique(String versionId, String stableFieldKey, String excludeId) {
        if (stableFieldKey == null || stableFieldKey.trim().isEmpty()) {
            return;
        }
        if (fieldConfigMapper.countByStableFieldKey(versionId, stableFieldKey, excludeId) > 0) {
            throw new BizException(ErrorCode.BUSINESS_CONFLICT, "稳定字段标识在当前版本下已存在");
        }
    }

    private FieldConfigEntity buildFieldConfig(String scriptId, String versionId, FieldConfigRequest request) {
        FieldConfigEntity field = new FieldConfigEntity();
        field.setScriptId(scriptId);
        field.setScriptVersionId(versionId);
        field.setStepId(request.getStepId());
        field.setFieldScope(request.getFieldScope());
        field.setFieldPath(request.getFieldPath());
        field.setFieldKey(request.getFieldKey());
        field.setStableFieldKey(request.getStableFieldKey());
        field.setFieldName(request.getFieldName());
        field.setDataType(request.getDataType());
        field.setRequired(defaultFlag(request.getRequired()));
        field.setArrayFlag(defaultFlag(request.getArrayFlag()));
        field.setSensitive(defaultFlag(request.getSensitive()));
        field.setDescription(request.getDescription());
        return field;
    }

    private Integer defaultFlag(Integer value) {
        return value == null ? ScriptConstants.DISABLED : value;
    }

    private String defaultValueSource(String valueSource) {
        return valueSource == null || valueSource.trim().isEmpty() ? ScriptConstants.VALUE_SOURCE_MANUAL : valueSource;
    }

    private ScriptFieldDefaultResponse findDefaultResponse(String scriptId, String versionId, FieldConfigEntity field) {
        List<ScriptFieldDefaultResponse> defaults = scriptFieldDefaultMapper.listDefaults(scriptId, versionId, field.getStepId(), field.getFieldScope());
        for (ScriptFieldDefaultResponse response : defaults) {
            if (field.getId().equals(response.getFieldConfigId())) {
                return response;
            }
        }
        throw new BizException(ErrorCode.RESOURCE_NOT_FOUND, "字段默认值不存在");
    }

    private FieldConfigResponse toFieldResponse(FieldConfigEntity field) {
        FieldConfigResponse response = new FieldConfigResponse();
        response.setId(field.getId());
        response.setScriptId(field.getScriptId());
        response.setScriptVersionId(field.getScriptVersionId());
        response.setStepId(field.getStepId());
        response.setFieldScope(field.getFieldScope());
        response.setFieldPath(field.getFieldPath());
        response.setFieldKey(field.getFieldKey());
        response.setStableFieldKey(field.getStableFieldKey());
        response.setFieldName(field.getFieldName());
        response.setDataType(field.getDataType());
        response.setRequired(field.getRequired());
        response.setArrayFlag(field.getArrayFlag());
        response.setSensitive(field.getSensitive());
        response.setDescription(field.getDescription());
        response.setCreatedTime(field.getCreatedTime());
        response.setUpdatedTime(field.getUpdatedTime());
        return response;
    }
}
