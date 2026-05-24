package com.apidemo.script.service.impl;

import com.apidemo.common.error.ErrorCode;
import com.apidemo.common.exception.BizException;
import com.apidemo.common.util.IdGenerator;
import com.apidemo.script.constant.ScriptConstants;
import com.apidemo.script.dto.CaseDataSetDetailResponse;
import com.apidemo.script.dto.CaseDataSetRequest;
import com.apidemo.script.dto.CaseDataSetResponse;
import com.apidemo.script.dto.CaseFieldValueBatchSaveRequest;
import com.apidemo.script.dto.CaseFieldValueResponse;
import com.apidemo.script.dto.CaseFieldValueSaveItem;
import com.apidemo.script.entity.CaseDataSetEntity;
import com.apidemo.script.entity.CaseFieldValueEntity;
import com.apidemo.script.entity.FieldConfigEntity;
import com.apidemo.script.entity.ScriptVersionEntity;
import com.apidemo.script.mapper.CaseDataSetMapper;
import com.apidemo.script.mapper.CaseFieldValueMapper;
import com.apidemo.script.mapper.FieldConfigMapper;
import com.apidemo.script.mapper.ScriptVersionMapper;
import com.apidemo.script.service.CaseDataSetService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default case data set and field override value service implementation.
 */
@Service
public class CaseDataSetServiceImpl implements CaseDataSetService {

    private final ScriptVersionMapper scriptVersionMapper;
    private final FieldConfigMapper fieldConfigMapper;
    private final CaseDataSetMapper caseDataSetMapper;
    private final CaseFieldValueMapper caseFieldValueMapper;

    public CaseDataSetServiceImpl(ScriptVersionMapper scriptVersionMapper,
                                  FieldConfigMapper fieldConfigMapper,
                                  CaseDataSetMapper caseDataSetMapper,
                                  CaseFieldValueMapper caseFieldValueMapper) {
        this.scriptVersionMapper = scriptVersionMapper;
        this.fieldConfigMapper = fieldConfigMapper;
        this.caseDataSetMapper = caseDataSetMapper;
        this.caseFieldValueMapper = caseFieldValueMapper;
    }

    /**
     * Creates a case data set after validating script version ownership.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param request case data set request
     * @return created case data set
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CaseDataSetResponse createCaseDataSet(String scriptId, String versionId, CaseDataSetRequest request) {
        getExistingVersion(scriptId, versionId);
        LocalDateTime now = LocalDateTime.now();
        CaseDataSetEntity caseDataSet = buildCaseDataSet(scriptId, versionId, request);
        caseDataSet.setId(IdGenerator.nextId());
        caseDataSet.setCreatedTime(now);
        caseDataSet.setUpdatedTime(now);
        caseDataSet.setDeleted(ScriptConstants.NOT_DELETED);
        caseDataSetMapper.insert(caseDataSet);
        return toCaseDataSetResponse(caseDataSet);
    }

    /**
     * Lists active case data sets under a script version.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @return case data set list
     */
    @Override
    public List<CaseDataSetResponse> listCaseDataSets(String scriptId, String versionId) {
        getExistingVersion(scriptId, versionId);
        List<CaseDataSetEntity> caseDataSets = caseDataSetMapper.listByVersion(scriptId, versionId);
        List<CaseDataSetResponse> responses = new ArrayList<CaseDataSetResponse>();
        for (CaseDataSetEntity caseDataSet : caseDataSets) {
            responses.add(toCaseDataSetResponse(caseDataSet));
        }
        return responses;
    }

    /**
     * Gets case data set detail with field override values.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param caseDataSetId case data set id
     * @return case data set detail
     */
    @Override
    public CaseDataSetDetailResponse getCaseDataSetDetail(String scriptId, String versionId, String caseDataSetId) {
        CaseDataSetEntity caseDataSet = getExistingCaseDataSet(scriptId, versionId, caseDataSetId);
        CaseDataSetDetailResponse response = new CaseDataSetDetailResponse();
        response.setCaseDataSet(toCaseDataSetResponse(caseDataSet));
        response.setValues(caseFieldValueMapper.listByCaseDataSetId(caseDataSetId));
        return response;
    }

    /**
     * Updates a case data set.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param caseDataSetId case data set id
     * @param request case data set request
     * @return updated case data set
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CaseDataSetResponse updateCaseDataSet(String scriptId, String versionId, String caseDataSetId, CaseDataSetRequest request) {
        CaseDataSetEntity existingCaseDataSet = getExistingCaseDataSet(scriptId, versionId, caseDataSetId);
        CaseDataSetEntity caseDataSet = buildCaseDataSet(scriptId, versionId, request);
        caseDataSet.setId(existingCaseDataSet.getId());
        caseDataSet.setCreatedTime(existingCaseDataSet.getCreatedTime());
        caseDataSet.setUpdatedTime(LocalDateTime.now());
        caseDataSet.setDeleted(ScriptConstants.NOT_DELETED);
        caseDataSetMapper.update(caseDataSet);
        return toCaseDataSetResponse(getExistingCaseDataSet(scriptId, versionId, caseDataSetId));
    }

    /**
     * Logically deletes a case data set and its field override values.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param caseDataSetId case data set id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCaseDataSet(String scriptId, String versionId, String caseDataSetId) {
        getExistingCaseDataSet(scriptId, versionId, caseDataSetId);
        LocalDateTime now = LocalDateTime.now();
        caseDataSetMapper.logicalDelete(caseDataSetId, scriptId, versionId, now, null);
        caseFieldValueMapper.logicalDeleteByCaseDataSetId(caseDataSetId, now, null);
    }

    /**
     * Batch saves case field override values, validating every field belongs to the same script version.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param caseDataSetId case data set id
     * @param request batch save request
     * @return saved field override value list
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<CaseFieldValueResponse> saveCaseFieldValues(String scriptId, String versionId, String caseDataSetId, CaseFieldValueBatchSaveRequest request) {
        getExistingCaseDataSet(scriptId, versionId, caseDataSetId);
        LocalDateTime now = LocalDateTime.now();
        for (CaseFieldValueSaveItem item : request.getValues()) {
            getExistingField(scriptId, versionId, item.getFieldConfigId());
            CaseFieldValueEntity existingValue = caseFieldValueMapper.selectByCaseAndFieldConfigId(caseDataSetId, item.getFieldConfigId());
            if (existingValue == null) {
                CaseFieldValueEntity value = new CaseFieldValueEntity();
                value.setId(IdGenerator.nextId());
                value.setCaseDataSetId(caseDataSetId);
                value.setFieldConfigId(item.getFieldConfigId());
                value.setValue(item.getValue());
                value.setValueSource(defaultValueSource(item.getValueSource()));
                value.setCreatedTime(now);
                value.setUpdatedTime(now);
                value.setDeleted(ScriptConstants.NOT_DELETED);
                caseFieldValueMapper.insert(value);
            } else {
                existingValue.setValue(item.getValue());
                existingValue.setValueSource(defaultValueSource(item.getValueSource()));
                existingValue.setUpdatedTime(now);
                existingValue.setDeleted(ScriptConstants.NOT_DELETED);
                caseFieldValueMapper.update(existingValue);
            }
        }
        return caseFieldValueMapper.listByCaseDataSetId(caseDataSetId);
    }

    /**
     * Lists case field override values.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param caseDataSetId case data set id
     * @return field override value list
     */
    @Override
    public List<CaseFieldValueResponse> listCaseFieldValues(String scriptId, String versionId, String caseDataSetId) {
        getExistingCaseDataSet(scriptId, versionId, caseDataSetId);
        return caseFieldValueMapper.listByCaseDataSetId(caseDataSetId);
    }

    private ScriptVersionEntity getExistingVersion(String scriptId, String versionId) {
        ScriptVersionEntity version = scriptVersionMapper.selectByScriptIdAndId(scriptId, versionId);
        if (version == null) {
            throw new BizException(ErrorCode.RESOURCE_NOT_FOUND, "脚本版本不存在");
        }
        return version;
    }

    private CaseDataSetEntity getExistingCaseDataSet(String scriptId, String versionId, String caseDataSetId) {
        getExistingVersion(scriptId, versionId);
        CaseDataSetEntity caseDataSet = caseDataSetMapper.selectByScriptVersionAndId(scriptId, versionId, caseDataSetId);
        if (caseDataSet == null) {
            throw new BizException(ErrorCode.RESOURCE_NOT_FOUND, "用例数据集不存在");
        }
        return caseDataSet;
    }

    private FieldConfigEntity getExistingField(String scriptId, String versionId, String fieldConfigId) {
        FieldConfigEntity fieldConfig = fieldConfigMapper.selectByScriptVersionAndId(scriptId, versionId, fieldConfigId);
        if (fieldConfig == null) {
            throw new BizException(ErrorCode.RESOURCE_NOT_FOUND, "字段配置不存在或不属于当前版本");
        }
        return fieldConfig;
    }

    private CaseDataSetEntity buildCaseDataSet(String scriptId, String versionId, CaseDataSetRequest request) {
        CaseDataSetEntity caseDataSet = new CaseDataSetEntity();
        caseDataSet.setScriptId(scriptId);
        caseDataSet.setScriptVersionId(versionId);
        caseDataSet.setName(request.getName());
        caseDataSet.setDescription(request.getDescription());
        caseDataSet.setStatus(defaultStatus(request.getStatus()));
        return caseDataSet;
    }

    private String defaultStatus(String status) {
        return status == null || status.trim().isEmpty() ? ScriptConstants.STATUS_ENABLED : status;
    }

    private String defaultValueSource(String valueSource) {
        return valueSource == null || valueSource.trim().isEmpty() ? ScriptConstants.VALUE_SOURCE_MANUAL : valueSource;
    }

    private CaseDataSetResponse toCaseDataSetResponse(CaseDataSetEntity caseDataSet) {
        CaseDataSetResponse response = new CaseDataSetResponse();
        response.setId(caseDataSet.getId());
        response.setScriptId(caseDataSet.getScriptId());
        response.setScriptVersionId(caseDataSet.getScriptVersionId());
        response.setName(caseDataSet.getName());
        response.setDescription(caseDataSet.getDescription());
        response.setStatus(caseDataSet.getStatus());
        response.setCreatedTime(caseDataSet.getCreatedTime());
        response.setUpdatedTime(caseDataSet.getUpdatedTime());
        return response;
    }
}
